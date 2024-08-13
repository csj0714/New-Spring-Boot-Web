package com.example.test.cron;

import com.example.test.dateideal.DateIdeal;
import com.example.test.meeting.MeetingDTO;
import com.example.test.meeting.MeetingRepository;
import com.example.test.meetingideal.MeetingIdeal;
import com.example.test.meetingideal.MeetingIdealRepository;
import com.example.test.user.UserDTO;
import com.example.test.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class MeetingCron {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private MeetingIdealRepository meetingIdealRepository;

    /**
     * 매일 n시에 소개팅을 매칭하는 Cron <br/>
     * - 필수 조건 : 직업 & 지역 <br/>
     * - 선택 조건 (2개 이상) : 학과, 군대, 흡연여부, 키, 체형
     */
    @Scheduled(cron = "${cron.meeting.matching.expression}", zone = "${cron.meeting.matching.zone}")
    public void dailyMeetingMatching() {
        log.info("======daily meeting matching start=======");

        // 모든 사용자 조회
        final List<UserDTO> userList = userRepository.findAll();

        // 모든 미팅 조회
        final List<MeetingDTO> allMeeting = meetingRepository.findAll();

        userList.forEach(user -> {
            log.info("user[{}]", user);
            final AtomicInteger count = new AtomicInteger(0);

            // 현재 유저의 이상형 조회
            final MeetingIdeal ideal = meetingIdealRepository.findByUser(user).orElseThrow();
            log.info("ideal[{}]", ideal);

            // 필수 조건을 만족하는 미팅 목록
            final List<MeetingDTO> meetingList = allMeeting.stream()
                    .filter(meeting -> meeting.getRegion().equals(ideal.getRegion()))
                    .toList();
            log.info("meetingList[{}]", meetingList);

            final List<MeetingDTO> meetingMatchingTargets = new ArrayList<>();

            // 최대 2명의 매칭 생성
            if (count.get() < 3) {
                meetingList.forEach(meeting -> {
                    if (isMatchCondition(ideal, meeting)) {
                        meetingMatchingTargets.add(meeting);
                        count.getAndIncrement();
                    }
                });
            }

            // 미팅 매칭 정보 생성
            meetingMatchingTargets.forEach(meetingDTO -> {
                user.getMeetingDTOList().add(meetingDTO);
                userRepository.save(user);
            });
        });
        log.info("======daily meeting matching end=======");
    }

    private boolean isMatchCondition(MeetingIdeal ideal, MeetingDTO meeting) {
        int count = 0;

        if (ideal.getAverageAge() == meeting.getAverage()) {
            count++;
        }
        if (ideal.getJob().equals(meeting.getJob())) {
            count++;
        }
        if (ideal.getMood().equals(meeting.getMood())) {
            count++;
        }
        if (ideal.getDepartment().equals(meeting.getDepartment())) {
            count++;
        }

        return count >= 2;
    }

}
