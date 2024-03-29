package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.services.EmailServiceIMP;
import com.ramon_silva.projeto_hotel.services.PaymentServiceIMP;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.EmailModel;
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
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EmailServiceIMP emailServiceIMP;

    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private ModelMapper modelMapper2;

    @Test
    @DisplayName("Sucesso no pagamento da estadia")
    void Test_payment_success() {
        ReservationModel reservationModel = ReservationCreator.newReservationModel();
        reservationModel.setId(1L);

        PaymentCreator paymentCreator = PaymentCreator.createModelPayment();

        paymentCreator.getPaymentModel().setId(1L);

        PaymentDto paymentDto = new PaymentDto(null,
                modelMapper.map(reservationModel, ReservationDto.class),
                paymentCreator.getPaymentModel().getPaymentMethod(),
                paymentCreator.getPaymentModel().getPayment_day(), null, 0.0);

        PaymentDto paymentDto2 = modelMapper.map(paymentCreator.getPaymentModel(), PaymentDto.class);
        when(paymentRepository.findByReservationId(reservationModel.getId())).thenReturn(Optional.empty());
        when(reservationRepository.findById(reservationModel.getId())).thenReturn(Optional.of(reservationModel));
        when(paymentRepository.save(any(PaymentModel.class))).thenReturn(paymentCreator.getPaymentModel());
        when(modelMapper2.map(eq(paymentCreator.getPaymentModel()), eq(PaymentDto.class))).thenReturn(paymentDto2);

        PaymentDto resulDto = paymentServiceIMP.create(reservationModel.getId(),
                paymentCreator.getPaymentModel().getPaymentMethod());

        verify(reservationRepository, times(1))
                .findById(reservationModel.getId());

        verify(paymentRepository, times(1))
                .findByReservationId(any());

        verify(paymentRepository, times(1))
                .save(any(PaymentModel.class));

        verify(emailServiceIMP, times(1))
                .sendEmail(
                        any(EmailModel.class),
                        any(PaymentDto.class),
                        any(String.class));

        assertEquals(resulDto.getId(), paymentCreator.getPaymentModel().getId());
        assertEquals(resulDto.getReservation().getId(), paymentCreator.getPaymentModel().getReservation().getId());
        assertEquals(resulDto.getStatus(), StatusEnum.CONFIRM);
    }

    @Test
    @DisplayName("Tentar realizar o pagamento com um reserva ja paga!")
    void Test_payment_error_invalid_reservation() {
        PaymentCreator paymentCreator = PaymentCreator.createModelPayment();
        PaymentDto paymentDto = modelMapper.map(paymentCreator.getPaymentModel(), PaymentDto.class);
        paymentCreator.getPaymentModel().setId(1L);
        long reservation_id = paymentDto.getReservation().getId();

        when(paymentRepository.findByReservationId(reservation_id))
                .thenReturn(Optional.of(paymentCreator.getPaymentModel()));

        assertThrows(GeralException.class,
                () -> paymentServiceIMP.create(reservation_id, paymentDto.getPaymentMethod()));

        verify(paymentRepository, times(1))
                .findByReservationId(reservation_id);

        verify(paymentRepository, never())
                .save(any(PaymentModel.class));

        verify(emailServiceIMP, never())
                .sendEmail(
                        any(EmailModel.class),
                        any(PaymentDto.class),
                        any(String.class));
    }

    @Test
    @DisplayName("Tentar fazer o pagamento sem a confirmacao da reserva")
    void test_payment_error_status_pending_or_status_canceled() {

        ReservationModel reservationModel = ReservationCreator.newReservationModel2();
        reservationModel.setId(1L);
        PaymentCreator paymentCreator = PaymentCreator.createModelPayment();

        PaymentDto paymentDto = new PaymentDto(null, modelMapper.map(reservationModel, ReservationDto.class),
                paymentCreator.getPaymentModel().getPaymentMethod(),
                paymentCreator.getPaymentModel().getPayment_day(), null, 0.0);

        when(reservationRepository.findById(reservationModel.getId())).thenReturn(Optional.of(reservationModel));

        assertThrows(GeralException.class,
                () -> paymentServiceIMP.create(reservationModel.getId(), paymentDto.getPaymentMethod()));

        verify(reservationRepository, times(1))
                .findById(reservationModel.getId());

        verify(emailServiceIMP, never())
                .sendEmail(
                        paymentCreator.getEmailReturn().getEmailModel(),
                        paymentCreator.getEmailReturn().getPaymentDto(),
                        paymentCreator.getEmailReturn().getMailConstants());

        verify(paymentRepository, never())
                .save(paymentCreator.getPaymentModel());

    }

    @Test
    @DisplayName("Tentar fazer o pagamento com id do reserva invalido")
    void test_payment_error_with_id_reservation_invalid() {

        Long reservation_id = 99l;
        PaymentCreator paymentCreator = PaymentCreator.createModelPayment();

        PaymentDto paymentDto = new PaymentDto(null,
                modelMapper.map(ReservationCreator.newReservationModel2(), ReservationDto.class),
                paymentCreator.getPaymentModel().getPaymentMethod(),
                paymentCreator.getPaymentModel().getPayment_day(), null, 0.0);
        paymentDto.getReservation().setId(reservation_id);
        when(reservationRepository.findById(reservation_id)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentServiceIMP.create(reservation_id, paymentDto.getPaymentMethod()));

        verify(reservationRepository, times(1))
                .findById(reservation_id);

        verify(emailServiceIMP, never())
                .sendEmail(
                        any(EmailModel.class),
                        any(PaymentDto.class),
                        any(String.class));

        verify(paymentRepository, never())
                .save(any(PaymentModel.class));

    }

    @Test
    @DisplayName("Atualizar o registro de pagamento modificando o modo de pagamento!")
    void Test_update_by_id_payment_success() {
        PaymentModel paymentCreator = PaymentCreator.createModelPayment().getPaymentModel();
        paymentCreator.setId(1L);
        paymentCreator.getReservation().setId(1L);

        System.out.println(paymentCreator.getReservation().getId());

        PaymentModel paymentCreator2 = PaymentCreator.createModelPayment2().getPaymentModel();
        paymentCreator2.setId(paymentCreator.getId());
        paymentCreator2.setReservation(paymentCreator.getReservation());
        paymentCreator2.getReservation().setStatus(StatusEnum.CONFIRM);

        PaymentDto paymentCreatorDto = modelMapper.map(paymentCreator2, PaymentDto.class);

        when(paymentRepository.findById(paymentCreator.getId())).thenReturn(Optional.of(paymentCreator));
        when(paymentRepository.save(any(PaymentModel.class))).thenReturn(paymentCreator2);
        when(modelMapper2.map(eq(paymentCreatorDto), eq(PaymentModel.class))).thenReturn(paymentCreator2);
        when(modelMapper2.map(eq(paymentCreator2), eq(PaymentDto.class))).thenReturn(paymentCreatorDto);

        PaymentDto pay = paymentServiceIMP.updateById(paymentCreatorDto.getId(), paymentCreatorDto);

        verify(paymentRepository, times(1)).findById(paymentCreatorDto.getId());
        verify(paymentRepository, times(1)).save(any(PaymentModel.class));

        assertEquals(pay.getId(), paymentCreator.getId());
        assertEquals(pay.getReservation().getId(), paymentCreator.getReservation().getId());
        assertNotEquals(paymentCreator2.getPaymentMethod(), paymentCreator.getPaymentMethod());
    }

    @Test
    @DisplayName("Atualizar o registro de pagamento com id inexistente!")
    void Test_update_by_id_payment_error() {
        Long id = 99L;
        PaymentModel paymentCreator = PaymentCreator.createModelPayment().getPaymentModel();
        paymentCreator.setId(1L);
        PaymentModel paymentCreator2 = PaymentCreator.createModelPayment2().getPaymentModel();
        paymentCreator2.setId(paymentCreator.getId());
        PaymentDto paymentCreatorDto = modelMapper.map(paymentCreator2, PaymentDto.class);

        when(paymentRepository.findById(id)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> paymentServiceIMP.updateById(id, paymentCreatorDto));

        verify(paymentRepository, times(1)).findById(id);
        verify(reservationRepository, never()).findById(any(Long.class));
        verify(paymentRepository, never()).save(any(PaymentModel.class));

    }

    @Test
    @DisplayName("Selecionar o pagamento pelo id")
    void Test_get_payment_by_id_success() {
        PaymentModel paymentCreator = PaymentCreator.createModelPayment().getPaymentModel();
        paymentCreator.setId(1L);
        PaymentDto paymentDto = modelMapper.map(paymentCreator, PaymentDto.class);
        when(paymentRepository.findById(paymentCreator.getId())).thenReturn(Optional.of(paymentCreator));
        when(modelMapper2.map(eq(paymentCreator), eq(PaymentDto.class))).thenReturn(paymentDto);
        PaymentDto paymentDtoResult = paymentServiceIMP.getById(paymentCreator.getId());

        verify(paymentRepository, times(1)).findById(paymentCreator.getId());

        assertEquals(paymentDtoResult.getId(), paymentCreator.getId());
        assertEquals(paymentDtoResult.getReservation().getId(), paymentCreator.getReservation().getId());
        assertEquals(paymentDtoResult.getTotal_payment(), paymentCreator.getTotal_payment());
    }

    @Test
    @DisplayName("Selecionar o pagamento com um id inexistente")
    void Test_get_payment_by_nonexiste_id() {
        Long id = 99L;

        when(paymentRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> paymentServiceIMP.getById(id));
        verify(paymentRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Selecionar todos os pagamentos")
    void Test_get_all_payments() {
        PaymentModel paymentCreator = PaymentCreator.createModelPayment().getPaymentModel();
        paymentCreator.setId(1L);
        PaymentModel paymentCreator2 = PaymentCreator.createModelPayment2().getPaymentModel();
        paymentCreator2.setId(2L);
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        List<PaymentModel> paymentModels = new ArrayList<>();
        paymentModels.add(paymentCreator);
        paymentModels.add(paymentCreator2);

        Page<PaymentModel> pay = new PageImpl<>(paymentModels);
        when(paymentRepository.findAll(any(Pageable.class))).thenReturn(pay);

        PageDto<PaymentDto> page = paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);

        verify(paymentRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));
        assertEquals(2, page.getTotalElments());
        assertEquals(1, page.getTotalPages());

    }

    @Test
    @DisplayName("Selecionar todos os pagamentos com lista vazia")
    void Test_get_all_payments_empty() {

        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        List<PaymentModel> paymentModels = new ArrayList<>();

        Page<PaymentModel> pay = new PageImpl<>(paymentModels);
        when(paymentRepository.findAll(any(Pageable.class))).thenReturn(pay);

        PageDto<PaymentDto> page = paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);

        verify(paymentRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));
        assertEquals(0, page.getTotalElments());
        assertEquals(1, page.getTotalPages());
    }

    @Test
    @DisplayName("deletar o pagamento com um id inexistente")
    void Test_delete_payment_by_nonexiste_id() {
        Long id = 99L;

        when(paymentRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> paymentServiceIMP.deleteById(id));

        verify(paymentRepository, times(1)).findById(id);
        verify(paymentRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("deletar o pagamento pelo id")
    void Test_delete_payment_by_id() {
        PaymentModel paymentCreator = PaymentCreator.createModelPayment().getPaymentModel();

        when(paymentRepository.findById(paymentCreator.getId())).thenReturn(Optional.of(paymentCreator));

        paymentServiceIMP.deleteById(paymentCreator.getId());

        verify(paymentRepository, times(1)).findById(paymentCreator.getId());
        verify(paymentRepository, times(1)).deleteById(paymentCreator.getId());
    }
}
