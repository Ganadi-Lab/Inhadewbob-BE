package GanadiLab.inhadewbob.domain.manualDietDetail.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "manual_diet_detail")
public class ManualDietDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false)
    private DietLog dietLog;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;
}
