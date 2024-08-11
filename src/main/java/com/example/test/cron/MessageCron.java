package com.example.test.cron;

import com.example.test.common.Constant;
import com.example.test.common.MessageSender;
import com.example.test.user.UserDTO;
import com.example.test.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageCron {

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private UserRepository userRepository;

    /**
     * 매일 n시에 모든 유저에게 새로운 매칭 존재 메시지를 전송하는 Cron
     */
    @Scheduled(cron = "${cron.message.matching.expression}", zone = "${cron.message.matching.zone}")
    public void sendMatchingSuccessMessage() {
        final List<UserDTO> userList = userRepository.findAll();
        userList.forEach(user -> {
            if (user.getTel() != null) {
                messageSender.send(user.getTel(), Constant.DATE_WAITING_MESSAGE_TEXT);
            }
        });
    }
}
