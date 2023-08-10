package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;

public class PaymentCreator{
    private static PaymentModel paymentModel;
    private static EmailReturn emailReturn;

    private PaymentCreator(PaymentModel paymentModel, EmailReturn emailReturn) {
        PaymentCreator.paymentModel = paymentModel;
        PaymentCreator.emailReturn= emailReturn;
    }

    public PaymentModel getPaymentModel() {
        return paymentModel;
    }

    public void setPaymentModel(PaymentModel paymentModel) {
       PaymentCreator.paymentModel = paymentModel;
    }

    public EmailReturn getEmailReturn() {
        return emailReturn;
    }

    public void setEmailReturn(EmailReturn emailReturn) {
        PaymentCreator.emailReturn = emailReturn;
    }

    public static PaymentCreator createModelPayment(){
        PaymentModel paymentModel=new PaymentModel();
        ReservationModel reservationModel=ReservationCreator.ReservationModelUpdateConfirm();
        Double valueService=reservationModel.getReservation_service().stream()
           .mapToDouble(res->res.getServico().getPrice()).sum();
            LocalDate paymentDay=reservationModel.getCheckOutDate().plusDays(1);
            
            paymentModel.setId(1L);
            paymentModel.setReservation(reservationModel);
            paymentModel.setPaymentMethod(PaymentMethodEnum.MONEY);
            paymentModel.setPayment_day(paymentDay);
            paymentModel.setStatus(StatusEnum.CONFIRM);
            paymentModel.setTotal_payment(reservationModel.getTotal_pay()+valueService);
            PaymentModel result=paymentModel;
            PaymentDto resultDto= new PaymentDto(result);

            EmailModel emailModel=new EmailModel();
            emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
            emailModel.setEmailTo(resultDto.reservation().client().email());
            emailModel.setText(MailConstants.MESSAGE_PAYMENT);
            emailModel.setSubject(resultDto.reservation().room().hotel().name());
            PaymentCreator.emailReturn=new EmailReturn(emailModel, resultDto, MailConstants.PAYMENT);

            return new PaymentCreator(paymentModel, emailReturn);
    }
    public static PaymentCreator createModelPayment2(){
        PaymentModel paymentModel=new PaymentModel();
        ReservationModel reservationModel=ReservationCreator.ReservationModelUpdateConfirm();
        Double valueService=reservationModel.getReservation_service().stream()
           .mapToDouble(res->res.getServico().getPrice()).sum();
            LocalDate paymentDay=reservationModel.getCheckOutDate().plusDays(1);
            
            paymentModel.setId(2L);
            paymentModel.setReservation(reservationModel);
            paymentModel.setPaymentMethod(PaymentMethodEnum.MONEY);
            paymentModel.setPayment_day(paymentDay);
            paymentModel.setStatus(StatusEnum.CONFIRM);
            paymentModel.setTotal_payment(reservationModel.getTotal_pay()+valueService);
            PaymentModel result=paymentModel;
            PaymentDto resultDto= new PaymentDto(result);

            EmailModel emailModel=new EmailModel();
            emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
            emailModel.setEmailTo(resultDto.reservation().client().email());
            emailModel.setText(MailConstants.MESSAGE_PAYMENT);
            emailModel.setSubject(resultDto.reservation().room().hotel().name());
            PaymentCreator.emailReturn=new EmailReturn(emailModel, resultDto, MailConstants.PAYMENT);

            return new PaymentCreator(paymentModel, emailReturn);
    }
    
}