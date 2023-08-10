package com.ramon_silva.projeto_hotel.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
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
    void Test_payment_success() {
        ReservationModel reservationModel=ReservationCreator.ReservationModelUpdateConfirm();
        PaymentCreator paymentCreator=PaymentCreator.createModelPayment();
        PaymentDto paymentDto= new PaymentDto(null, null, 
        paymentCreator.getPaymentModel().getPaymentMethod(),
        paymentCreator.getPaymentModel().getPayment_day(),null, 0.0);
        
        when(reservationRepository.findById(reservationModel.getId())).thenReturn(Optional.of(reservationModel));
        when(paymentRepository.save(any(PaymentModel.class))).thenReturn(paymentCreator.getPaymentModel());

         PaymentDto resulDto=paymentServiceIMP.payment(paymentDto, reservationModel.getId());
       
        verify(reservationRepository,times(1))
        .findById(reservationModel.getId());

        verify(emailServiceIMP,times(1))
        .sendEmail(
        paymentCreator.getEmailReturn().getEmailModel(),
        paymentCreator.getEmailReturn().getPaymentDto(),
        paymentCreator.getEmailReturn().getMailConstants()
        );

        verify(paymentRepository,times(1))
        .save(paymentCreator.getPaymentModel()); 
        assertEquals(resulDto.id(),paymentCreator.getPaymentModel().getId());
        assertEquals(resulDto.reservation().id(),paymentCreator.getPaymentModel().getReservation().getId());
        assertEquals(resulDto.status(),StatusEnum.CONFIRM);
    }
    @Test
    @DisplayName("Tentar fazer o pagamento sem a confirmacao da reserva")
    void test_payment_error_status_pending_or_status_canceled() {

        ReservationModel reservationModel=ReservationCreator.ReservationModelUpdatePending();
        PaymentCreator paymentCreator=PaymentCreator.createModelPayment();
        PaymentDto paymentDto= new PaymentDto(null, null, 
        paymentCreator.getPaymentModel().getPaymentMethod(),
        paymentCreator.getPaymentModel().getPayment_day(),null, 0.0);
        
        when(reservationRepository.findById(reservationModel.getId())).thenReturn(Optional.of(reservationModel));
       

       assertThrows(GeralException.class,()->paymentServiceIMP.payment(paymentDto, reservationModel.getId())); 
       
        verify(reservationRepository,times(1))
        .findById(reservationModel.getId());

        verify(emailServiceIMP,times(0))
        .sendEmail(
        paymentCreator.getEmailReturn().getEmailModel(),
        paymentCreator.getEmailReturn().getPaymentDto(),
        paymentCreator.getEmailReturn().getMailConstants()
        );

        verify(paymentRepository,times(0))
        .save(paymentCreator.getPaymentModel()); 
        
    }


@Test
@DisplayName("Atualizar o registro de pagamento modificando o modo de pagamento!")
void Test_update_by_id_payment_success(){
  PaymentModel paymentCreator=PaymentCreator.createModelPayment().getPaymentModel();
  PaymentDto paymentCreatorDto=new PaymentDto(PaymentCreator.createModelPayment2().getPaymentModel());
  PaymentModel resultPay=new PaymentModel(paymentCreator.getId(),paymentCreatorDto);

  
  when(paymentRepository.findById(paymentCreator.getId())).thenReturn(Optional.of(paymentCreator));
  when(paymentRepository.save(resultPay)).thenReturn(resultPay);

  paymentServiceIMP.updateById(paymentCreator.getId(),paymentCreatorDto);
  //verify(paymentRepository,times(1)).findById(paymentCreatorDto.id());
  //verify(paymentRepository,times(1)).save(paymentCreator);
  //assertEquals(paymentDto.id(),paymentCreator.getId());
  //assertNotEquals(paymentDto.paymentMethod(),paymentCreator.getPaymentMethod());

}


    @Test
    @DisplayName("Selecionar o pagamento pelo id")
    void Test_get_payment_by_id_success() {
        PaymentModel paymentCreator=PaymentCreator.createModelPayment().getPaymentModel();
        when(paymentRepository.findById(paymentCreator.getId())).thenReturn(Optional.of(paymentCreator));
        PaymentDto paymentDto=paymentServiceIMP.getPaymentById(paymentCreator.getId());
        verify(paymentRepository,times(1)).findById(paymentCreator.getId());
        
        assertEquals(paymentDto.id(), paymentCreator.getId());
        assertEquals(paymentDto.reservation().id(), paymentCreator.getReservation().getId());
        assertEquals(paymentDto.total_payment(), paymentCreator.getTotal_payment());
    }
    @Test
    @DisplayName("Selecionar o pagamento com um id inexistente")
    void Test_get_payment_by_nonexiste_id() {
        Long id=99L;
      
        when(paymentRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, ()->paymentServiceIMP.getPaymentById(id));
    }

    @Test
    @DisplayName("Selecionar todos os pagamentos")
    void Test_get_all_payments(){
        PaymentModel paymentCreator=PaymentCreator.createModelPayment().getPaymentModel();
        PaymentModel paymentCreator2=PaymentCreator.createModelPayment2().getPaymentModel();
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        List<PaymentModel> paymentModels=new ArrayList<>();
        paymentModels.add(paymentCreator);
        paymentModels.add(paymentCreator2);

        Page<PaymentModel> pay=new PageImpl<>(paymentModels);
        when(paymentRepository.findAll(any(Pageable.class))).thenReturn(pay);

        PageDto<PaymentDto> page=paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(2, page.totalElments());
        assertEquals(1, page.totalPages());

    }

    
    @Test
    @DisplayName("Selecionar todos os pagamentos com lista vazia")
    void Test_get_all_payments_empty(){

        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        List<PaymentModel> paymentModels=new ArrayList<>();
    

        Page<PaymentModel> pay=new PageImpl<>(paymentModels);
        when(paymentRepository.findAll(any(Pageable.class))).thenReturn(pay);

        PageDto<PaymentDto> page=paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(0, page.totalElments());
        assertEquals(1, page.totalPages());
    }

}
