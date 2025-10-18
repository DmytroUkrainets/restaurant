package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.entity.User;
import edu.ukma.restaurant.dto.UserDto;

public class UserMapper {

    public static UserDto toDto(User e){
        if(e==null) return null;
        UserDto d = new UserDto();
        d.setId(e.getId());
        d.setUsername(e.getUsername());
        d.setFullName(e.getFullName());
        d.setEmail(e.getEmail());
        d.setRole(e.getRole()!=null ? e.getRole().name() : null);
        d.setActive(e.isActive());
        return d;
    }

    public static void updateEntity(User e, UserDto d){
        if(d.getUsername()!=null) e.setUsername(d.getUsername());
        if(d.getFullName()!=null) e.setFullName(d.getFullName());
        if(d.getEmail()!=null) e.setEmail(d.getEmail());
        if(d.getRole()!=null) {
            try { e.setRole(User.Role.valueOf(d.getRole())); } catch (Exception ignored) {}
        }
        e.setActive(d.isActive() || !d.isActive() ? d.isActive() : e.isActive());
    }
}
