package com.ramon_silva.projeto_hotel.singles.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.ramon_silva.projeto_hotel.controllers.PaymentController;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.services.PaymentServiceIMP;
import com.ramon_silva.projeto_hotel.util.PaymentCreator;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {
    
    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentServiceIMP paymentServiceIMP;

    private PaymentDto paymentDto;

    private PaymentModel paymentModel;

    private UriComponentsBuilder uriBuilder=UriComponentsBuilder.newInstance();
    @Test
    @DisplayName("Sucesso ao realizar um pagamento de reserva")
    void Test_payment_success(){
        paymentModel=PaymentCreator.createModelPayment().getPaymentModel();
        paymentDto=new PaymentDto(paymentModel);
        paymentModel.setId(1L);
        PaymentDto payment=new PaymentDto(paymentModel);
        Long reservation_id=paymentDto.reservation().id();
        when(paymentServiceIMP.payment(any(PaymentDto.class),anyLong())).thenReturn(payment);

         ResponseEntity<PaymentDto> response= paymentController.payment(reservation_id, payment, uriBuilder);

         assertEquals(HttpStatus.CREATED,response.getStatusCode());
         assertNotNull(response.getBody().id());
         verify(paymentServiceIMP,times(1)).payment(any(PaymentDto.class),anyLong());

    }
    
  @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva nao confirmada")
    void Test_payment_error_with_reservation_pending(){
        paymentModel=PaymentCreator.createModelPayment2().getPaymentModel();
        paymentDto=new PaymentDto(paymentModel);
        paymentModel.setId(1L);
        PaymentDto payment=new PaymentDto(paymentModel);
        Long reservation_id=paymentDto.reservation().id();
        when(paymentServiceIMP.payment(any(PaymentDto.class),anyLong())).thenThrow(GeralException.class);

        assertThrows(GeralException.class,()->paymentController.payment(reservation_id, payment, uriBuilder));

         verify(paymentServiceIMP,times(1)).payment(any(PaymentDto.class),anyLong());

    }

     @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva inexistente")
    void Test_payment_error_with_reservation_notExists(){
        paymentModel=PaymentCreator.createModelPayment2().getPaymentModel();
        paymentDto=new PaymentDto(paymentModel);
        paymentModel.setId(1L);
        PaymentDto payment=new PaymentDto(paymentModel);
        Long reservation_id=99L;
        when(paymentServiceIMP.payment(any(PaymentDto.class),anyLong())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,()->paymentController.payment(reservation_id, payment, uriBuilder));

         verify(paymentServiceIMP,times(1)).payment(any(PaymentDto.class),anyLong());

    }

    @Test
    @DisplayName("Deletar com sucesso um pagamento")
    void Test_delete_payment_by_id(){
        Long id=1L;
        ResponseEntity<Void> result=paymentController.deleteById(id);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        verify(paymentServiceIMP,times(1)).deletePaymentById(id);
                 

    }
    
}
