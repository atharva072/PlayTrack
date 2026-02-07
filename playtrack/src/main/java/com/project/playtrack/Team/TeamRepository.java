package com.project.playtrack.Team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String>{

    // get a team based on name and city
    public Team findByNameAndCity(String name, String city);
}
