package com.cihangunhan.fittrackapi.config;

import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Oturum açmanız gerekiyor");
        }

        return (User) authentication.getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}