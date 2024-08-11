package com.example.test.common;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageSender {

    @Value("${sms.api-key}")
    private String SMS_API_KEY;

    @Value("${sms.secret}")
    private String SMS_SECRET;

    @Value("${sms.url}")
    private String SMS_URL;

    @Value("${sms.sender}")
    private String SMS_SENDER;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        messageService = NurigoApp.INSTANCE.initialize(SMS_API_KEY, SMS_SECRET, SMS_URL);
    }

    /**
     * 메시지 전송 메서드
     *
     * @param recipient : 수신자. 하이픈(-) 없이 숫자만 입력
     * @param text      : 메시지 내용. 한글 45자, 영자 90자까지 입력 가능
     */
    public void send(final String recipient, final String text) {
        log.info("recipient[{}], text[{}]", recipient, text);

        String phoneNumber;
        if (recipient.contains("-")) {
            // 휴대폰 번호에 "-"가 포함된 사용자의 경우, 하이픈 제거
            phoneNumber = recipient.replace("-", "");
        } else {
            phoneNumber = recipient;
        }

        final Message message = new Message();
        message.setFrom(SMS_SENDER);
        message.setTo(phoneNumber);
        message.setText(text);

        try {
            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            log.error("message send fail to[{}]", recipient, exception);
            log.error("failedMessageList[{}]", exception.getFailedMessageList());
        } catch (Exception exception) {
            log.error("message send fail to[{}]", recipient, exception);
        }
    }
}
