package GanadiLab.inhadewbob.domain.consume.service;

import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogCreateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogUpdateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeLogResponse;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeStatResponse;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeStatusResponse;

import java.time.LocalDate;

public interface ConsumeLogService {

    /* 소비 기록 등록 */
    ConsumeLogResponse createConsumeLog(ConsumeLogCreateRequest req);

    /* 소비 기록 수정 */
    ConsumeLogResponse updateConsumeLog(Long id, ConsumeLogUpdateRequest req);

    /* 이번 주 소비 현황 조회 */
    ConsumeStatusResponse getConsumeStatus(LocalDate today, Long memberId);

    /* 이번 주, 지난 주, 지지난 주 소비 통계 조회 */
    ConsumeStatResponse getConsumeStats(LocalDate today, Long memberId);
}
