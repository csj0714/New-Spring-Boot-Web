package com.example.test.meetingideal;


import com.example.test.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 데이트 상대 매칭용 이상형
 */
@Data
@Entity
@Table(name = "meeting_ideal")
public class MeetingIdeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num")
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDTO user;

    @NotBlank
    @Column(name = "region")
    private String region; // 밋팅 지역 - 필수 조건

    @NotNull
    @Column(name = "average_age")
    private Integer averageAge; // 평균 나이

    @NotBlank
    @Column(name = "job")
    private String job; // 직업

    @NotBlank
    @Column(name = "mood")
    private String mood; // 분위기

    @NotBlank
    @Column(name = "department")
    private String department; // 학과

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regdate", updatable = false)
    private Date regdate = new Date();

    public void update(MeetingIdeal request) {
        this.job = request.job;
        this.averageAge = request.averageAge;
        this.region = request.region;
        this.department = request.department;
        this.mood = request.mood;
    }
}

