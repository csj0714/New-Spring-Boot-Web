package com.example.test.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.test.common.AES128Converter;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import com.example.test.meeting.MeetingDTO;

import lombok.Data;

//@Entity : 자바의 객체와 DB테이블을 매칭시켜주는 기능
//@Table : DB에 테이블을 정의 해준다.

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
@ToString
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num")//컬럼이름 설정
    private int num;

    @Column(name = "realname", nullable = false)
    @Convert(converter = AES128Converter.class)
    private String realname;

    @Column(name = "username", nullable = false)
    @Convert(converter = AES128Converter.class)
    private String username;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name = "kakaoID", nullable = true)
    @Convert(converter = AES128Converter.class)
    private String kakaoID;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "school", nullable = true)
    private String school;

    @Column(name = "tel", nullable = true)
    @Convert(converter = AES128Converter.class)
    private String tel;

    @Column(name = "introduce", nullable = true)
    private String introduce;

    @Column(name = "form", nullable = true)
    private String form;

    @Column(name = "save_name", nullable = true)
    private String save_name;

    @Column(name = "file_path", nullable = true)
    private String file_path;

    @Column(name = "job", nullable = true)
    private String job;

    @Column(name = "region", nullable = true)
    private String region;

    @Column(name = "military", nullable = true)
    private String military;

    @Column(name = "tall", nullable = true)
    private String tall;

    @Column(name = "department", nullable = true)
    private String department;

    @Column(name = "smoking", nullable = true)
    private String smoking;

    @Column(name="major",nullable = true)
    private String major;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regdate", updatable = false)
    private Date regdate;

    @ToString.Exclude
    @OneToMany
    private List<MeetingDTO> meetingDTOList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        regdate = new Date();
    }
}
