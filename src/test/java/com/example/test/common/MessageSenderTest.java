package com.example.test.common;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageSenderTest {

    @Autowired
    private MessageSender messageSender;

    @Test
    @Disabled
    @DisplayName("SMS 메시지가 정상적으로 전송되어야 한다.")
    void messageSend() {

        // given
        final String receipt = "01011112222";
        final String text = "Test Message";

        // when
        messageSender.sendSMS(receipt, text);

        // then
    }

}