package com.example.test.user;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private HttpSession session;

    public UserDTO insertOK(UserDTO vo) {

        /**
         * 비밀번호 단방향 암호화
         */
        vo.setPw(BCrypt.hashpw(vo.getPw(), BCrypt.gensalt()));
        return userRepo.save(vo);
    }

    public UserDTO selectOne(UserDTO vo) {
        return userRepo.findByNum(vo.getNum());
    }

    @Transactional(readOnly = true)
    public UserDTO findByNum(Integer num) {
        return userRepo.findByNum(num);
    }

    public UserDTO updateOK(UserDTO vo) {
        UserDTO existingUser = userRepo.findByUsername(vo.getUsername());
        if (existingUser != null) {
            // existingUser가 null이 아닌 경우에만 업데이트를 수행
            existingUser.setRealname(vo.getRealname());
            existingUser.setPw(vo.getPw());
            existingUser.setNickname(vo.getNickname());
            existingUser.setTel(vo.getTel());
            existingUser.setGender(vo.getGender());
            existingUser.setKakaoID(vo.getKakaoID());
            existingUser.setSchool(vo.getSchool());
            existingUser.setTel(vo.getTel());
            existingUser.setAge(vo.getAge());
            existingUser.setSave_name(vo.getSave_name());
            existingUser.setFile_path(vo.getFile_path());
            existingUser.setForm(vo.getForm());
            existingUser.setIntroduce(vo.getIntroduce());
            existingUser.setJob(vo.getJob());
            existingUser.setMilitary(vo.getMilitary());
            existingUser.setRegion(vo.getRegion());
            existingUser.setTall(vo.getTall());
            existingUser.setSmoking(vo.getSmoking());
            existingUser.setDepartment(vo.getDepartment());

            updateSession(existingUser);
            return userRepo.save(existingUser);
        } else {
            // existingUser가 null인 경우에 대한 처리
            log.error("User with username {} not found", vo.getUsername());
            // 예외 처리 또는 새로운 유저 생성 등을 여기에 추가할 수 있습니다.
            return null; // 예시로 null을 반환하도록 하였습니다. 실제로는 예외 처리 등을 하셔야 합니다.
        }
    }

    private void updateSession(UserDTO user) {
        // 세션에 사용자 정보 업데이트
        session.setAttribute("username", user.getUsername());
        session.setAttribute("pw", user.getPw());
        session.setAttribute("nickname", user.getNickname());
        session.setAttribute("gender", user.getGender());
        session.setAttribute("kakaoID", user.getKakaoID());
        session.setAttribute("age", user.getAge());
        session.setAttribute("name", user.getRealname());
        session.setAttribute("school", user.getSchool());
        session.setAttribute("tel", user.getTel());
        session.setAttribute("savename", user.getSave_name());
        session.setAttribute("form", user.getForm());
        session.setAttribute("introduce", user.getIntroduce());
        session.setAttribute("department", user.getDepartment());
        session.setAttribute("job", user.getJob());
        session.setAttribute("military", user.getMilitary());
        session.setAttribute("region", user.getRegion());
        session.setAttribute("smoking", user.getSmoking());
        session.setAttribute("tall", user.getTall());
    }

    public int deleteOK(String username) {
        return userRepo.deleteByUsername(username);
    }

    public UserDTO loginOK(UserDTO vo) {
        UserDTO user = userRepo.findByUsername(vo.getUsername());

        /**
         * 비밀번호 암호화 매칭 확인
         */
        if (BCrypt.checkpw(vo.getPw(), user.getPw())) {
            return user;
        } else {
            return null;
        }
    }
}
