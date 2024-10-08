package com.example.test.date;

import java.time.LocalDate;
import java.util.Date;

import com.example.test.meeting.MeetingDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="date")
public class DateDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="num")//컬럼이름 설정
	private int num;
	
	@Column(name="applicantNum",nullable = false)
	private int applicantNum;
	
	@Column(name="applicantRealName",nullable = false)
	private String applicantRealName;
	
	@Column(name="applicantNickname",nullable = false)
	private String applicantNickname;
	
	@Column(name="receiverNum",nullable = false)
	private int receiverNum;
	
	@Column(name="receiverRealName",nullable = false)
	private String receiverRealName;
	
	@Column(name="receiverNickname",nullable = false)
	private String receiverNickname;

	@Column(name="accept",nullable = false)
	private String accept;

	@Column(name = "regdate", updatable = false)
	private LocalDate regdate;

	@PrePersist
	protected void onCreate() {
		regdate = LocalDate.now();
	}

}
