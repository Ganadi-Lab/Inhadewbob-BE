package GanadiLab.inhadewbob.domain.member.model;

import GanadiLab.inhadewbob.global.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    String nickname;

    @Column(nullable = false)
    private String provider = "local"; // 기본값 지정

    @Column(nullable = false, unique = true)
    private String providerId = "";      // 구글 sub

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "weekly_budget", nullable = true)
    Integer weeklyBudget=0;

    @Column(name = "eatout_count", nullable = false)
    Integer eatoutCount=0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Mode mode = Mode.EASY;
}
