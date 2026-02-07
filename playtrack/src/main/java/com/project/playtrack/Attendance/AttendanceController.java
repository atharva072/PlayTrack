package com.project.playtrack.Attendance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.playtrack.Util.ApiResponse;

@RestController
@RequestMapping("/api/attendance/")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ApiResponse<AttendanceDTO> markAttendance (@RequestBody AttendanceDTO request) {
        return attendanceService.markAttendance(request);
    }

    @GetMapping("/playerBetween/{team}/{username}/{startDate}/{endDate}")
    public ApiResponse<List<AttendanceDTO>> getPlayerAttendanceBetween(
        @PathVariable String team, 
        @PathVariable String username, 
        @PathVariable LocalDate startDate, 
        @PathVariable LocalDate endDate) 
    {
        return attendanceService.getPlayerAttendanceBetween (team, username, startDate, endDate);
    }

    @GetMapping("/{username}/{team}/{date}")
    public ApiResponse<AttendanceDTO> getAttendanceByUsernameTeamAndDate(@PathVariable String username, @PathVariable String team, @PathVariable LocalDate date) {
        // LocalDate localDate = LocalDate.parse(date);
        return attendanceService.getAttendanceByUsernameTeamAndDate(username, team, date);
    }
    
    @GetMapping("/all/{team}/{date}")
    public ApiResponse<List<AttendanceDTO>> getAllTeamAttendanceOnDate(@PathVariable String team, @PathVariable LocalDate date) {
        return attendanceService.getAllTeamAttendanceOnDate(team, date);
    }

    @DeleteMapping("/remove/") // CONTINUE HERE
    public ApiResponse<Attendance> removeAttendance(@PathVariable String team, @PathVariable String username, @PathVariable LocalDate date) {
        return attendanceService.removeAttendance(team, username, date);
    }
}