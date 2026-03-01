package com.project.playtrack.Profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.playtrack.User.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByFirstNameAndLastNameAndUser(String firstName, String lastName, User user);
    Optional<Profile> findByFirstNameAndLastNameAndUserId(String firstName, String lastName, Long userId);
    Profile findByUserId(Long userId);
}