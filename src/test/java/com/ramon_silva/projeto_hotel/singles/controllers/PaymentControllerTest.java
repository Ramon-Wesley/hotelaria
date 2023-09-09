package com.ramon_silva.projeto_hotel.singles.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.ramon_silva.projeto_hotel.controllers.PaymentController;
import com.ramon_silva.projeto_hotel.dto.PageDto;
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

    @Autowired 
    private ModelMapper modelMapper=new ModelMapper();

    private PaymentDto paymentDto;

    private PaymentModel paymentModel;

    private UriComponentsBuilder uriBuilder=UriComponentsBuilder.newInstance();
    @Test
    @DisplayName("Sucesso ao realizar um pagamento de reserva")
    void Test_payment_success(){
        paymentModel=PaymentCreator.createModelPayment().getPaymentModel();
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        paymentModel.setId(1L);
        PaymentDto payment=modelMapper.map(paymentModel,PaymentDto.class);
        Long reservation_id=paymentDto.getReservation().getId();
        when(paymentServiceIMP.create(any(PaymentDto.class))).thenReturn(payment);

         ResponseEntity<PaymentDto> response= paymentController.payment(reservation_id, payment, uriBuilder);

         assertEquals(HttpStatus.CREATED,response.getStatusCode());
         assertNotNull(response.getBody().getId());
         verify(paymentServiceIMP,times(1)).create(any(PaymentDto.class));

    }
    
  @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva nao confirmada")
    void Test_payment_error_with_reservation_pending(){
        paymentModel=PaymentCreator.createModelPayment2().getPaymentModel();
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        paymentModel.setId(1L);
        PaymentDto payment=modelMapper.map(paymentModel,PaymentDto.class);
        Long reservation_id=paymentDto.getReservation().getId();
        when(paymentServiceIMP.create(any(PaymentDto.class))).thenThrow(GeralException.class);

        assertThrows(GeralException.class,()->paymentController.payment(reservation_id, payment, uriBuilder));

         verify(paymentServiceIMP,times(1)).create(any(PaymentDto.class));

    }

     @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva inexistente")
    void Test_payment_error_with_reservation_notExists(){
        paymentModel=PaymentCreator.createModelPayment2().getPaymentModel();
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        paymentModel.setId(1L);
        PaymentDto payment=modelMapper.map(paymentModel,PaymentDto.class);
        Long reservation_id=99L;
        when(paymentServiceIMP.create(any(PaymentDto.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,()->paymentController.payment(reservation_id, payment, uriBuilder));

         verify(paymentServiceIMP,times(1)).create(any(PaymentDto.class));

    }

    @Test
    @DisplayName("Deletar com sucesso um pagamento")
    void Test_delete_payment_by_id(){
        Long id=1L;
        ResponseEntity<Void> result=paymentController.deleteById(id);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        verify(paymentServiceIMP,times(1)).deleteById(id);
                 

    }
    
      @Test
    @DisplayName("Deletar um pagamento inexistente")
    void Test_delete_payment_by_id_notExists(){
        Long id=99L;
        doThrow(ResourceNotFoundException.class).when(paymentServiceIMP).deleteById(id);

    
        assertThrows(ResourceNotFoundException.class,()->paymentController.deleteById(id));

        verify(paymentServiceIMP,times(1)).deleteById(id);
                
    }

    @Test
    @DisplayName("Atualizar pagamento existente")
    void Test_update_payment_by_id(){
        paymentModel=PaymentCreator.createModelPayment().getPaymentModel();
        paymentModel.setId(1L);
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        Long id=paymentDto.getId();
        paymentModel.setPaymentMethod(PaymentCreator.createModelPayment2().getPaymentModel().getPaymentMethod());

        PaymentDto updatePayment=modelMapper.map(paymentModel,PaymentDto.class);

        when(paymentServiceIMP.updateById(id, updatePayment)).thenReturn(updatePayment);

        ResponseEntity<PaymentDto> response=paymentController.updateById(id, updatePayment);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(id,response.getBody().getId());
        assertNotEquals(paymentDto.getPaymentMethod(), updatePayment.getPaymentMethod());
        verify(paymentServiceIMP,times(1)).updateById(id, updatePayment);    
                
    }

    @Test
    @DisplayName("Atualizar pagamento inexistente")
    void Test_update_payment_with_notExist_id(){
        paymentModel=PaymentCreator.createModelPayment().getPaymentModel();
        paymentModel.setId(1L);
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        Long id=99L;
        paymentModel.setPaymentMethod(PaymentCreator.createModelPayment2().getPaymentModel().getPaymentMethod());

        PaymentDto updatePayment=modelMapper.map(paymentModel,PaymentDto.class);

        when(paymentServiceIMP.updateById(id, updatePayment)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, ()->paymentController.updateById(id, updatePayment));

        verify(paymentServiceIMP,times(1)).updateById(id, updatePayment);    
                
    }

  @Test
    @DisplayName("selecionar com sucesso um pagamento")
    void Test_get_payment_by_id(){
        paymentModel=PaymentCreator.createModelPayment().getPaymentModel();
        paymentModel.setId(1L);
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        Long id=paymentDto.getId();
        when(paymentServiceIMP.getById(id)).thenReturn(paymentDto);
        ResponseEntity<PaymentDto> result=paymentController.getById(id);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(id,result.getBody().getId());
        assertEquals(paymentDto,result.getBody());
        verify(paymentServiceIMP,times(1)).getById(id);
                 

    }

      @Test
    @DisplayName("selecionar um pagamento inexistente")
    void Test_get_payment_notExist_by_id(){
   
        Long id=99L;
        when(paymentServiceIMP.getById(id)).thenThrow(ResourceNotFoundException.class);
        
        assertThrows(ResourceNotFoundException.class, ()->paymentController.getById(id));

        verify(paymentServiceIMP,times(1)).getById(id);
                
    }


    @Test
    @DisplayName("Listar pagamentos")
    void Test_get_all_payment(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        String sortOrder = "asc";
        int numberOfElements= 2;
        int totalPages= 1;
        long totalElments=2;
    

        paymentModel=PaymentCreator.createModelPayment().getPaymentModel();
        paymentModel.setId(1L);
        PaymentModel paymentModel2=PaymentCreator.createModelPayment2().getPaymentModel();
        paymentModel2.setId(2L);
        
        paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
        PaymentDto paymentDto2=modelMapper.map(paymentModel2,PaymentDto.class);
        List<PaymentDto> paymentList=new ArrayList<>();
        paymentList.add(paymentDto);
        paymentList.add(paymentDto2);

        List<PaymentDto> paymentList2=paymentList.stream().sorted(Comparator.comparingLong(obj->((PaymentDto) obj).getId()).reversed()).collect(Collectors.toList());
    
        PageDto<PaymentDto> page=new PageDto<>(paymentList2, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(page);

        ResponseEntity<PageDto<PaymentDto>> response=paymentController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(numberOfElements,response.getBody().getGetContent().size());
        assertNotEquals(paymentList,paymentList2);

        verify(paymentServiceIMP,times(1)).getAll(pageNumber, pageSize, sortBy, sortOrder);
    }

     @Test
    @DisplayName("Listar pagamentos com lista vazia")
    void Test_get_all_payment_empty_list(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        String sortOrder = "asc";
        int numberOfElements= 0;
        int totalPages= 1;
        long totalElments=0;
    

     
        List<PaymentDto> paymentList=new ArrayList<>();
    
     
        PageDto<PaymentDto> page=new PageDto<>(paymentList, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(page);

        ResponseEntity<PageDto<PaymentDto>> response=paymentController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(numberOfElements,response.getBody().getGetContent().size());
        

        verify(paymentServiceIMP,times(1)).getAll(pageNumber, pageSize, sortBy, sortOrder);
    }
}


