package com.ramon_silva.projeto_hotel.services;

import com.itextpdf.text.DocumentException;
import com.ramon_silva.projeto_hotel.models.EmailModel;

import jakarta.mail.MessagingException;


public interface EmailService {
    public void sendEmail(EmailModel emailModel,Object object,String typeEmail)throws MessagingException,DocumentException;
}
