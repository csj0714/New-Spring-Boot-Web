package com.example.test.dateideal;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/date/ideal")
@RequiredArgsConstructor
public class DateIdealController {

    private final HttpSession session;
    private final DateIdealService dateIdealService;

    /**
     * 데이트 이상형 조회
     *
     * @return : 데이트 이상형
     */
    @GetMapping
    public ResponseEntity<DateIdeal> getDateIdeal() {
        final int num = (int) session.getAttribute("num");
        return ResponseEntity.ok(dateIdealService.findByNum(num));
    }

    /**
     * 데이트 이상형 저장
     *
     * @param dateIdeal : 이상형 정보
     * @return : 데이트 이상형
     */
    @PostMapping
    public ResponseEntity<DateIdeal> saveDateIdeal(@RequestBody @Valid final DateIdeal dateIdeal) {
        log.info("dateIdeal[{}]", dateIdeal);

        final int num = (int) session.getAttribute("num");
        return ResponseEntity.ok(dateIdealService.save(num, dateIdeal));
    }

    /**
     * 데이트 이상형 수정
     *
     * @param dateIdeal : 수정할 정보
     * @return : 데이트 이상형
     */
    @PatchMapping
    public ResponseEntity<DateIdeal> updateDateIdeal(@RequestBody @Valid final DateIdeal dateIdeal) {
        log.info("dateIdeal[{}]", dateIdeal);

        final int num = (int) session.getAttribute("num");
        return ResponseEntity.ok(dateIdealService.update(num, dateIdeal));
    }
}