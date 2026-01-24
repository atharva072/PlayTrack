
package com.project.playtrack.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.playtrack.DTO.AttendanceDTO;
import com.project.playtrack.Entity.Attendance;
import com.project.playtrack.Entity.Player;
import com.project.playtrack.Entity.Team;
import com.project.playtrack.Repository.AttendanceRepository;
import com.project.playtrack.Repository.PlayerRepository;
import com.project.playtrack.Repository.TeamRepository;
import com.project.playtrack.Util.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    // ************************************************************************************************************************************************************************
    // 
    // ************************************************************************************************************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')") @Transactional
    public ApiResponse<AttendanceDTO> markAttendance(AttendanceDTO attendanceDTO) {
        // validations
        if (attendanceDTO.getDate() == null) {
            return new ApiResponse<>("error", "Please provide the date to mark attendance.", null);
        }
        
        Player player = playerRepository.findByUsername(attendanceDTO.getUsername());
        if (player == null) {
            return new ApiResponse<>("error", "Please provide username of the player.", null);
        }

        Team team = teamRepository.findById(attendanceDTO.getTeam()).orElse(null);
        if (team == null) {
            return new ApiResponse<>("error", "Please provide team.", null);
        }

        // if player is not part of the team -> return
        if (!team.getPlayers().contains(player)) {
            return new ApiResponse<>("error", "This player is not part of this team.", null);
        }

        // if attendance for that player on that date for that team is already marked -> no need to mark again
        Attendance existingAttendance = attendanceRepository.findByPlayer_UsernameAndPlayer_Team_NameAndDate(attendanceDTO.getUsername(), attendanceDTO.getTeam(), attendanceDTO.getDate());
        if (existingAttendance != null) {
            return new ApiResponse<>("error", "The attendance for this player is already marked on this day.", null);
        }

        Attendance attendance = new Attendance();
        attendance.setDate(attendanceDTO.getDate());
        attendance.setPresent(attendanceDTO.getPresent());
        attendance.setPlayer(player);
        attendance.setTeam(team);

        try {
            // save the attendance in the DB
            Attendance savedAttendance = attendanceRepository.save(attendance);

            if (savedAttendance != null && savedAttendance.getId() != null) {
                return new ApiResponse<>("success", "Attendance for " + savedAttendance.getPlayer().getFullName() + " marked as " + savedAttendance.getPresent() + " for date: " + savedAttendance.getDate(), null);
            } else {
                return new ApiResponse<>("error", "There was a problem in marking the attendance.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // ************************************************************************************************************************************************************************
    // 
    // ************************************************************************************************************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<List<AttendanceDTO>> getPlayerAttendanceBetween(String team, String username, LocalDate startDate, LocalDate endDate) {
        // validations

        // if the team name is empty,  return
        if (team == null || "".equals(team)) {
            return new ApiResponse<>("error", "Please provide the team name of the player.", null);
        }

        // if the username is empty,  return
        if (username == null || "".equals(username)) {
            return new ApiResponse<>("error", "Please provide the username of the player.", null);
        }

        // if start date and end dates are not provided, return
        if (startDate == null || endDate == null) {
            return new ApiResponse<>("error", "Please provide a range of dates.", null);
        }

        try {
            // get the attendadnce for player of a particular team in between dates
            List<Attendance> attendanceList = attendanceRepository.findByPlayer_UsernameAndPlayer_Team_NameAndDateBetween(username, team, startDate, endDate);

            // covert the attendace objects to attendance DTO for response to frontend
            List<AttendanceDTO> attendanceDTOList = new ArrayList<>();
            for (Attendance att : attendanceList) {
                AttendanceDTO attendanceDTO = convertToDTO(att);
                attendanceDTOList.add(attendanceDTO);
            }
            return new ApiResponse<>("success", "", attendanceDTOList);
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // ************************************************************************************************************************************************************************
    // 
    // ************************************************************************************************************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<AttendanceDTO> getAttendanceByUsernameTeamAndDate(@PathVariable String username, @PathVariable String team, @PathVariable LocalDate date) {

        // validations
        if (username == null || "".equals(username)) {
            return new ApiResponse<>("error", "Please provide username of the player.", null);
        }

        if (date == null) {
            return new ApiResponse<>("error", "Please provide date.", null);
        }

        try {

            // get attendance for a player of a team on specific date
            Attendance attendance = attendanceRepository.findByPlayer_UsernameAndPlayer_Team_NameAndDate(username, team, date);
            if (attendance == null) {
                return new ApiResponse<>("error", "Attendance for this player on " + date + " does not exist.", null);
            }
            AttendanceDTO returnedAttendanceDTO = convertToDTO(attendance);
            return new ApiResponse<>("success", "", returnedAttendanceDTO);
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // ************************************************************************************************************************************************************************
    // 
    // ************************************************************************************************************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<List<AttendanceDTO>> getAllTeamAttendanceOnDate(String team, LocalDate date) {
        // validations
        if (team == null || "".equals(team)) {
            return new ApiResponse<>("error", "Please provide team name.", null);
        }

        if (date == null) {
            return new ApiResponse<>("error", "Please provide a date.", null);
        }

        try {
            // get all attendance for a team on specific date
            List <Attendance> attendanceList = new ArrayList<>(attendanceRepository.findByPlayer_Team_NameAndDate(team, date));
            
            // convert the attendace objects to attendance DTO for response to frontend
            List<AttendanceDTO> attendanceDTOList = new ArrayList<>();
            for (Attendance att : attendanceList) {
                AttendanceDTO attendanceDTO = convertToDTO(att);
                attendanceDTOList.add(attendanceDTO);
            }
            return new ApiResponse<>("error", "", attendanceDTOList);
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // ************************************************************************************************************************************************************************
    // 
    // ************************************************************************************************************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')") @Transactional
    public ApiResponse<Attendance> removeAttendance(String team, String username, LocalDate date) {
        
        // CONTINUE HERE
        
        ApiResponse response = new ApiResponse<>(null, null, attendanceRepository);
        Optional<Attendance> removedAttendance = attendanceRepository.deleteByPlayerUsernameAndDate(username, date);
        if (removedAttendance != null && !removedAttendance.isEmpty()) {
            response.setMessage("Attendance removed for " + username + " on date " + date);
            response.setStatus("success");
            response.setData(removedAttendance);
        } else {
            response.setMessage("No Attendances found for removal");
            response.setStatus("success");
            response.setData(removedAttendance);
        }
        return response;
    }

    // ************************************************************************************************************************************************************************
    // 
    // ************************************************************************************************************************************************************************
    private AttendanceDTO convertToDTO (Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setDate(attendance.getDate());
        dto.setUsername(attendance.getPlayer().getUsername());
        dto.setTeam(attendance.getTeam().getName());
        dto.setPresent(attendance.getPresent());
        return dto;
    }
}