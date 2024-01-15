package com.example.backend.annotations;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.exceptions.UnauthorizedException;
import com.example.backend.user_management.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {
    UserService userService;
    public AuthAspect(UserService userService) {
        this.userService = userService;
    }

    @Around("@annotation(AuthRequest)")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = "";
        for (Object o:joinPoint.getArgs()) {
            if (o instanceof String) {
                token = (String) o;
                break;
            }
        }
        if (!userService.verifyApiKey(token)) {
            throw new UnauthorizedException("token");
        }

        Permissions requiredPermission = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuthRequest.class).requiredPermission();
        if (requiredPermission.equals(Permissions.NO_VALUE)) return joinPoint.proceed();

        UserInfoEntity user = userService.getUserByToken(token);

        if (!userService.getPermissionsOfUser(user.getId()).contains(requiredPermission)) {
            throw new UnauthorizedException("permission");
        }

        return joinPoint.proceed();
    }
}
