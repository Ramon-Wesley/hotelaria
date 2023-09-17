package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
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

    @Autowired
    private ModelMapper modelMapper=new ModelMapper();

    @Mock
    private ModelMapper modelMapper2;
    


    private HotelModel hotelModel;
    private HotelDto hotelDto;
    @Test
    @DisplayName("Criar um hotel com sucesso!")
    void Test_create_hotel_sucess(){
     hotelModel=HotelCreator.newModelHotel();
     hotelDto=modelMapper.map(hotelModel,HotelDto.class);
     
     when(hotelRepository.existsByCnpj(hotelDto.getCnpj())).thenReturn(false);
     when(modelMapper2.map(eq(hotelDto), eq(HotelModel.class))).thenReturn(hotelModel);
     hotelModel.setId(1L);
     when(hotelRepository.save(any(HotelModel.class))).thenReturn(hotelModel);
     hotelDto.setId(1L);
     when(modelMapper2.map(eq(hotelModel), eq(HotelDto.class))).thenReturn(hotelDto);

     hotelDto=hotelServiceIMP.create(hotelDto);

     verify(hotelRepository,times(1)).existsByCnpj(hotelDto.getCnpj());
     verify(hotelRepository,times(1)).save(any(HotelModel.class));
    }

    @Test
    @DisplayName("Criar um hotel com cnpj ja cadastrado!")
    void Test_create_hotel_with_cnpj_exists(){
     hotelModel=HotelCreator.newModelHotel();
     hotelDto=modelMapper.map(hotelModel,HotelDto.class);
     hotelModel.setId(1L);

     when(hotelRepository.existsByCnpj(hotelDto.getCnpj())).thenReturn(true);
  

    assertThrows(GeralException.class,()-> hotelServiceIMP.create(hotelDto));

     verify(hotelRepository,times(1)).existsByCnpj(hotelDto.getCnpj());
     verify(hotelRepository,never()).save(any(HotelModel.class));
    }

    @Test
    @DisplayName("Buscar hotel pelo id")
    void Test_get_hotel_by_id(){
        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
        hotelDto=modelMapper.map(hotelModel, HotelDto.class);
        long id=hotelModel.getId();
        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotelModel));
        when(modelMapper2.map(eq(hotelModel), eq(HotelDto.class))).thenReturn(hotelDto);
        hotelDto=hotelServiceIMP.getById(id);

        verify(hotelRepository,times(1)).findById(id);
        assertEquals(hotelModel.getId(), hotelDto.getId());
        assertNotNull(hotelDto.getCnpj());
    }

    @Test
    @DisplayName("Buscar hotel com id invalido")
    void Test_get_hotel_by_notExists_id(){

   
        long id=99L;
        when(hotelRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
       assertThrows(ResourceNotFoundException.class, ()->hotelServiceIMP.getById(id));

        verify(hotelRepository,times(1)).findById(id);
       
    }

    @Test
    @DisplayName("deletar hotel pelo id")
    void Test_delete_hotel_by_id(){
        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
   
        long id=hotelModel.getId();
        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotelModel));
        hotelServiceIMP.deleteById(id);

        verify(hotelRepository,times(1)).findById(id);
        verify(hotelRepository,times(1)).deleteById(id);

    }

    @Test
    @DisplayName("deletar hotel com id invalido")
    void Test_delte_hotel_by_notExists_id(){

   
        long id=99L;
        when(hotelRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
       assertThrows(ResourceNotFoundException.class, ()->hotelServiceIMP.deleteById(id));

        verify(hotelRepository,times(1)).findById(id);
        verify(hotelRepository,times(0)).deleteById(id);
       
    }

    @Test
    @DisplayName("atualizar hotel pelo id")
    void Test_update_hotel_by_id(){
        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
        long id=hotelModel.getId();

        HotelModel hotelModel2=HotelCreator.newModelHotel2();
        hotelModel2.setClassification("1");
        hotelModel2.setId(1L);
        hotelDto=modelMapper.map(hotelModel2,HotelDto.class);


        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotelModel));
        when(hotelRepository.existsByCnpjAndIdNot(hotelDto.getCnpj(), id)).thenReturn(false);
        when(modelMapper2.map(eq(hotelDto),eq(HotelModel.class))).thenReturn(hotelModel2);
        when(hotelRepository.save(any(HotelModel.class))).thenReturn(hotelModel2);
        when(modelMapper2.map(eq(hotelModel2),eq(HotelDto.class))).thenReturn(hotelDto);


        hotelDto=hotelServiceIMP.updateById(id, hotelDto);

        verify(hotelRepository,times(1)).findById(id);
        verify(hotelRepository,times(1)).existsByCnpjAndIdNot(hotelDto.getCnpj(), id);
        verify(hotelRepository,times(1)).save(any(HotelModel.class));
        assertEquals(id, hotelDto.getId());
        assertNotEquals(hotelModel.getClassification(), hotelDto.getClassification());

    }

    @Test
    @DisplayName("atualizar hotel com id invalido")
    void Test_update_hotel_by_notExists_id(){
        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
        hotelDto=modelMapper.map(hotelModel,HotelDto.class);
        long id=99L;

       when(hotelRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
       assertThrows(ResourceNotFoundException.class, ()->hotelServiceIMP.updateById(id,hotelDto));

        verify(hotelRepository,times(1)).findById(id);
        verify(hotelRepository,times(0)).save(hotelModel);
        verify(hotelRepository,times(0)).existsByCnpjAndIdNot(hotelDto.getCnpj(), id);
       
    }

    @Test
    @DisplayName("atualizar hotel com cnpj existente")
    void Test_update_hotel_by_Exists_cnpj(){
        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
        hotelDto=modelMapper.map(hotelModel,HotelDto.class);
        long id=99L;

       when(hotelRepository.findById(id)).thenReturn(Optional.of(hotelModel));
       when(hotelRepository.existsByCnpjAndIdNot(hotelDto.getCnpj(), id)).thenReturn(true);
       assertThrows(GeralException.class , ()-> hotelServiceIMP.updateById(id,hotelDto));

        verify(hotelRepository,times(1)).findById(id);
        verify(hotelRepository,times(1)).existsByCnpjAndIdNot(hotelDto.getCnpj(), id);
        verify(hotelRepository,times(0)).save(hotelModel);
       
    }


    @Test
    @DisplayName("Listar dados vazios")
    void Test_getting_all_empty_hotel() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
       
        List<HotelModel> hotel = Arrays.asList();
  
        Page<HotelModel> page = new PageImpl<>(hotel);
        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(page);
        PageDto<HotelDto> result = hotelServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);
        verify(hotelRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));

        assertEquals(0, result.getTotalElments());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    @DisplayName("Listar dados ")
    void Test_getting_all_hotel() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
        HotelModel hotelModel2=HotelCreator.newModelHotel2();
        hotelModel2.setId(2L);
        
        List<HotelModel> hotel = Arrays.asList(hotelModel,hotelModel2);
  
  
        Page<HotelModel> page = new PageImpl<>(hotel);
        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(page);
        PageDto<HotelDto> result = hotelServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);
        verify(hotelRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));

        assertEquals(2, result.getTotalElments());
        assertEquals(1, result.getTotalPages());
    }


}
