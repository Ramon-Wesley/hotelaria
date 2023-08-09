package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.models.EmailModel;

public class EmailReturn {
    private static EmailModel emailModel;
    private static Object paymentDto;
    private static String mailConstants;

    public EmailModel getEmailModel() {
        return emailModel;
    }

    public void setEmailModel(EmailModel emailModel) {
        EmailReturn.emailModel = emailModel;
    }

    public Object getPaymentDto() {
        return paymentDto;
    }

    public void setPaymentDto(Object paymentDto) {
        EmailReturn.paymentDto = paymentDto;
    }

    public String getMailConstants() {
        return mailConstants;
    }

    public void setMailConstants(String mailConstants) {
        EmailReturn.mailConstants = mailConstants;
    }

    public EmailReturn(EmailModel emailModel, Object paymentDto, String payment) {
        EmailReturn.emailModel = emailModel;
        EmailReturn.paymentDto = paymentDto;
        EmailReturn.mailConstants = payment;
    } 

    
}
