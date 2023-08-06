package com.ramon_silva.projeto_hotel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;

public class MethodsJson {
    
    public static String asJsonString(Object obj){
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            return objectMapper.writeValueAsString(objectMapper);
        } catch (Exception e) {
          throw new  GeralException(e.getMessage());
        }
    
    }
}
