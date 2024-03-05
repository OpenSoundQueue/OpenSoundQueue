/**
 * This is the Aspect for the AuthRequest annotation
 * It implements the logic behind the annotation
 */

package com.example.backend.annotations;

import com.example.backend.Repository.Permissions;
import com.example.backend.Repository.UserInfoEntity;
import com.example.backend.exceptions.UnauthorizedException;
import com.example.backend.system_management.PropertyService;
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
    PropertyService propertyService;
    public AuthAspect(UserService userService, PropertyService propertyService) {
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @Around("@annotation(AuthRequest)")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        // getting parameter of annotation
        Permissions requiredPermission = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuthRequest.class).requiredPermission();

        // the permissions checks are disabled while the installation process is not finished
        if (requiredPermission.equals(Permissions.MANAGE_SYSTEM_SETTINGS)) {
            if (!Boolean.parseBoolean(propertyService.getProperty("system.installed"))) return joinPoint.proceed();
        }

        // getting the users token out of the function
        String token = "";
        for (Object o:joinPoint.getArgs()) {
            if (o instanceof String) {
                token = (String) o;
                break;
            }
        }

        // checking the api key
        if (!userService.verifyApiKey(token)) {
            throw new UnauthorizedException("token");
        }

        // if no permission is specified the user is authorized to continue
        if (requiredPermission.equals(Permissions.NO_VALUE)) return joinPoint.proceed();

        //checking permissions of the user
        UserInfoEntity user = userService.getUserByToken(token);
        if (!userService.getPermissionsOfUser(user.getId()).contains(requiredPermission)) {
            throw new UnauthorizedException("permission");
        }

        return joinPoint.proceed();
    }
}
