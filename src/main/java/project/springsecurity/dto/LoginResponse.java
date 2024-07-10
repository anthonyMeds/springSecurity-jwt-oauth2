package project.springsecurity.dto;

public record LoginResponse(String acessToken, Long expiresIn) {
}
