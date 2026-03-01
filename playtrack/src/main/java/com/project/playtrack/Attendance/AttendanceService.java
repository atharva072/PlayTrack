
package com.project.playtrack.Attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.playtrack.Player.Player;
import com.project.playtrack.Player.PlayerRepository;
import com.project.playtrack.Team.Team;
import com.project.playtrack.Team.TeamRepository;
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

    // *****************************************************************************
    // Mark Player attendance
    // *****************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')") @Transactional
    public ApiResponse<AttendanceDTO> markAttendance(AttendanceDTO attendanceDTO) {
        // validations
        if (attendanceDTO.getDate() == null) {
            return new ApiResponse<>("ERROR", "Please provide the date to mark attendance.", null);
        }
        
        Player player = playerRepository.findByProfile_User_UserName(attendanceDTO.getUsername());
        if (player == null) {
            return new ApiResponse<>("ERROR", "Please provide username of the player.", null);
        }

        Team team = teamRepository.findByName(attendanceDTO.getTeam());
        if (team == null) {
            return new ApiResponse<>("ERROR", "Please provide team.", null);
        }

        // if player is not part of the team -> return
        if (!team.getPlayers().contains(player)) {
            return new ApiResponse<>("ERROR", "This player is not part of this team.", null);
        }

        // if attendance for that player on that date for that team is already marked -> no need to mark again
        boolean attendanceExists = attendanceRepository.existsByPlayer_Profile_User_UserNameAndPlayer_Team_NameAndDate(attendanceDTO.getUsername(), attendanceDTO.getTeam(), attendanceDTO.getDate());
        if (attendanceExists) {
            return new ApiResponse<>("ERROR", "The attendance for this player is already marked on this day.", null);
        }

        Attendance attendance = new Attendance();
        attendance.setDate(attendanceDTO.getDate());
        attendance.setPresent(attendanceDTO.getPresent());
        attendance.setPlayer(player);
        attendance.setTeam(team);

        try {
            // save the attendance in the DB
            Attendance savedAttendance = saveAttendance(attendance);
            if (savedAttendance == null) {
                return new ApiResponse<>("ERROR", "There was a problem in marking the attendance.", null);
            }
            return new ApiResponse("SUCCESS", "", convertToDTO(savedAttendance));
            
        } catch (Exception ex) {
            return new ApiResponse<>("ERROR", ex.getMessage(), null);
        }
    }

    // *****************************************************************************
    // Saves the attendance to DB
    // *****************************************************************************
    @Transactional
    private Attendance saveAttendance (Attendance attendance) {
        try {
            if (attendance == null) return null;
            Attendance savedAttendance = attendanceRepository.save(attendance);
            return savedAttendance;
        } catch (Exception ex) {
            return null;
        }
    }

    // *****************************************************************************
    // Returns players attendance between dates
    // *****************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<List<AttendanceDTO>> getPlayerAttendanceBetween(String team, String username, LocalDate startDate, LocalDate endDate) {
        // validations

        // if the team name is empty,  return
        if (team == null || "".equals(team)) {
            return new ApiResponse<>("ERROR", "Please provide the team name of the player.", null);
        }

        // if the username is empty,  return
        if (username == null || "".equals(username)) {
            return new ApiResponse<>("ERROR", "Please provide the username of the player.", null);
        }

        // if start date and end dates are not provided, return
        if (startDate == null || endDate == null) {
            return new ApiResponse<>("ERROR", "Please provide a range of dates.", null);
        }

        try {
            // get the attendadnce for player of a particular team in between dates
            List<Attendance> attendanceList = attendanceRepository.findByPlayer_Profile_User_UserNameAndTeam_NameAndDateBetween(username, team, startDate, endDate);

            // covert the attendace objects to attendance DTO for response to frontend
            List<AttendanceDTO> attendanceDTOList = new ArrayList<>();
            for (Attendance att : attendanceList) {
                AttendanceDTO attendanceDTO = convertToDTO(att);
                attendanceDTOList.add(attendanceDTO);
            }
            return new ApiResponse<>("SUCCESS", "", attendanceDTOList);
        } catch (Exception ex) {
            return new ApiResponse<>("ERROR", ex.getMessage(), null);
        }
    }

    // *****************************************************************************
    // Gets player attendance based on teams, username and date
    // *****************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<AttendanceDTO> getAttendanceByUsernameTeamAndDate(@PathVariable String username, @PathVariable String team, @PathVariable LocalDate date) {

        // validations
        if (username == null || "".equals(username)) {
            return new ApiResponse<>("ERROR", "Please provide username of the player.", null);
        }

        if (date == null) {
            return new ApiResponse<>("ERROR", "Please provide date.", null);
        }

        try {

            // get attendance for a player of a team on specific date
            Attendance attendance = attendanceRepository.findByPlayer_Profile_User_UserNameAndTeam_NameAndDate(username, team, date);
            if (attendance == null) {
                return new ApiResponse<>("ERROR", "Attendance for this player on " + date + " does not exist.", null);
            }
            AttendanceDTO returnedAttendanceDTO = convertToDTO(attendance);
            return new ApiResponse<>("SUCCESS", "", returnedAttendanceDTO);
        } catch (Exception ex) {
            return new ApiResponse<>("ERROR", ex.getMessage(), null);
        }
    }

    // *****************************************************************************
    // Gtes attendance of whole team on a specific date
    // *****************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<List<AttendanceDTO>> getAllTeamAttendanceOnDate(String team, LocalDate date) {
        // validations
        if (team == null || "".equals(team)) {
            return new ApiResponse<>("ERROR", "Please provide team name.", null);
        }

        if (date == null) {
            return new ApiResponse<>("ERROR", "Please provide a date.", null);
        }

        try {
            // get all attendance for a team on specific date
            List <Attendance> attendanceList = new ArrayList<>(attendanceRepository.findByTeam_NameAndDate(team, date));
            
            // convert the attendace objects to attendance DTO for response to frontend
            List<AttendanceDTO> attendanceDTOList = new ArrayList<>();
            for (Attendance att : attendanceList) {
                AttendanceDTO attendanceDTO = convertToDTO(att);
                attendanceDTOList.add(attendanceDTO);
            }
            return new ApiResponse<>("ERROR", "", attendanceDTOList);
        } catch (Exception ex) {
            return new ApiResponse<>("ERROR", ex.getMessage(), null);
        }
    }

    // *****************************************************************************
    // Removes attendance
    // *****************************************************************************
    @Transactional @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<Attendance> removeAttendance(String team, String username, LocalDate date) {
        boolean removedAttendance = attendanceRepository.existsByPlayer_Profile_User_UserNameAndPlayer_Team_NameAndDate(username, team, date);
        if (!removedAttendance) {
            return new ApiResponse("ERROR", "No Attendances found for removal", removedAttendance);    
        }
        return new ApiResponse("SUCCESS", "Attendance removed for " + username + " on date " + date, removedAttendance);
    }
    
    // *****************************************************************************
    // Gets the total count of attendances
    // *****************************************************************************
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<Long> totalAttendances() {
        try {
            return new ApiResponse<>("SUCCESS", "", attendanceRepository.count());
        } catch (Exception ex) {
            return new ApiResponse<>("ERROR", ex.getMessage(), null);
        }
    }
    
    // *****************************************************************************
    // Helper method to convert attendance to dto
    // *****************************************************************************
    private AttendanceDTO convertToDTO (Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setDate(attendance.getDate());
        dto.setTeam(attendance.getTeam().getName());
        dto.setPresent(attendance.getPresent());
        return dto;
    }
}