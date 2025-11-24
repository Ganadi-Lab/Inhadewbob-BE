package GanadiLab.inhadewbob.domain.diet.service;

import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;
import GanadiLab.inhadewbob.domain.diet.dto.response.LatestDietLogResponse;
import GanadiLab.inhadewbob.domain.diet.dto.response.WeeklyDietLogResponse;

import java.util.List;
import java.time.LocalDate;

public interface DietLogService {

    /* 식단 기록 생성(먹은 메뉴 기록) */
    DietLogResponse create(DietLogCreateRequest request);

    /* 특정 유저의 식단 기록 조회 */
    List<DietLogResponse> getByMember(Long memberId);

    /* 특정 유저의 일별 식단 기록 조회 */
    List<DietLogResponse> getDaily(Long memberId, LocalDate date);

    /* 특정 유저의 주별 식단 기록 조회(각 날짜에 외식을 몇번 했는지) */
    List<WeeklyDietLogResponse> getWeekly(LocalDate start, LocalDate end, Long memberId);

    /* 최근 식사 5개 조회 */
    List<LatestDietLogResponse> getLatest(Long memberId);
}
