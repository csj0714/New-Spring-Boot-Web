package com.example.test.cron;

import com.example.test.date.DateDTO;
import com.example.test.date.DateRepository;
import com.example.test.user.UserDTO;
import com.example.test.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    /**
     * 매일 n시에 소개팅을 매칭하는 Cron <br/>
     * - 필수 조건 : 직업 & 지역 <br/>
     * - 선택 조건 (2개 이상) : 학과, 군대, 흡연여부, 키, 체형
     */
    @Scheduled(cron = "${cron.date.matching.expression}", zone = "${cron.date.matching.zone}")
    public void dailyDateMatching() {

        // TODO : 현재 직업-지역을 기반으로 필수 조건이 있어 다행이나, 나중에는 이상형?등을 저장하는 로직도 필요해보임.

        // 모든 사용자 조회
        final List<UserDTO> userList = userRepository.findAll();

        // 순서 무작위로 변경
        Collections.shuffle(userList);

        userList.forEach(user -> {
            final AtomicInteger count = new AtomicInteger(0);

            final List<UserDTO> dateUsers = userList.stream()
                    .filter(otherUser -> otherUser.getNum() != user.getNum())            // 자기 자신 제외
                    .filter(otherUser -> otherUser.getJob().equals(user.getJob()))       // 직업이 일치하는 사람
                    .filter(otherUser -> otherUser.getRegion().equals(user.getRegion())) // 지역이 일치하는 사람
                    .toList();

            dateUsers.forEach(dateUser -> {
                        if (count.get() < 3
                                && dateRepository.existsByApplicantNumAndReceiverNum(user.getNum(), dateUser.getNum())
                                && dateRepository.existsByApplicantNumAndReceiverNum(dateUser.getNum(), user.getNum())) {

                            // 서로 신청한 적이 없을 경우에만 최대 2건 데이트 매칭 생성
                            dateRepository.save(createDate(user, dateUser));
                            count.getAndIncrement();
                        }
                    }
            );
        });
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
