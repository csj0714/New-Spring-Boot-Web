package com.example.test.dateideal;

import com.example.test.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * 데이트 상대 매칭용 이상형
 */
@Data
@Entity
@Table(name = "date_ideal")
public class DateIdeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num")
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDTO user;

    @NotBlank
    @Column(name = "job")
    private String job; // 직업 - 필수 조건

    @NotBlank
    @Column(name = "region")
    private String region; // 지역 - 필수 조건

    @NotBlank
    @Column(name = "department")
    private String department; // 학과

    @NotBlank
    @Column(name = "military")
    private String military; // 군대

    @NotBlank
    @Column(name = "tall")
    private String tall; // 키

    @NotBlank
    @Column(name = "form")
    private String form; // 체형

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regdate", updatable = false)
    private Date regdate = new Date();

    public void update(DateIdeal request) {
        this.job = request.job;
        this.region = request.region;
        this.department = request.department;
        this.military = request.military;
        this.tall = request.tall;
        this.form = request.form;
    }
}
