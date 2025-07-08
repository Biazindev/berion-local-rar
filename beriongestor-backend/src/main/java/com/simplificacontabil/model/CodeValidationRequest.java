package com.simplificacontabil.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeValidationRequest {
    private String email;
    private String code;
    private String password;
    private String ip;
    private String UserAgent;
    private String username;
}
