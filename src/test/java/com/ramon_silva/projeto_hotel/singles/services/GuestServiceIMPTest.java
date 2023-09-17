package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.services.GuestServiceIMP;
import com.ramon_silva.projeto_hotel.util.GuestCreator;

@ExtendWith(MockitoExtension.class)
class GuestServiceIMPTest {

    private GuestDto guestDto;
    private GuestModel guestModel;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ModelMapper modelMapper2;

    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private GuestServiceIMP guestServiceIMP;

    @Test
    @DisplayName("Salvar hospede com email ou cpf ja cadastrado")
    void Test_saving_guest_with_existing_email() {
        guestModel = GuestCreator.newGuestModel();

        guestDto = modelMapper.map(guestModel, GuestDto.class);

        when(guestRepository.existsByEmailAndCpf(guestDto.getEmail(), guestDto.getCpf())).thenReturn(true);

        assertThrows(GeralException.class, () -> guestServiceIMP.create(guestDto), "Email ja cadastrado!");

        verify(guestRepository, never()).save(guestModel);
        verify(guestRepository, times(1)).existsByEmailAndCpf(guestDto.getEmail(), guestDto.getCpf());

    }

    @Test
    @DisplayName("Salvar novo hospede!")
    void Test_create_new_guest() {
        guestModel = GuestCreator.newGuestModel();
        guestModel.getAddress().setId(1L);
        guestDto = modelMapper.map(guestModel, GuestDto.class);
        guestModel.setId(1L);

        when(guestRepository.existsByEmailAndCpf(guestDto.getEmail(), guestDto.getCpf())).thenReturn(false);
        when(modelMapper2.map(eq(guestDto), eq(GuestModel.class))).thenReturn(guestModel);
        guestDto.setId(1L);
        when(modelMapper2.map(eq(guestModel), eq(GuestDto.class))).thenReturn(guestDto);
        when(guestRepository.save(any(GuestModel.class))).thenReturn(guestModel);
        GuestDto result = guestServiceIMP.create(guestDto);

        verify(guestRepository, times(1)).existsByEmailAndCpf(guestDto.getEmail(), guestDto.getCpf());

        verify(guestRepository, times(1)).save(Mockito.any(GuestModel.class));

        assertNotNull(result.getId());
        assertNotNull(result.getAddress().getId());

    }

    @Test
    @DisplayName("Salvar dados vazios")
    void Test_creating_guest_with_empty_data() {
        Assertions.assertThrows(NullPointerException.class, () -> guestServiceIMP.create(null),
                "Gueste não pode ser nulo");
        verify(guestRepository, never()).save(any(GuestModel.class));
        verify(guestRepository, never()).existsByEmailAndCpf(anyString(), anyString());

    }

    @Test
    @DisplayName("Deletar o hospede pelo id!")
    void Test_deleting_guest_by_id() {

        guestModel = GuestCreator.newGuestModel();
        guestModel.setId(1L);
        guestModel.getAddress().setId(1L);

        Mockito.when(guestRepository.findById(guestModel.getId()))
                .thenReturn(Optional.of(guestModel));

        guestServiceIMP.deleteById(guestModel.getId());

        verify(guestRepository, times(1)).deleteById(guestModel.getId());
        verify(guestRepository, times(1)).findById(guestModel.getId());
    }

    @Test
    @DisplayName("Deletar o hospede com um id inválido!")
    void Test_deleting_guest_with_invalid_ID() {
        Long id = 99L;
        when(guestRepository.findById(id))
                .thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> guestServiceIMP.deleteById(id),
                "Id inválido");

        verify(guestRepository, times(1)).findById(id);
        verify(guestRepository, never()).deleteById(id);

    }

    @Test
    @DisplayName("Listar todos os hospedes")
    void Test_getting_all_guests() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        GuestModel guest1 = GuestCreator.newGuestModel();
        guest1.setId(1L);
        guest1.getAddress().setId(1L);

        GuestModel guest2 = GuestCreator.newGuestModel();
        guest2.setId(2L);
        guest2.getAddress().setId(2L);

        List<GuestModel> guests = Arrays.asList(guest1, guest2);

        Page<GuestModel> page = new PageImpl<>(guests);

        when(guestRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageDto<GuestDto> result = guestServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);

        verify(guestRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));

        assertEquals(2, result.getTotalElments());
        assertEquals(1, result.getTotalPages());

    }

    @Test
    @DisplayName("Listar dados vazios")
    void Test_getting_all_empty_guests() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        List<GuestModel> guest = Arrays.asList();
        Page<GuestModel> page = new PageImpl<>(guest);
        when(guestRepository.findAll(any(Pageable.class))).thenReturn(page);
        PageDto<GuestDto> result = guestServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);
        verify(guestRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));

        assertEquals(0, result.getTotalElments());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    @DisplayName("Pegar um usuario pelo id")
    void Test_getting_by_id_guest() {

        guestModel = GuestCreator.newGuestModel();
        guestModel.setId(1L);
        guestModel.getAddress().setId(1L);

        guestDto = modelMapper.map(guestModel, GuestDto.class);
        when(guestRepository.findById(guestModel.getId())).thenReturn(Optional.of(guestModel));
        when(modelMapper2.map(eq(guestModel), eq(GuestDto.class))).thenReturn(guestDto);
        GuestDto guestDto2 = guestServiceIMP.getById(guestModel.getId());

        verify(guestRepository, times(1)).findById(guestModel.getId());
        assertEquals(guestModel.getId(), guestDto2.getId(), "Os dados nao sao compativeis");

    }

    @Test
    @DisplayName("Tentar pegar um usuario que nao existe pelo id")
    void Test_getting_by_id_not_found_guest() {

        Long id = 99L;
        when(guestRepository.findById(id)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> guestServiceIMP.getById(id),
                "Gueste nao encontrado!");

        verify(guestRepository, times(1)).findById(id);

    }

    @Test
    @DisplayName("Atualizar registro")
    void Test_update_by_id() {
        guestModel = GuestCreator.newGuestModel();
        guestModel.getAddress().setId(1L);

        guestModel.setId(1L);
        guestDto = modelMapper.map(guestModel, GuestDto.class);

        GuestDto guestDto2 = new GuestDto(guestDto.getId(),
                guestDto.getName(),
                GuestCreator.newGuestModel2().getCpf(),
                GuestCreator.newGuestModel2().getEmail(),
                guestDto.getPhone(),
                guestDto.getAddress(), true);

        GuestModel guestModel2 = modelMapper.map(guestDto2, GuestModel.class);

        when(guestRepository.findById(guestDto.getId())).thenReturn(Optional.of(guestModel));
        when(guestRepository.save(Mockito.any(GuestModel.class))).thenReturn(guestModel2);
        when(modelMapper2.map(eq(guestDto2), eq(GuestModel.class))).thenReturn(guestModel2);
        when(modelMapper2.map(eq(guestModel2), eq(GuestDto.class))).thenReturn(guestDto2);

        GuestDto resulDto = guestServiceIMP.updateById(guestDto.getId(), guestDto2);

        verify(guestRepository, times(1)).findById(guestDto.getId());
        verify(guestRepository, times(1)).save(Mockito.any(GuestModel.class));

        assertNotNull(resulDto);
        assertNotEquals(guestDto, resulDto, "Os dados do hospede são iguais!");
        assertEquals(guestDto.getId(), resulDto.getId(), "O id do hospede nao é igual! ");

    }

    @Test
    @DisplayName("Atualizar registro com id invalido")
    void Test_update_by_invalid_id() {
        Long id = 99L;
        guestModel = GuestCreator.newGuestModel();
        guestModel.setId(1L);
        guestModel.getAddress().setId(1L);

        guestDto = modelMapper.map(guestModel, GuestDto.class);
        when(guestRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> guestServiceIMP.updateById(id, guestDto),
                "Id invalido");

        verify(guestRepository, times(1)).findById(id);
        verify(guestRepository, times(0)).save(Mockito.any(GuestModel.class));

    }

}
