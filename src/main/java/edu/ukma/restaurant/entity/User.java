package edu.ukma.restaurant.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_user_email", columnNames = "email")
    })
public class User {

    public enum Role { ADMIN, MANAGER, WAITER }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(length = 128)
    private String fullName;

    @Column(length = 128)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role role = Role.WAITER;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getFullName(){return fullName;}
    public void setFullName(String fullName){this.fullName = fullName;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public Role getRole(){return role;}
    public void setRole(Role role){this.role = role;}
    public boolean isActive(){return active;}
    public void setActive(boolean active){this.active = active;}
    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String passwordHash){this.passwordHash = passwordHash;}
}
