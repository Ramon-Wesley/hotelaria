package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.services.HotelServiceIMP;
import com.ramon_silva.projeto_hotel.util.HotelCreator;

@ExtendWith(MockitoExtension.class)
public class HotelServiceIMPTest {
    
    @InjectMocks
    private HotelServiceIMP hotelServiceIMP;

    @Mock
    private HotelRepository hotelRepository;

    private HotelModel hotelModel;
    private HotelDto hotelDto;
    @Test
    @DisplayName("Criar um hotel com sucesso!")
    void Test_create_hotel_sucess(){
     hotelModel=HotelCreator.newModelHotel();
     hotelDto=new HotelDto(hotelModel);
     hotelModel.setId(1L);

     when(hotelRepository.existsByCnpj(hotelDto.cnpj())).thenReturn(false);
     when(hotelRepository.save(any(HotelModel.class))).thenReturn(hotelModel);

     hotelDto=hotelServiceIMP.create(hotelDto);

     verify(hotelRepository,times(1)).existsByCnpj(hotelDto.cnpj());
     verify(hotelRepository,times(1)).save(any(HotelModel.class));
    }

    @Test
    @DisplayName("Criar um hotel com cnpj ja cadastrado!")
    void Test_create_hotel_with_cnpj_exists(){
     hotelModel=HotelCreator.newModelHotel();
     hotelDto=new HotelDto(hotelModel);
     hotelModel.setId(1L);

     when(hotelRepository.existsByCnpj(hotelDto.cnpj())).thenReturn(true);
  

    assertThrows(GeralException.class,()-> hotelServiceIMP.create(hotelDto));

     verify(hotelRepository,times(1)).existsByCnpj(hotelDto.cnpj());
     verify(hotelRepository,never()).save(any(HotelModel.class));
    }

    @Test
    @DisplayName("Buscar hotel pelo id")
    void Test_get_hotel_by_id(){
        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
   
        long id=hotelModel.getId();
        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotelModel));
        hotelDto=hotelServiceIMP.getById(id);

        verify(hotelRepository,times(1)).findById(id);
        assertEquals(hotelModel.getId(), hotelDto.id());
        assertNotNull(hotelDto.cnpj());
    }

    @Test
    @DisplayName("Buscar hotel com id invalido")
    void Test_get_hotel_by_notExists_id(){

   
        long id=99L;
        when(hotelRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
       assertThrows(ResourceNotFoundException.class, ()->hotelServiceIMP.getById(id));

        verify(hotelRepository,times(1)).findById(id);
       
    }

}
