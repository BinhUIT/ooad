package com.example.ooad.exception;

import com.example.ooad.dto.response.GlobalResponse;

public class ExceptionHandler {
    public static <T> GlobalResponse<T> getResponseFromException(Exception e) {
        
        if(e instanceof BadRequestException) {
            return new GlobalResponse(null,e.getMessage(),400);
        }
        if(e instanceof NotFoundException) {
            return new GlobalResponse(null,e.getMessage(),404);
        }
        if(e instanceof InternalServerErrorException) {
            return new GlobalResponse(null,e.getMessage(),500);
        }      
        if(e instanceof UnauthorizedException) {
            return new GlobalResponse(null,e.getMessage(),401);
        }  
        if(e instanceof ConflictException) {
            return new GlobalResponse(null, e.getMessage(),409);
        }
        return new GlobalResponse(null,e.getMessage(),500);
    }
}
