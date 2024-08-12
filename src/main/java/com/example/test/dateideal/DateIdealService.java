package com.example.test.dateideal;

import com.example.test.user.UserDTO;
import com.example.test.user.UserRepository;
import com.example.test.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DateIdealService {

    private final UserRepository userRepository;
    private final DateIdealRepository dateIdealRepository;

    /**
     * 데이트 이상형 저장
     *
     * @param num       : 유저 PK
     * @param dateIdeal : 이상형 정보
     * @return : 데이트 이상형
     */
    @Transactional
    public DateIdeal save(final int num, final DateIdeal dateIdeal) {
        log.debug("dateIdeal[{}]", dateIdeal);

        final UserDTO user = userRepository.findByNum(num);
        log.debug("user[{}]", user);

        dateIdeal.setUser(user);
        return dateIdealRepository.save(dateIdeal);
    }

    /**
     * 데이트 이상형 수정
     *
     * @param num     : 유저 PK
     * @param request : 수정할 정보
     * @return : 데이트 이상형
     */
    @Transactional
    public DateIdeal update(int num, DateIdeal request) {

        log.debug("request[{}]", request);

        final UserDTO user = userRepository.findByNum(num);
        log.debug("user[{}]", user);

        final DateIdeal dateIdeal = dateIdealRepository.findByUser(user).orElseThrow();
        dateIdeal.update(request);

        return dateIdealRepository.save(dateIdeal);
    }
}
