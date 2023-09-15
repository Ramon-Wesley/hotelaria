package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.dto.PaymentMethodDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.Reservation_serviceRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IPaymentService;
import com.ramon_silva.projeto_hotel.util.Constants;
import com.ramon_silva.projeto_hotel.util.MailConstants;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;

@Service
public class PaymentServiceIMP implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final EmailServiceIMP emailServiceIMP;
    private final ModelMapper modelMapper;

    private PaymentServiceIMP(PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            EmailServiceIMP emailServiceIMP, Reservation_serviceRepository reservation_serviceRepository,
            ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.emailServiceIMP = emailServiceIMP;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentDto create(Long reservation_id, PaymentMethodEnum paymentDto) {

        Optional<ReservationModel> reservationModel = reservationRepository.findById(reservation_id);
        if (!reservationModel.isPresent()) {
            throw new ResourceNotFoundException("reservas", "id", reservation_id);
        }

        Optional<PaymentModel> paymentResult = paymentRepository
                .findByReservationId(reservation_id);
        if (paymentResult.isPresent()) {
            throw new GeralException("Essa reserva ja esta paga!");
        }
        Double valueService = 0.0;
        if (!Objects.requireNonNull(reservationModel.get().getReservation_service()).isEmpty()) {
            valueService = reservationModel.get().getReservation_service().stream()
                    .mapToDouble(res -> res.getServico().getPrice()).sum();
        }

        if (reservationModel.get().getStatus() == StatusEnum.CONFIRM) {
            reservationModel.get().setStatus(StatusEnum.PAY);
            PaymentModel paymentModel = new PaymentModel();
            paymentModel.setReservation(reservationModel.get());
            paymentModel.setPaymentMethod(paymentDto);
            paymentModel.setStatus(StatusEnum.CONFIRM);
            paymentModel.setTotal_payment(reservationModel.get().getTotal_pay() + valueService);
            PaymentModel result = paymentRepository.save(paymentModel);
            PaymentDto resultDto = modelMapper.map(result, PaymentDto.class);

            EmailModel emailModel = new EmailModel();
            emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
            emailModel.setEmailTo(resultDto.getReservation().getGuest().getEmail());
            emailModel.setText(MailConstants.MESSAGE_PAYMENT);
            emailModel.setSubject(resultDto.getReservation().getRoom().getHotel().getName());

            emailServiceIMP.sendEmail(emailModel, resultDto, MailConstants.PAYMENT);
            return resultDto;
        }
        throw new GeralException(Constants.NOT_CONFIRM_RESERVATION);
    }

    @Override
    public PaymentDto getById(Long id) {

        PaymentModel payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento",
                        "id", id));
        return modelMapper.map(payment, PaymentDto.class);

    }

    @Override
    public void deleteById(Long id) {

        paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", "id", id));

        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentDto updateById(Long id, PaymentDto paymentDto) {

        PaymentModel paymentModel = paymentRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Pagamento", "id", id));

        ReservationModel reservationModel2 = new ReservationModel();

        if (!paymentModel.getReservation().getId().equals(paymentDto.getReservation().getId())) {
            ReservationModel reservationModel = reservationRepository.findById(paymentDto.getReservation().getId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Reserva", "id", id));

            reservationModel2 = paymentModel.getReservation();

            paymentModel.setReservation(reservationModel);
        }

        boolean existsReservation = paymentRepository
                .existsByReservationIdAndIdNot(paymentModel.getReservation().getId(), id);

        if (existsReservation) {
            throw new GeralException(Constants.RESERVATION_CONFLICT);
        }

        Double valueService = paymentModel.getReservation()
        .getReservation_service().stream()
                .mapToDouble(res -> res.getServico().getPrice()).sum();
        
        if (paymentModel.getReservation().getStatus() == StatusEnum.CONFIRM) {
            paymentModel = modelMapper.map(paymentDto, PaymentModel.class);

            paymentModel.setTotal_payment(paymentModel.getReservation().getTotal_pay() 
            + valueService);
            PaymentModel result = paymentRepository.save(paymentModel);

            if (reservationModel2.getId() != null) {
                reservationModel2.setStatus(StatusEnum.CONFIRM);
                reservationRepository.save(reservationModel2);
            }

            PaymentDto resultDto = modelMapper.map(result, PaymentDto.class);

            EmailModel emailModel = new EmailModel();
            emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
            emailModel.setEmailTo(resultDto.getReservation().getGuest().getEmail());
            emailModel.setText(MailConstants.MESSAGE_PAYMENT);
            emailModel.setSubject(resultDto.getReservation().getRoom().getHotel().getName());

            emailServiceIMP.sendEmail(emailModel, resultDto, MailConstants.PAYMENT);
            return resultDto;
        }
        throw new GeralException(Constants.NOT_CONFIRM_RESERVATION);
    }

    @Override
    public PageDto<PaymentDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<PaymentModel> page = paymentRepository.findAll(pageable);

        List<PaymentDto> paymentDtos = page.getContent().stream()
                .map((e) -> modelMapper.map(e, PaymentDto.class))
                .collect(Collectors.toList());
        PageDto<PaymentDto> pageDto = new PageDto<>(paymentDtos, page.getNumber(), page.getNumberOfElements(),
                page.getSize(),
                page.getTotalPages(), page.getTotalElements());
        return pageDto;
    }

}
