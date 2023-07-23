package com.ramon_silva.projeto_hotel.infra.errors;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ErrorsConfiguration {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> error404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgument(MethodArgumentNotValidException err){
        var errors=err.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(fieldErros::new).toList());
    }

    public record fieldErros(String field, String message){
        public fieldErros(FieldError err){
            this(err.getField(),err.getDefaultMessage());
        }
    }
}
