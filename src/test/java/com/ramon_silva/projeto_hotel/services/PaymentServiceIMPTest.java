package com.ramon_silva.projeto_hotel.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.util.PaymentCreator;
import com.ramon_silva.projeto_hotel.util.ReservationCreator;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceIMPTest {

    @InjectMocks
    private PaymentServiceIMP paymentServiceIMP;

    @Mock
    private  PaymentRepository paymentRepository;
    
    @Mock
    private  ReservationRepository reservationRepository;
    
    @Mock
    private  EmailServiceIMP emailServiceIMP;

    @Test
    @DisplayName("Sucesso no pagamento da estadia")
    void testPayment() {
        ReservationModel reservationModel=ReservationCreator.ReservationModelUpdateConfirm();
        PaymentCreator paymentCreator=PaymentCreator.createModelPayment();
        PaymentDto paymentDto= new PaymentDto(null, null, 
        paymentCreator.getPaymentModel().getPaymentMethod(),
        paymentCreator.getPaymentModel().getPayment_day(),null, 0.0);
        
        when(reservationRepository.findById(reservationModel.getId())).thenReturn(Optional.of(reservationModel));
        when(paymentRepository.save(any(PaymentModel.class))).thenReturn(paymentCreator.getPaymentModel());

        paymentServiceIMP.payment(paymentDto, reservationModel.getId());
       
        verify(reservationRepository,times(1)).findById(reservationModel.getId());
        verify(emailServiceIMP,times(1)).sendEmail(paymentCreator.getEmailReturn().getEmailModel(),
        paymentCreator.getEmailReturn().getPaymentDto(),
        paymentCreator.getEmailReturn().getMailConstants());
        verify(paymentRepository,times(1)).save(paymentCreator.getPaymentModel());


       
        
    }
    @Test
    void testGetAll() {

    }

    @Test
    void testGetPaymentById() {

    }

}
