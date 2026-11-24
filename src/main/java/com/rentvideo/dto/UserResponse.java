
package com.rentvideo.dto;

import com.rentvideo.entity.AppUser;
import com.rentvideo.entity.Role;

public record UserResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    Role role
) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole()
        );
    }
}
