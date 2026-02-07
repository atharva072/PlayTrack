package com.project.playtrack.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByDateAndPlayer_Username(LocalDate date, String username);

    // to find a players attendance in between two dates
    List<Attendance> findByPlayer_UsernameAndPlayer_Team_NameAndDateBetween(String username, String teamName, LocalDate startDate, LocalDate endDate);

    // find attendance of player of a team on a specific date
    Attendance findByPlayer_UsernameAndPlayer_Team_NameAndDate(String username, String teamName, LocalDate specificDate);

    // find all attendance for a team on a particular date
    List<Attendance> findByPlayer_Team_NameAndDate(String team, LocalDate date);

    // delete a team's player attendance on a particular date
    void deleteByPlayer_UsernameAndPlayer_Team_NameAndDate(String username, String teamName, LocalDate specificDate);

    Optional<Attendance> deleteByPlayerUsernameAndDate(String username, LocalDate date);
}
