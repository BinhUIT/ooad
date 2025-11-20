package com.example.ooad.service.auth.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.RefreshToken;
import com.example.ooad.domain.entity.UnusedAccessToken;

public interface JwtService {
    public String generateToken(String email);
    public String generateRefreshToken(String email);
    public String extractTenDangNhap(String token);
    public Date extractExpiredDate(String token);
    public Date extractIssueDate(String token);
    public boolean validateToken(String token,UserDetails userDetails);
    public boolean validateRefreshToken(String token , UserDetails userDetails);
    public Account findByToken(String token);
    public RefreshToken getRefreshTokenByToken(String token);
    public UnusedAccessToken getUnusedAccessTokenByToken(String token);
    public void saveNewRefreshToken(String token);
    public void saveUnusedAccessToken(String token);
    public void deleteRefreshToken(String token);
    public List<UnusedAccessToken> getAllAccessTokenExpired() ;
    public void deleteAllExpiredToken();
    public List<RefreshToken> getAllRefreshTokenExpired();
    public void deleteAllExpiredRefreshToken();
}
