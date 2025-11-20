package GanadiLab.inhadewbob.domain.diet.service;

import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;

import java.util.List;

public interface DietLogService {

    /* 식단 기록 생성(먹은 메뉴 기록) */
    DietLogResponse create(DietLogCreateRequest request);

    /* 특정 유저의 식단 기록 조회 */
    List<DietLogResponse> getByMember(Long memberId);
}
