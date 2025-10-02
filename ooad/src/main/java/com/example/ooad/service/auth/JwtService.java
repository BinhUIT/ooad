package com.example.ooad.service.auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.RefreshToken;
import com.example.ooad.domain.entity.TaiKhoan;
import com.example.ooad.domain.entity.UnusedAccessToken;
import com.example.ooad.repository.RefreshTokenRepository;
import com.example.ooad.repository.TaiKhoanRepository;
import com.example.ooad.repository.UnusedAcessTokenRepository;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String secret = Dotenv.load().get("SECRET_KEY");
    private static final int thoiHanAcessToken = 1000*60*30;
    private static final int thoiHanRefreshToken = 1000*60*60*24;
    private final TaiKhoanRepository taiKhoanRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final UnusedAcessTokenRepository unusedAccessTokenRepo;
    public JwtService(TaiKhoanRepository taiKhoanRepo, RefreshTokenRepository refreshTokenRepo, UnusedAcessTokenRepository unusedAcessTokenRepo) {
        this.taiKhoanRepo = taiKhoanRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.unusedAccessTokenRepo= unusedAcessTokenRepo;
    }
     private Key getSignKey() {
        
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, email);
    }
    private String createToken(Map<String,Object> claims, String email) {
        long current = System.currentTimeMillis();
        return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date(current)).setExpiration(new Date(current + thoiHanAcessToken))
        .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    private String createRefreshToken(Map<String, Object> claims, String email) {
        String refreshToken =Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + thoiHanRefreshToken))
        .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        RefreshToken rfToken = new RefreshToken();
        rfToken.setToken(refreshToken);
        refreshTokenRepo.save(rfToken);
        return refreshToken;
    }
    public String extractTenDangNhap(String token) {
       
        return extractClaim(token,Claims::getSubject );
    }
    public Date extractExpiredDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public Date extractIssueDate(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }
    private boolean isTokenExpire(String token) {
        return extractExpiredDate(token).before(new Date());
    }
    private boolean isAccessToken(String token) {
        long chenhLechThoiGian = extractExpiredDate(token).getTime()-extractIssueDate(token).getTime();
        return chenhLechThoiGian!=thoiHanRefreshToken;
    }
    public boolean validateToken(String token,UserDetails userDetails) {
        if(!isAccessToken(token)) {
            
            return false;
        }
        if(userDetails instanceof TaiKhoan taiKhoan) {
            
            String tenTaiKhoan = extractTenDangNhap(token);
            return taiKhoan.getTenDangNhap().equals(tenTaiKhoan)&&!isTokenExpire(token);
        } 
        return false;
    }
    public boolean validateRefreshToken(String token , UserDetails userDetails) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token);
        if(refreshToken==null) {
            return false;
        } 
        if(userDetails instanceof TaiKhoan taiKhoan) {
         String tenTaiKhoan = extractTenDangNhap(token);
         boolean isValid=taiKhoan.getTenDangNhap().equals(tenTaiKhoan)&&!isTokenExpire(token);
         if(!isValid) {
            refreshTokenRepo.delete(refreshToken);
        }
        return isValid;
        }
        return false;
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
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
    public TaiKhoan findByToken(String token) {
        String tenTaiKhoan = extractTenDangNhap(token);
        return taiKhoanRepo.findByTenDangNhap(tenTaiKhoan);
    }
    public RefreshToken getRefreshTokenByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    } 
    public UnusedAccessToken getUnusedAccessTokenByToken(String token) {
        return unusedAccessTokenRepo.findByToken(token);
    } 
    public void saveNewRefreshToken(String token) {
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setExpireAt(extractExpiredDate(token));
        newRefreshToken.setToken(token);
        refreshTokenRepo.save(newRefreshToken);
    } 
    public void saveUnusedAccessToken(String token) {
        UnusedAccessToken unusedAccessToken = new UnusedAccessToken();
        unusedAccessToken.setExpireAt(extractExpiredDate(token));
        unusedAccessToken.setToken(token);
        unusedAccessTokenRepo.save(unusedAccessToken);
    }
    public void deleteRefreshToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }
    public List<UnusedAccessToken> getAllAccessTokenExpired() {
        

        return unusedAccessTokenRepo.findByExpireAtBefore(new Date());
    }
    public void deleteAllExpiredToken() {
        List<UnusedAccessToken> listTokens = getAllAccessTokenExpired();
        unusedAccessTokenRepo.deleteAll(listTokens);
    }
    public List<RefreshToken> getAllRefreshTokenExpired() {
        return refreshTokenRepo.findByExpireAtBefore(new Date());
    }
    public void deleteAllExpiredRefreshToken() {
        List<RefreshToken> listTokens = getAllRefreshTokenExpired();
        refreshTokenRepo.deleteAll(listTokens);
    }
}
