package com.example.test.date;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DateRepository extends JpaRepository<DateDTO, Object> {

    List<DateDTO> findByApplicantNum(int num);

    DateDTO findByReceiverNumAndApplicantNum(int receiverNum, int applicantNum);

    int deleteByReceiverNumAndApplicantNum(int receiverNum, int applicantNum);

    int deleteByNum(int num);

    DateDTO findByApplicantNumAndReceiverNum(int receiverNum, int applicantNum);

    List<DateDTO> findByReceiverNum(int num);

    /**
     * 나에게 신청한 데이트 목록 조회
     *
     * @param num : 자신의 user ID (Key)
     * @return : 데이트 목록
     */
    @Query("SELECT dt FROM DateDTO dt WHERE dt.receiverNum =:num")
    List<DateDTO> findByReceiverNumber(int num);

    /**
     * 이전에 존재하는 정보인지 조회
     *
     * @param applicantNum : 신청자 ID
     * @param receiverNum  : 수신자 ID
     * @return : 데이트 신청 여부
     */
    Boolean existsByApplicantNumAndReceiverNum(int applicantNum, int receiverNum);

    @Modifying
    @Transactional
    @Query("UPDATE DateDTO d SET d.accept = :accept WHERE d.receiverNum = :receiverNum AND d.applicantNum = :applicantNum")
    int updateAcceptField(String accept, int receiverNum, int applicantNum);
}
