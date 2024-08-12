package com.example.test.meetingideal;

import com.example.test.dateideal.DateIdeal;
import com.example.test.dateideal.DateIdealRepository;
import com.example.test.user.UserDTO;
import com.example.test.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingIdealService {

    private final MeetingIdealRepository meetingIdealRepository;

    private final UserRepository userRepository;

    /**
     * 미팅 이상형 저장
     *
     * @param num     : 유저 PK
     * @param request : 이상형 정보
     * @return : 미팅 이상형
     */
    @Transactional
    public MeetingIdeal save(final int num, final MeetingIdeal request) {
        log.debug("request[{}]", request);

        final UserDTO user = userRepository.findByNum(num);
        log.debug("user[{}]", user);

        request.setUser(user);
        return meetingIdealRepository.save(request);
    }

    /**
     * 미팅 이상형 수정
     *
     * @param num     : 유저 PK
     * @param request : 수정할 정보
     * @return : 미팅 이상형
     */
    @Transactional
    public MeetingIdeal update(int num, MeetingIdeal request) {
        log.debug("request[{}]", request);

        final UserDTO user = userRepository.findByNum(num);
        log.debug("user[{}]", user);

        final MeetingIdeal meetingIdeal = meetingIdealRepository.findByUser(user).orElseThrow();
        meetingIdeal.update(request);

        return meetingIdealRepository.save(meetingIdeal);
    }

    /**
     * 데이트 이상형 조회
     *
     * @return : 데이트 이상형
     */
    public MeetingIdeal findByNum(int num) {
        log.debug("num[{}]", num);

        final UserDTO user = userRepository.findByNum(num);
        log.debug("user[{}]", user);

        return meetingIdealRepository.findByUser(user).orElseThrow();
    }
}
