package GanadiLab.inhadewbob.global.newAuth.token.dto.response;

public record SignupResponse(
        Long memberId,
        String email,
        String nickname
) {}
