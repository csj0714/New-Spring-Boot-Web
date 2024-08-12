package com.example.test.meetingideal;

import com.example.test.dateideal.DateIdeal;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting/ideal")
public class MeetingIdealController {

    private final HttpSession session;
    private final MeetingIdealService meetingIdealService;

    /**
     * 미팅 이상형 조회
     *
     * @return : 데이트 이상형
     */
    @GetMapping
    public ResponseEntity<MeetingIdeal> getMeetingIdeal() {
        final int num = (int) session.getAttribute("num");
        return ResponseEntity.ok(meetingIdealService.findByNum(num));
    }

    /**
     * 미팅 이상형 저장
     *
     * @param request : 이상형 정보
     * @return : 미팅 이상형
     */
    @PostMapping
    public ResponseEntity<MeetingIdeal> saveMeetingIdeal(@RequestBody @Valid final MeetingIdeal request) {
        log.info("request[{}]", request);

        final int num = (int) session.getAttribute("num");
        return ResponseEntity.ok(meetingIdealService.save(num, request));
    }

    /**
     * 미팅 이상형 수정
     *
     * @param request : 수정할 정보
     * @return : 미팅 이상형
     */
    @PatchMapping
    public ResponseEntity<MeetingIdeal> updateMeetingIdeal(@RequestBody @Valid final MeetingIdeal request) {
        log.info("request[{}]", request);

        final int num = (int) session.getAttribute("num");
        return ResponseEntity.ok(meetingIdealService.update(num, request));
    }
}
