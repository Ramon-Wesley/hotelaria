package com.ramon_silva.projeto_hotel.infra.errors;

public class GeralException extends RuntimeException {
    public String message;

    public GeralException(String message){
        super(message);
        this.message=message;
    }
}
