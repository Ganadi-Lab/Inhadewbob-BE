package GanadiLab.inhadewbob.global.newAuth.token.dto.request;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
}