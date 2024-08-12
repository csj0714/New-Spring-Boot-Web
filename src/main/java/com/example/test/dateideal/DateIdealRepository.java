package com.example.test.dateideal;

import com.example.test.user.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DateIdealRepository extends JpaRepository<DateIdeal, Long> {

    Optional<DateIdeal> findByUser(final UserDTO userDTO);
}
