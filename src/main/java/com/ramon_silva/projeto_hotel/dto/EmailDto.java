package com.ramon_silva.projeto_hotel.dto;

import com.ramon_silva.projeto_hotel.models.EmailModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDto(

    @NotBlank
    String owerRef,
    @NotBlank
    @Email
    String emailFrom,
    @NotBlank
    @Email
    String emailTo,
    @NotBlank
    String subject,
    @NotBlank
    String text
) {
    public EmailDto(EmailModel email){
        this(email.getOwerRef(), email.getEmailFrom(),email.getEmailTo(),email.getSubject(), 
        email.getText());
    }
}
