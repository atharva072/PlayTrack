package com.project.playtrack.DTO;

import java.time.LocalDate;

public class AttendanceDTO {
    private Long id;
    private LocalDate date;
    private String team;
    private String username;
    private boolean present;

    public AttendanceDTO() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getPresent() {
        return present;
    }
    
    public void setPresent(boolean present) {
        this.present = present;
    }
}
