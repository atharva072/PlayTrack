package com.project.playtrack.DTO;

public class LoginResponseDTO {
    private String message;

    public LoginResponseDTO (String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
