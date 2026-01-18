package com.project.playtrack.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.playtrack.Entity.Team;

public interface TeamRepository extends JpaRepository<Team, String>{

    // get a team based on name and city
    public Team findByNameAndCity(String name, String city);
}
