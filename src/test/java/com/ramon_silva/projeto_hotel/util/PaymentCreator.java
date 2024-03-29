package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;

public class PaymentCreator {
    private PaymentModel paymentModel;
    private EmailReturn emailReturn;

    private PaymentCreator() {
    }

    @Autowired
    private static ModelMapper modelMapper = new ModelMapper();

    private PaymentCreator(PaymentModel paymentModel, EmailReturn emailReturn) {
        this.paymentModel = paymentModel;
        this.emailReturn = emailReturn;

    }

    public PaymentModel getPaymentModel() {
        return paymentModel;
    }

    public void setPaymentModel(PaymentModel paymentModel) {
        this.paymentModel = paymentModel;
    }

    public EmailReturn getEmailReturn() {
        return emailReturn;
    }

    public void setEmailReturn(EmailReturn emailReturn) {
        this.emailReturn = emailReturn;
    }

    public static PaymentCreator createModelPayment() {
        ReservationModel reservationModel = ReservationCreator.newReservationModel();
        reservationModel.setId(1L);
        Double valueService = calculateValueService(reservationModel);
        LocalDate paymentDay = reservationModel.getCheckOutDate().plusDays(1);

        PaymentModel paymentModel = new PaymentModel();

        paymentModel.setReservation(reservationModel);
        paymentModel.setPaymentMethod(PaymentMethodEnum.MONEY);
        paymentModel.setPayment_day(paymentDay);
        paymentModel.setStatus(StatusEnum.CONFIRM);
        paymentModel.setTotal_payment(reservationModel.getTotal_pay() + valueService);
        PaymentDto resultDto = modelMapper.map(paymentModel, PaymentDto.class);

        EmailReturn emailReturn = createEmailReturn(resultDto);
        return new PaymentCreator(paymentModel, emailReturn);
    }

    public static PaymentCreator createModelPayment2() {
        ReservationModel reservationModel = ReservationCreator.newReservationModel2();
        PaymentModel paymentModel = new PaymentModel();
        reservationModel.setId(2L);
        Double valueService = calculateValueService(reservationModel);
        LocalDate paymentDay = reservationModel.getCheckOutDate().plusDays(1);

        paymentModel.setReservation(reservationModel);
        paymentModel.setPaymentMethod(PaymentMethodEnum.DEBIT);
        paymentModel.setPayment_day(paymentDay);
        paymentModel.setStatus(StatusEnum.CONFIRM);
        paymentModel.setTotal_payment(reservationModel.getTotal_pay() + valueService);

        PaymentDto resultDto = modelMapper.map(paymentModel, PaymentDto.class);

        EmailReturn emailReturn = createEmailReturn(resultDto);
        return new PaymentCreator(paymentModel, emailReturn);
    }

    private static Double calculateValueService(ReservationModel reservationModel) {
        return reservationModel.getReservation_service().stream()
                .mapToDouble(res -> res.getServico().getPrice()).sum();
    }

    private static EmailReturn createEmailReturn(PaymentDto resultDto) {
        EmailModel emailModel = new EmailModel();
        emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
        emailModel.setEmailTo(resultDto.getReservation().getGuest().getEmail());
        emailModel.setText(MailConstants.MESSAGE_PAYMENT);
        emailModel.setSubject(resultDto.getReservation().getRoom().getHotel().getName());

        return new EmailReturn(emailModel, resultDto, MailConstants.PAYMENT);
    }

}