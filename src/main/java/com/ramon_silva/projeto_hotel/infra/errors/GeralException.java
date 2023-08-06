package com.ramon_silva.projeto_hotel.infra.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class GeralException extends RuntimeException {
    public String message;

    public GeralException(String message){
        super(message);
        this.message=message;
    }
}
