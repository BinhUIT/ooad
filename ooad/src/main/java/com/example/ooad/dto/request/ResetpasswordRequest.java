package com.example.ooad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResetpasswordRequest {
    private String newPassword;
    private String confirmNewPassword;
    private String verificationCode;
    private String email;
}
