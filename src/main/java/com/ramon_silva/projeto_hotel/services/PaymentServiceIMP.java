package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.Reservation_serviceRepository;
import com.ramon_silva.projeto_hotel.util.Constants;
import com.ramon_silva.projeto_hotel.util.MailConstants;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;

@Service
public class PaymentServiceIMP implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final EmailServiceIMP emailServiceIMP;

    private  PaymentServiceIMP(PaymentRepository paymentRepository,
    ReservationRepository reservationRepository,
    EmailServiceIMP emailServiceIMP,Reservation_serviceRepository reservation_serviceRepository
    ){
        this.paymentRepository=paymentRepository;
        this.reservationRepository=reservationRepository;
        this.emailServiceIMP=emailServiceIMP;
    }


    @Override
    public PaymentDto payment(PaymentDto paymentDto,Long reservation_id) {   
  
            ReservationModel reservationModel= reservationRepository.findById(reservation_id)
            .orElseThrow
            (()->new ResourceNotFoundException("Reserva", "id", reservation_id));
    
            boolean existsReservation=paymentRepository.existsByReservationId(reservationModel.getId());

            if(existsReservation){
                throw new GeralException(Constants.RESERVATION_CONFLICT);
            }

            Double valueService=reservationModel.getReservation_service().stream()
            .mapToDouble(res->res.getServico().getPrice()).sum();
            
            if(reservationModel.getStatus() == StatusEnum.CONFIRM ){
                PaymentModel paymentModel=new PaymentModel();
                paymentModel.setReservation(reservationModel);
                paymentModel.setPaymentMethod(paymentDto.paymentMethod());
                paymentModel.setStatus(StatusEnum.CONFIRM);
                paymentModel.setTotal_payment(reservationModel.getTotal_pay()+valueService);
                PaymentModel result=paymentRepository.save(paymentModel);
                PaymentDto resultDto= new PaymentDto(result);
    
                EmailModel emailModel=new EmailModel();
                emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
                emailModel.setEmailTo(resultDto.reservation().client().email());
                emailModel.setText(MailConstants.MESSAGE_PAYMENT);
                emailModel.setSubject(resultDto.reservation().room().hotel().name());
        
                emailServiceIMP.sendEmail(emailModel, resultDto, MailConstants.PAYMENT);
                return resultDto;
            }
            throw new GeralException(Constants.NOT_CONFIRM_RESERVATION);
        } 
    

    @Override
    public PaymentDto getPaymentById(Long id) { 
       
        PaymentModel payment=paymentRepository.findById(id)
        .orElseThrow(()->
        new ResourceNotFoundException("Pagamento",
         "id", id));
        return new PaymentDto(payment);
      
    
    }

    @Override
    public void deletePaymentById(Long id) {
       
            paymentRepository.findById(id)
            .orElseThrow
            (()->new ResourceNotFoundException("Pagamento", "id", id));
    
            paymentRepository.deleteById(id);
        } 
    


    @Override
    public PaymentDto updateById(Long id, PaymentDto paymentDto) {
   
            PaymentModel paymentModel=paymentRepository.findById(id)
             .orElseThrow(
               ()->new ResourceNotFoundException("Pagamento", "id", id));
               
               ReservationModel reservationModel=reservationRepository.findById(paymentDto.reservation().id()).orElseThrow(
               ()->new ResourceNotFoundException("Reserva", "id", id));

               boolean existsReservation=paymentRepository.existsByReservationIdAndIdNot(reservationModel.getId(), id);

            if(existsReservation){
                throw new GeralException(Constants.RESERVATION_CONFLICT);
            }

               Double valueService= reservationModel.getReservation_service().stream()
               .mapToDouble(res->res.getServico().getPrice()).sum();
               
               if(reservationModel.getStatus() == StatusEnum.CONFIRM){
                 
                   paymentModel.setReservation(reservationModel);
                   paymentModel.setPaymentMethod(paymentDto.paymentMethod());
                   paymentModel.setPayment_day(paymentDto.payment_day());
                   paymentModel.setStatus(StatusEnum.CONFIRM);
                   paymentModel.setTotal_payment(reservationModel.getTotal_pay()+valueService);
                   PaymentModel result=paymentRepository.save(paymentModel);
                   PaymentDto resultDto= new PaymentDto(result);
       
                   EmailModel emailModel=new EmailModel();
                   emailModel.setEmailFrom(MailConstants.BASIC_EMAIL);
                   emailModel.setEmailTo(resultDto.reservation().client().email());
                   emailModel.setText(MailConstants.MESSAGE_PAYMENT);
                   emailModel.setSubject(resultDto.reservation().room().hotel().name());
           
                   emailServiceIMP.sendEmail(emailModel, resultDto, MailConstants.PAYMENT);
                   return resultDto;
               }
               throw new GeralException(Constants.NOT_CONFIRM_RESERVATION);
        } 
      
    


    @Override
    public PageDto<PaymentDto> getAll( int pageNumber, int pageSize, String sortBy,String sortOrder) {
 
            Sort sort=sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
            Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
            Page<PaymentModel> page=paymentRepository.findAll(pageable);
    
            List<PaymentDto> paymentDtos=page.getContent().
            stream()
            .map(PaymentDto::new)
            .collect(Collectors.toList());
            PageDto<PaymentDto> pageDto=new PageDto<>(paymentDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
            page.getTotalPages(), page.getTotalElements());
            return pageDto;
 }

}
