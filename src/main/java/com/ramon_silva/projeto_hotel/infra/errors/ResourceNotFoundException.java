package com.ramon_silva.projeto_hotel.infra.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    
    private static final Long serialVersionUID=1L;
    private String resourceName;
    private String fieldName;
    private Long fieldValue;
    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue) {
        super(String.format("%s não encontrado com : %s : '%s'", resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    
}
