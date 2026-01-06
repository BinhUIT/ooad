package com.example.ooad.dto.request;

import com.example.ooad.domain.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateActorAccountDto extends CreateAccountDto {
    private int actorId;
    public CreateActorAccountDto(int actorId, String username, ERole role, String password) {
        this.actorId=actorId;
        this.role=role;
        this.username = username;
        this.password = password;
    }
}
