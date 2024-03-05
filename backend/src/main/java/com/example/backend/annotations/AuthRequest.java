/**
 * this is the declaration of the AuthRequest annotation which is responsible for checking validity and permissions of a user
 */

package com.example.backend.annotations;

import com.example.backend.Repository.Permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequest {
    // the permission that is being checked for in the annotation aspect
    Permissions requiredPermission() default Permissions.NO_VALUE;
}
