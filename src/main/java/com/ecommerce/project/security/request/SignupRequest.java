package com.ecommerce.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @Email
    private String email;
    private Set<String> roles;
}
