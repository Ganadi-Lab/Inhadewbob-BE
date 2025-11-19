package GanadiLab.inhadewbob.domain.diet.service;

import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;
import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import GanadiLab.inhadewbob.domain.diet.repository.DietLogRepository;
import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.domain.menu.model.Menu;
import GanadiLab.inhadewbob.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietLogService {
    private final DietLogRepository dietLogRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    public DietLogResponse create(DietLogCreateRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));

        DietLog dietLog = dietLogRepository.save(
                DietLog.builder()
                        .member(member)
                        .menu(menu)
                        .build()
        );

        return toResponse(dietLog);
    }

    public List<DietLogResponse> getByMember(Long memberId) {
        return dietLogRepository.findByMemberId(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DietLogResponse toResponse(DietLog diet) {
        return DietLogResponse.builder()
                .id(diet.getId())
                .memberId(diet.getMember().getId())
                .menuId(diet.getMenu().getId())
                .menuName(diet.getMenu().getName())
                .price(diet.getMenu().getPrice())
                .restaurantName(diet.getMenu().getRestaurant().getName())
                .createdAt(diet.getCreatedAt().toString())
                .build();
    }
}