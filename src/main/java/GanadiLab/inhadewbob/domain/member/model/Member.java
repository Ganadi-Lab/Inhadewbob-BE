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

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String nickname;

    @Column(name = "weekly_budget", nullable = true)
    Integer weeklyBudget;

    @Column(name = "eatout_count", nullable = false)
    Integer eatoutCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Mode mode;
}
