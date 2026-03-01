package com.project.playtrack.Profile;

public class ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private String designation;
    private ProfileType type;
    private String username;

    public ProfileDTO () {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
