package com.example.test.cron;

import com.example.test.date.DateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DateCron {

    @Autowired
    private DateRepository dateRepository;

    /**
     * 매일 n시에 소개팅을 매칭하는 Cron <br/>
     * - 필수 조건 : 직업 & 지역 <br/>
     * - 선택 조건 (2개 이상) : 학과, 군대, 흡연여부, 키, 체형
     */
    @Scheduled(cron = "${cron.date.matching.expression}", zone = "${cron.date.matching.zone}")
    public void dailyDateMatching() {


    }
}
