package com.wealthdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Set<String> roles;

    public static AuthResponse of(String token, Long id, String username, String email, String fullName, Set<String> roles) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(id)
                .username(username)
                .email(email)
                .fullName(fullName)
                .roles(roles)
                .build();
    }
}