package com.ramon_silva.projeto_hotel.services.interfaces;

import com.ramon_silva.projeto_hotel.models.EmailModel;


public interface IEmailService {
    public void sendEmail(EmailModel emailModel,Object object,String typeEmail);
}
