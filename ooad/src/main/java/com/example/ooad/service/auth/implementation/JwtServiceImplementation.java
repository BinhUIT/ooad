package com.example.ooad.service.auth.implementation;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.RefreshToken;
import com.example.ooad.domain.entity.UnusedAccessToken;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.RefreshTokenRepository;
import com.example.ooad.repository.UnusedAcessTokenRepository;
import com.example.ooad.service.auth.interfaces.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImplementation implements JwtService {

    // Dùng @Value để lấy secret key an toàn cho cả Local và
    // Deploy
    @Value("${application.security.jwt.secret-key}")
    private String secret;

    private static final int thoiHanAcessToken = 1000 * 60 * 30;
    private static final int thoiHanRefreshToken = 1000 * 60 * 60 * 24;

    private final AccountRepository accountRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final UnusedAcessTokenRepository unusedAccessTokenRepo;

    public JwtServiceImplementation(AccountRepository accountRepo, RefreshTokenRepository refreshTokenRepo,
            UnusedAcessTokenRepository unusedAcessTokenRepo) {
        this.accountRepo = accountRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.unusedAccessTokenRepo = unusedAcessTokenRepo;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    @Override
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        long current = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + thoiHanAcessToken))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String email) {
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + thoiHanRefreshToken))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        RefreshToken rfToken = new RefreshToken();
        rfToken.setToken(refreshToken);
        refreshTokenRepo.save(rfToken);
        return refreshToken;
    }

    @Override
    public String extractTenDangNhap(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiredDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Date extractIssueDate(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    private boolean isTokenExpire(String token) {
        return extractExpiredDate(token).before(new Date());
    }

    private boolean isAccessToken(String token) {
        long chenhLechThoiGian = extractExpiredDate(token).getTime() - extractIssueDate(token).getTime();
        // Sửa logic so sánh một chút để tránh lỗi sai số mili giây
        return Math.abs(chenhLechThoiGian - thoiHanRefreshToken) > 1000;
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        if (!isAccessToken(token)) {
            return false;
        }

        if (userDetails instanceof Account account) {
            String username = extractTenDangNhap(token);
            return account.getUsername().equals(username) && !isTokenExpire(token);
        }
        return false;
    }

    @Override
    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token);
        if (refreshToken == null) {
            return false;
        }
        if (userDetails instanceof Account account) {
            String username = extractTenDangNhap(token);
            boolean isValid = account.getUsername().equals(username) && !isTokenExpire(token);
            if (!isValid) {
                refreshTokenRepo.delete(refreshToken);
            }
            return isValid;
        }
        return false;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Account findByToken(String token) {
        String username = extractTenDangNhap(token);
        return accountRepo.findByUsername(username);
    }

    @Override
    public RefreshToken getRefreshTokenByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public UnusedAccessToken getUnusedAccessTokenByToken(String token) {
        return unusedAccessTokenRepo.findByToken(token);
    }

    @Override
    public void saveNewRefreshToken(String token) {
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setExpireAt(extractExpiredDate(token));
        newRefreshToken.setToken(token);
        refreshTokenRepo.save(newRefreshToken);
    }

    @Override
    public void saveUnusedAccessToken(String token) {
        UnusedAccessToken unusedAccessToken = new UnusedAccessToken();
        unusedAccessToken.setExpireAt(extractExpiredDate(token));
        unusedAccessToken.setToken(token);
        unusedAccessTokenRepo.save(unusedAccessToken);
    }

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

    @Override
    public List<UnusedAccessToken> getAllAccessTokenExpired() {
        return unusedAccessTokenRepo.findByExpireAtBefore(new Date());
    }

    @Override
    public void deleteAllExpiredToken() {
        List<UnusedAccessToken> listTokens = getAllAccessTokenExpired();
        unusedAccessTokenRepo.deleteAll(listTokens);
    }

    @Override
    public List<RefreshToken> getAllRefreshTokenExpired() {
        return refreshTokenRepo.findByExpireAtBefore(new Date());
    }

    @Override
    public void deleteAllExpiredRefreshToken() {
        List<RefreshToken> listTokens = getAllRefreshTokenExpired();
        refreshTokenRepo.deleteAll(listTokens);
    }
}
