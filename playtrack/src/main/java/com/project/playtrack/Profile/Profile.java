package com.project.playtrack.Profile;

import com.project.playtrack.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="profiles")
public class Profile {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="phone", length = 20)
    private String phone;

    @Column(name="city")
    private String city;

    @Column(name="designation")
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProfileType type;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Profile () {}

    public Profile(String city, String designation, String firstName, String lastName, String phone, ProfileType profileType, User user) {
        this.city = city;
        this.designation = designation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.type = profileType;
        this.user = user;
    }

    public Long getId() { return id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public ProfileType getType() { return type; }
    public void setType(ProfileType type) { this.type = type; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Transient
    public String getFullName() { return firstName + " " + lastName;}
}
