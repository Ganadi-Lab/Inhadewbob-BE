package GanadiLab.inhadewbob.domain.diet.model;

import GanadiLab.inhadewbob.domain.manualDietDetail.model.ManualDietDetail;
import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.menu.model.Menu;
import GanadiLab.inhadewbob.global.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diet_log")
public class DietLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @OneToOne(mappedBy = "dietLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ManualDietDetail manualDietDetail;
}
