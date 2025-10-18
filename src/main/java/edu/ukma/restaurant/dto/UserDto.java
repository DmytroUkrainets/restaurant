package edu.ukma.restaurant.dto;

public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private boolean active;
    private String password;

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username=username;}
    public String getFullName(){return fullName;}
    public void setFullName(String fullName){this.fullName=fullName;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getRole(){return role;}
    public void setRole(String role){this.role=role;}
    public boolean isActive(){return active;}
    public void setActive(boolean active){this.active=active;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}
}
