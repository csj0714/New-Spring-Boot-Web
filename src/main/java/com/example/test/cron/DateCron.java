package com.example.test.cron;

import com.example.test.date.DateDTO;
import com.example.test.date.DateRepository;
import com.example.test.dateideal.DateIdeal;
import com.example.test.dateideal.DateIdealRepository;
import com.example.test.user.UserDTO;
import com.example.test.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DateCron {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DateRepository dateRepository;
    @Autowired
    private DateIdealRepository dateIdealRepository;

    /**
     * 매일 n시에 소개팅을 매칭하는 Cron <br/>
     * - 필수 조건 : 직업 & 지역 <br/>
     * - 선택 조건 (2개 이상) : 학과, 군대, 흡연여부, 키, 체형
     */
    @Scheduled(cron = "${cron.date.matching.expression}", zone = "${cron.date.matching.zone}")
    public void dailyDateMatching() {

        // 모든 사용자 조회
        final List<UserDTO> userList = userRepository.findAll();

        userList.forEach(user -> {
            final AtomicInteger count = new AtomicInteger(0);

            // 현재 유저의 이상형 조회
            final DateIdeal dateIdeal = dateIdealRepository.findByUser(user).orElseThrow();

            // 필수 조건을 만족하는 유저 목록
            final List<UserDTO> dateUsers = userList.stream()
                    .filter(otherUser -> otherUser.getNum() != user.getNum())                 // 자기 자신 제외
                    .filter(otherUser -> otherUser.getJob().equals(dateIdeal.getJob()))       // 직업이 일치하는 사람
                    .filter(otherUser -> otherUser.getRegion().equals(dateIdeal.getRegion())) // 지역이 일치하는 사람
                    .toList();

            final List<UserDTO> dateMatchingTargets = new ArrayList<>();

            // 최대 2명의 매칭 생성
            if (count.get() < 3) {
                dateUsers.forEach(dateUser -> {
                    // 조건이 2개 이상 일치하고, 서로 데이트를 신청한 적이 없었을 경우만 해당
                    if (isMatchCondition(dateIdeal, dateUser)
                            && dateRepository.existsByApplicantNumAndReceiverNum(user.getNum(), dateUser.getNum())
                            && dateRepository.existsByApplicantNumAndReceiverNum(dateUser.getNum(), user.getNum())) {
                        dateMatchingTargets.add(dateUser);
                        count.getAndIncrement();
                    }
                });
            }

            // 데이트 매칭 정보 생성
            dateMatchingTargets.forEach(dateUser -> dateRepository.save(createDate(user, dateUser)));
        });
    }

    private boolean isMatchCondition(DateIdeal dateIdeal, UserDTO dateUser) {
        int count = 0;

        if (dateIdeal.getDepartment().equals(dateUser.getDepartment())) {
            count++;
        }
        if (dateIdeal.getMilitary().equals(dateUser.getMilitary())) {
            count++;
        }
        if (dateIdeal.getTall().equals(dateUser.getTall())) {
            count++;
        }
        if (dateIdeal.getForm().equals(dateUser.getForm())) {
            count++;
        }

        return count >= 2;
    }

    @NotNull
    private DateDTO createDate(UserDTO user, UserDTO dateUser) {
        log.info("user[{}], dateUser[{}]", user, dateUser);
        final DateDTO date = new DateDTO();
        date.setApplicantNickname(user.getNickname());
        date.setApplicantNum(user.getNum());
        date.setApplicantRealName(user.getRealname());

        date.setReceiverNickname(dateUser.getNickname());
        date.setReceiverNum(dateUser.getNum());
        date.setReceiverRealName(dateUser.getRealname());

        date.setAccept("waiting...");
        return date;
    }
}
