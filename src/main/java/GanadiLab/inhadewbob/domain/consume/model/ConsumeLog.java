package GanadiLab.inhadewbob.domain.consume.model;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.global.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consume_log")
public class ConsumeLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder.Default
    @Column(nullable = false)
    private Integer spentAmount = 0;       // 오늘 쓴 금액

    @Column(nullable = false)
    private Integer remainingBudget;   // 남은 예산

    @Column(nullable = false)
    private String date;               // YYYY-MM-DD
}
