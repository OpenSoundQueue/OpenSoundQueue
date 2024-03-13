/**
 * This exception is being thrown if a user trying to access resources that they are not permitted for.
 * It can also be thrown if the user can not be identified
 */

package com.example.backend.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
