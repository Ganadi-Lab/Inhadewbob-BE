package GanadiLab.inhadewbob.global.newAuth.token.dto.response;

import GanadiLab.inhadewbob.domain.member.model.Member;

public record ProfileResponse(
        Long id,
        String email,
        String nickname,
        Integer eatoutCount,
        Integer weeklyBudget
) {
    public static ProfileResponse from(Member member) {
        return new ProfileResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getEatoutCount(),
                member.getWeeklyBudget()
        );
    }
}
