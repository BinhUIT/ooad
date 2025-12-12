package com.example.ooad.mapper;

import java.math.BigDecimal;
import java.util.HashMap;

import com.example.ooad.dto.request.ServiceRequest;
import com.example.ooad.exception.BadRequestException;

public class ServiceRequestMapper {
    public static ServiceRequest serviceRequestFromHashMap(HashMap<String,String> rawData) {
        System.out.println(rawData);
        if(!rawData.containsKey("serviceName")||!rawData.containsKey("unitPrice")) {
            throw new BadRequestException("Bad Request");
        } 
        try{
            Float price =Float.valueOf(rawData.get("unitPrice"));
        
        return new ServiceRequest(rawData.get("serviceName"),price);
        }
        catch(Exception e) {
            throw new BadRequestException("Bad Request unoit price");
        }
        
    }
}
