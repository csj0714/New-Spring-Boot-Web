package com.example.test.date;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import com.example.test.common.Constant;
import com.example.test.common.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.test.user.UserDTO;
import com.example.test.user.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DateController {

    @Autowired
    private DateService dateService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private HttpSession session;

    @GetMapping("/date")
    public String date(Model model) {
        log.info("/Date...");

        String username = (String) session.getAttribute("username");
        String gender = (String) session.getAttribute("gender");
        log.info("username:{}, gender:{}", username, gender);
        List<UserDTO> vos = dateService.selectRandomTwo(username, gender);
        log.info("vos :{}", vos.toString());

        model.addAttribute("vos", vos);


        return "thymeleaf/date/select";
    }

    @GetMapping("/date/register")
    public String register(UserDTO vo, Model model) {
        log.info("/register...");

        UserDTO vo2 = userService.selectOne(vo);

        model.addAttribute("vo2", vo2);

        return "thymeleaf/date/register";
    }

    @PostMapping("/date/registerOK")
    public String registerOK(UserDTO vo, Model model) {
        log.info("/registerOK...");

        log.info("vo:{}", vo.toString());
        UserDTO vo2 = userService.selectOne(vo);
        log.info("vo2 :{}", vo2.toString());
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");
        int num = (Integer) session.getAttribute("num");

        log.info("username:{}", username);
        log.info("name: {}", name);


        DateDTO dto = new DateDTO();
        dto.setApplicantNickname(username);
        dto.setApplicantRealName(name);
        dto.setApplicantNum(num);
        dto.setReceiverNickname(vo2.getUsername());
        dto.setReceiverRealName(vo2.getRealname());
        dto.setReceiverNum(vo2.getNum());
        dto.setAccept("waiting...");
        log.info(dto.toString());
        String msg;
        DateDTO Date = dateService.selectByReceiverAndApplicant(vo2.getNum(), num);
        if (Date == null) {
            DateDTO registeredDate = dateService.registerDate(dto);

            /*
             * 데이트 요청을 받은 사람에게 데이트 요청알림 메시지 전송
             */
            messageSender.send(vo2.getTel(), Constant.DATE_REQUEST_MESSAGE_TEXT);

            msg = "미팅 신청이 완료되었습니다.";
            log.info(msg);
            session.setAttribute("msg", msg);
            return "redirect:/";
        } else {
            msg = "이미 신청된 미팅입니다.";
            session.setAttribute("msg", msg);
            return "redirect:/date";
        }
    }

    @GetMapping("/date/selectAll")
    public String DateAll(Model model) {
        log.info("/Date/selectAll...");

        List<DateDTO> vos = dateService.selectAll();

        model.addAttribute("vos", vos);

        return "thymeleaf/date/selectAll";
    }

    @GetMapping("/date/selectOne")
    public String dateOne(Model model) {
        log.info("/Date/selectOne...");

        int num = (Integer) session.getAttribute("num");
        log.info("num:{}", num);

        List<DateDTO> vos = dateService.selectOne(num);

        model.addAttribute("vos", vos);

        return "thymeleaf/date/selectOne";
    }

    /**
     * 나에게 신청한 데이트 목록 조회
     *
     * @return : 데이트 목록
     */
    @GetMapping("/date/dateReceiveOne") //신청받은 소개팅
    public String dateReceiveOne(Model model) {

        int num = (Integer) session.getAttribute("num");
        log.info("num:{}", num);

        List<DateDTO> vos = dateService.selectOneReceiver(num);

        model.addAttribute("vos", vos);


        return "thymeleaf/date/dateReceiveOne";
    }

    @GetMapping("/date/delete")
    public String delete(DateDTO vo, Model model) {
        log.info("/Date/delete");
        log.info("vo:{}", vo.getApplicantNum());
        log.info("vo:{}", vo.getReceiverNum());

        DateDTO Date = dateService.selectByReceiverAndApplicant(vo.getReceiverNum(), vo.getApplicantNum());
        log.info("vo2:{}", Date.toString());

        model.addAttribute("vo2", Date);

        return "thymeleaf/date/delete";
    }

    @PostMapping("/date/deleteOK")
    public String deleteOK(@RequestParam("num") int num, Model model) {
        log.info("/Date/deleteOK");
        log.info("vo:{}", num);

        int result = dateService.deleteByNum(num);
        log.info("result :{}", result);
        if (result == 1) {
            return "redirect:/date/selectOne";
        } else {
            return "redirect:/date/delete";
        }
    }

    @PostMapping("/date/accept")
    public String allow(@RequestParam("applicantNum") int applicantNum, @RequestParam("receiverNum") int receiverNum, @RequestParam("accept") String accept, Model model) {
        log.info("/Date/accept");
        log.info("vo:{}", receiverNum);

        int result = dateService.updateAcceptField(accept, receiverNum, applicantNum);

        if (result == 1) {

            /**
             * 데이트 신청자에게 수락 메시지 전송
             */
            final UserDTO applicant = userService.findByNum(applicantNum);
            messageSender.send(applicant.getTel(), Constant.DATE_ACCEPT_MESSAGE_TEXT);
            return "redirect:/date/selectOne";
        } else {
            return "redirect:/date";
        }
    }

}