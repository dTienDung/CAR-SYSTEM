package com.example.CAR_.SYSTEM.aspect;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.model.User;
import com.example.CAR_.SYSTEM.model.enums.Role;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class RoleAuthorizationAspect {

    @Before("@annotation(requireRole) || @within(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        if (requireRole == null) return;
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("Unauthorized: Authentication required");
        }
        
        User user = (User) authentication.getPrincipal();
        Role[] allowedRoles = requireRole.value();
        
        boolean hasRole = Arrays.stream(allowedRoles)
                .anyMatch(role -> role.equals(user.getRole()));
        
        if (!hasRole) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletResponse response = attributes.getResponse();
                if (response != null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }
            throw new RuntimeException("Forbidden: Insufficient permissions. Required roles: " + 
                    Arrays.toString(allowedRoles));
        }
    }
}

