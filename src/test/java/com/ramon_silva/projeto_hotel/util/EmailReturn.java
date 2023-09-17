package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.EmailModel;

public class EmailReturn {
    private EmailModel emailModel;
    private Object paymentDto;
    private String mailConstants;

    private EmailReturn() {
    }

    public EmailModel getEmailModel() {
        return emailModel;
    }

    public void setEmailModel(EmailModel emailModel) {
        this.emailModel = emailModel;
    }

    public Object getPaymentDto() {
        return paymentDto;
    }

    public void setPaymentDto(Object paymentDto) {
        this.paymentDto = paymentDto;
    }

    public String getMailConstants() {
        return mailConstants;
    }

    public void setMailConstants(String mailConstants) {
        this.mailConstants = mailConstants;
    }

    public EmailReturn(EmailModel emailModel, Object paymentDto, String payment) {
        this.emailModel = emailModel;
        this.paymentDto = paymentDto;
        this.mailConstants = payment;
    }

}
