package com.ramon_silva.projeto_hotel.singles.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.ramon_silva.projeto_hotel.controllers.HotelController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.services.HotelServiceIMP;
import com.ramon_silva.projeto_hotel.util.HotelCreator;



@ExtendWith(MockitoExtension.class)
public class  HotelControllerTest {

@InjectMocks
private HotelController hotelController;

@Mock
private HotelServiceIMP hotelServiceIMP;

@Autowired
private ModelMapper modelMapper=new ModelMapper();

private HotelDto hotelDto;

private HotelModel hotelModel;

private UriComponentsBuilder uriBuilder=UriComponentsBuilder.newInstance();
@BeforeEach
public void setUp(){
}

@Test
@DisplayName("Hotel criado com sucesso")
void Test_create_hotel_controller_success(){

  hotelModel =HotelCreator.newModelHotel();
  hotelDto=modelMapper.map(hotelModel,HotelDto.class);
  hotelModel.setId(1L);
  hotelModel.getAddress().setId(1L);
  HotelDto hotelDto2=modelMapper.map(hotelModel,HotelDto.class);
  
  when(hotelServiceIMP.create(any(HotelDto.class))).thenReturn(hotelDto2);

        ResponseEntity<HotelDto> response = hotelController.create(hotelDto, uriBuilder);

        verify(hotelServiceIMP,times(1)).create(any(HotelDto.class));
         assertNotNull(response.getBody());
         assertNotNull(response.getBody().getId());
         assertNotNull(response.getBody().getAddress().getId());
         assertEquals(response.getStatusCode(),HttpStatus.CREATED);
}

@Test
@DisplayName("Criar Hotel com cnpj ou email iguais ")
void Test_create_hotel_with_dates_empty(){
  hotelModel =HotelCreator.newModelHotel();
  hotelDto=modelMapper.map(hotelModel,HotelDto.class);


  when(hotelServiceIMP.create(hotelDto)).thenThrow(GeralException.class);
   assertThrows(GeralException.class, ()->hotelController.create(hotelDto, uriBuilder));  
  verify(hotelServiceIMP,times(1)).create(hotelDto);   
   
}

@Test
@DisplayName("Atualizar Hoteis")
void Test_update_hoteis_by_id(){
  hotelModel =HotelCreator.newModelHotel();
  hotelModel.setId(1L);
  hotelModel.getAddress().setId(1L);

  hotelDto=modelMapper.map(hotelModel,HotelDto.class);

  hotelModel.setName("mudados");
  hotelModel.setPhone("(11)11111-11111");
  
  HotelDto hotelDto2=modelMapper.map(hotelModel,HotelDto.class);

  
  when(hotelServiceIMP.updateById(eq(hotelDto.getId()), any(HotelDto.class))).thenReturn(hotelDto2);

  ResponseEntity<HotelDto> response=hotelController.updateById(hotelDto.getId(), hotelDto2);
    
  verify(hotelServiceIMP, times(1)).updateById(eq(hotelDto.getId()), any(HotelDto.class));

  assertEquals(HttpStatus.OK, response.getStatusCode());
  assertEquals(hotelDto.getId(), response.getBody().getId());
  assertNotEquals(hotelDto.getPhone(), response.getBody().getPhone());
  assertNotEquals(hotelDto.getName(), response.getBody().getName());

}

@Test
@DisplayName("Atualizar Hotels com cnpj ou email ja cadastrados")
void Test_update_hotel_by_id_with_cpf_or_email_exists(){
   hotelModel =HotelCreator.newModelHotel();
   hotelModel.setId(1L);
   hotelModel.getAddress().setId(1L);

  hotelDto=modelMapper.map(hotelModel,HotelDto.class);
  when(hotelServiceIMP.updateById(eq(hotelDto.getId()), any(HotelDto.class))).thenThrow(GeralException.class);
  assertThrows(GeralException.class,()->hotelController.updateById(hotelDto.getId(), hotelDto));    
  verify(hotelServiceIMP, times(1)).updateById(eq(hotelDto.getId()), any(HotelDto.class));

}

@Test
@DisplayName("Atualizar Hoteis com id inexistente")
void Test_update_hotel_by_nonexist_id(){
  Long id=99l;
   hotelModel =HotelCreator.newModelHotel();
   hotelModel.setId(1L);
   hotelModel.getAddress().setId(1L);
  hotelDto=modelMapper.map(hotelModel,HotelDto.class);
  when(hotelServiceIMP.updateById(eq(id), any(HotelDto.class))).thenThrow(ResourceNotFoundException.class);
  assertThrows(ResourceNotFoundException.class,()->hotelController.updateById(id, hotelDto));    
  verify(hotelServiceIMP, times(1)).updateById(eq(id), any(HotelDto.class));

}

@Test
@DisplayName("Deletar Hotel com sucesso")
void Test_delete_hotel_by_id(){
  Long id=1L;

  doNothing().when(hotelServiceIMP).deleteById(id);
  ResponseEntity<Void> response=hotelController.deleteById(id);
  verify(hotelServiceIMP,times(1)).deleteById(id);
  assertEquals(HttpStatus.OK,response.getStatusCode());
  assertNull(response.getBody());
}

@Test
@DisplayName("Deletar Hotel com id inexistente")
void Test_delete_Hotel_by_nonexist_id(){
  Long id=99L;

  doThrow(ResourceNotFoundException.class).when(hotelServiceIMP).deleteById(eq(id));

  assertThrows(ResourceNotFoundException.class,()->hotelController.deleteById(id));

  verify(hotelServiceIMP, times(1)).deleteById(eq(id));

}

@Test
@DisplayName("encontrar Hotel pelo id com sucesso")
void Test_get_hotel_by_id(){
  
  hotelModel =HotelCreator.newModelHotel();
   hotelModel.setId(1L);
   hotelModel.getAddress().setId(1L);
  hotelDto=modelMapper.map(hotelModel,HotelDto.class);

  when(hotelServiceIMP.getById(hotelDto.getId())).thenReturn(hotelDto);

  ResponseEntity<HotelDto> response=hotelController.getById(hotelDto.getId());
  
  verify(hotelServiceIMP,times(1)).getById(hotelDto.getId());
  
  assertEquals(HttpStatus.OK,response.getStatusCode());
  assertNotNull(response.getBody());
}

@Test
@DisplayName("encontrar Hotel com id inexistente")
void Test_get_hotel_by_nonexist_id(){
  Long id=99L;

  doThrow(ResourceNotFoundException.class).when(hotelServiceIMP).getById(eq(id));

  assertThrows(ResourceNotFoundException.class,()->hotelController.getById(id));

  verify(hotelServiceIMP, times(1)).getById(eq(id));

}

@Test
@DisplayName("Listar todos os Hoteis")
void Test_get_all_hoteis(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
        int numberOfElements= 2;
        int totalPages= 1;
        long totalElments=2;

   hotelModel =HotelCreator.newModelHotel();
   hotelModel.setId(1L);
   hotelModel.getAddress().setId(1L);

   HotelModel hotelModel2 =HotelCreator.newModelHotel2();
   hotelModel2.setId(2L);
   hotelModel2.getAddress().setId(2L);

        HotelDto hotelDto=modelMapper.map(hotelModel,HotelDto.class);
        HotelDto hotelDto2=modelMapper.map(hotelModel2,HotelDto.class);

        List<HotelDto> hotelDtos=new ArrayList<>();
        hotelDtos.add(hotelDto);
        hotelDtos.add(hotelDto2);

        PageDto<HotelDto> pageDto= new PageDto<>(hotelDtos, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(hotelServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(pageDto);

        ResponseEntity<PageDto<HotelDto>> response=hotelController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(hotelDtos.size(),response.getBody().getGetContent().size());
        assertEquals(hotelDtos.size(),response.getBody().getNumberOfElements() );
        
}

@Test
@DisplayName("Listar todos os Hotels com lista vazia")
void Test_get_all_hoteis_empty_list(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
        int numberOfElements= 0;
        int totalPages= 1;
        long totalElments=0;


   
        List<HotelDto> hotelDtos=new ArrayList<>();

        PageDto<HotelDto> pageDto= new PageDto<>(hotelDtos, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(hotelServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(pageDto);

        ResponseEntity<PageDto<HotelDto>> response=hotelController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(hotelDtos.size(),response.getBody().getGetContent().size());
        assertEquals(hotelDtos.size(),response.getBody().getNumberOfElements() );
        
}

}

