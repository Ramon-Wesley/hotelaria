package com.ramon_silva.projeto_hotel.services;

import com.ramon_silva.projeto_hotel.models.EmailModel;


public interface EmailService {
    public void sendEmail(EmailModel emailModel,Object object,String typeEmail);
}
