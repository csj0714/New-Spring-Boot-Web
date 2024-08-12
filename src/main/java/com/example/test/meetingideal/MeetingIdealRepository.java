package com.example.test.meetingideal;

import com.example.test.dateideal.DateIdeal;
import com.example.test.user.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingIdealRepository extends JpaRepository<MeetingIdeal, Long> {
    Optional<MeetingIdeal> findByUser(final UserDTO userDTO);
}
