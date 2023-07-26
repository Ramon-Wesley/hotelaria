package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;

public class PaymentServiceIMP implements PaymentServce{

    private PaymentRepository paymentRepository;
    private ReservationRepository reservationRepository;

    public  PaymentServiceIMP(PaymentRepository paymentRepository,ReservationRepository reservationRepository){
        this.paymentRepository=paymentRepository;
        this.reservationRepository=reservationRepository;
    }


    @Override
    public PaymentDto payment(PaymentDto paymentDto) {
        Long idReservation=paymentDto.reservation().id();
        reservationRepository.findById(idReservation)
        .orElseThrow(()->new ResourceNotFoundException("Reserva", "id", idReservation));
        PaymentModel paymentModel=paymentRepository.save(new PaymentModel());
        return new PaymentDto(paymentModel);
        }

    @Override
    public PaymentDto getPaymentById(Long id) {
      
    PaymentModel payment=paymentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Pagamento", "id", id));

    return new PaymentDto(payment);
    
    }

   

    @Override
    public PageDto<PaymentDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {
     
        Sort sort=sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
        Page<PaymentModel> page=paymentRepository.findAll(pageable);

        List<PaymentDto> paymentDtos=page.getContent().stream().map(PaymentDto::new).collect(Collectors.toList());
        PageDto<PaymentDto> pageDto=new PageDto<>(paymentDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
        page.getTotalPages(), page.getTotalElements());
        return pageDto;

}
}
