package com.ramon_silva.projeto_hotel.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.Reservation_serviceRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.repositories.ServicesRepository;
import com.ramon_silva.projeto_hotel.util.MailConstants;


import com.ramon_silva.projeto_hotel.enums.StatusEnum;

@Service
public class ReservationServiceIMP implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ServicesRepository servicesRepository;
    private final Reservation_serviceRepository reservation_serviceRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceIMP emailServiceIMP;

    
    public  ReservationServiceIMP(
    ReservationRepository reservationRepository,
    GuestRepository guestRepository,RoomRepository roomRepository,
    ServicesRepository servicesRepository,
    Reservation_serviceRepository reservation_serviceRepository,
    EmailServiceIMP emailServiceIMP,
    ModelMapper modelMapper){
        this.reservationRepository=reservationRepository;
        this.guestRepository= guestRepository;
        this.roomRepository=roomRepository;
        this.servicesRepository=servicesRepository;
        this.reservation_serviceRepository=reservation_serviceRepository;
        this.emailServiceIMP=emailServiceIMP;
        this.modelMapper=modelMapper;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto){
          GuestModel guestModel= guestRepository.findById(reservationDto.getGuest().getId())
           .orElseThrow(()->new ResourceNotFoundException("hospede", "id", reservationDto.getGuest().getId()));
           
           RoomModel roomModel=roomRepository.findById(reservationDto.getRoom().getId())
           .orElseThrow(()->new ResourceNotFoundException("quarto", "id",reservationDto.getRoom().getId()));  
           
           boolean result=reservationRepository.hasConflictingReservations(roomModel.getId(),reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
           if(result){
            throw new GeralException("Datas incompativeis");
           }
           
           ReservationModel reservationModel=modelMapper
           .map(reservationDto,ReservationModel.class);

           reservationModel.setTotal_pay(totalPrice(reservationModel.getCheckInDate(), reservationModel.getCheckOutDate(), reservationModel.getRoom().getPrice()));
           ReservationModel resultModel=reservationRepository.save(reservationModel);
           ReservationDto resultDto=modelMapper.map(resultModel,ReservationDto.class);
             
               EmailModel email=new EmailModel();
               email.setEmailFrom(MailConstants.BASIC_EMAIL);
               email.setEmailTo(resultDto.getGuest().getEmail());
               email.setSubject(resultDto.getRoom().getHotel().getName());
               email.setText(MailConstants.MESSAGE_RESERVATION);
               emailServiceIMP.sendEmail(email,resultDto,MailConstants.RESERVATION);
               
               return resultDto;
              }



              
              @Override
              public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
 
              ReservationModel reservationModel=reservationRepository.findById(id)
              .orElseThrow(()->new ResourceNotFoundException("Reserva", 
               "id", id));
            
              guestRepository.findById(reservationDto.getGuest().getId())
              .orElseThrow(()->new ResourceNotFoundException("hospede", "id", reservationDto.getGuest().getId()));
           
             RoomModel roomModel=roomRepository.findById(reservationDto.getRoom().getId())
               .orElseThrow(()->new ResourceNotFoundException("quarto", "id",reservationDto.getRoom().getId()));  
           

           boolean result=reservationRepository.hasConflictingReservationsDatesWithIdNotEquals(id,roomModel.getId(),reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
           if(!result){
            throw new GeralException("Datas conflitantes!");
           }
           reservationDto.setId(id);          
           reservationModel=modelMapper.map(reservationDto,ReservationModel.class);
           reservationModel.setTotal_pay(totalPrice(reservationModel.getCheckInDate(), reservationModel.getCheckOutDate(), reservationModel.getRoom().getPrice()));
           
           ReservationModel resultModel=reservationRepository.save(reservationModel);
           ReservationDto resultDto=modelMapper.map(resultModel, ReservationDto.class);
             
               EmailModel email=new EmailModel();
               email.setEmailFrom(MailConstants.BASIC_EMAIL);
               email.setEmailTo(resultDto.getGuest().getEmail());
               email.setSubject(resultDto.getRoom().getHotel().getName());
               email.setText(MailConstants.MESSAGE_RESERVATION);
               emailServiceIMP.sendEmail(email,resultDto,MailConstants.RESERVATION);
               
               return resultDto;
          }
          
          
          @Override
          public ReservationDto getReservationById(Long id) {
            
            ReservationModel reservation=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reserva", "id", id));
            return modelMapper.map(reservation,ReservationDto.class);
          }

          
          @Override
          public PageDto<ReservationDto> getAllReservation(int pageNumber, int pageSize, String sortBy, String sortOrder) {
            Sort sort=sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
            Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
            Page<ReservationModel> page=reservationRepository.findAll(pageable);
            List<ReservationDto> reservationDtos=page.getContent().stream()
            .map((e)->modelMapper.map(e,ReservationDto.class)).collect(Collectors.toList());
            PageDto<ReservationDto> result=new PageDto<>(reservationDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
            page.getTotalPages(), page.getTotalElements());
            return result;
          }

          @Override
          public Set<Reservation_serviceDto> getAllServicesReservation(Long reservation_id)
              {
                  ReservationModel reservationModel=reservationRepository.findById(reservation_id)
      .orElseThrow(()->
       new ResourceNotFoundException("reservas", "id", reservation_id));

       Set<Reservation_serviceModel> reservation_serviceModel=reservationModel.getReservation_service();
       Set<Reservation_serviceDto> reservation_serviceDtos=new HashSet<>();
       if(Objects.requireNonNull(reservation_serviceModel).isEmpty()){
        throw new ResourceNotFoundException("reservas_servicos", "id", reservation_id);
      }
      reservation_serviceModel.stream().forEach((reservation_service)->reservation_serviceDtos.add(modelMapper.map(reservation_service
      ,Reservation_serviceDto.class)));

      return reservation_serviceDtos;
     
                }
                
                @Override
                public PageDto<Reservation_serviceDto> getAllServicesReservations(int pageNumber, int pageSize, String sortBy, String sortOrder) {
                  Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
                  Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
                  Page<Reservation_serviceModel> page=reservation_serviceRepository.findAll(pageable);
                  Set<Reservation_serviceDto> result=page.getContent().stream().map((e)->modelMapper.map(e,Reservation_serviceDto.class)).collect(Collectors.toSet());
                  PageDto<Reservation_serviceDto> pageResult=new PageDto<>(result,page.getNumber(), page.getNumberOfElements(), page.getSize(),
                  page.getTotalPages(), page.getTotalElements());
                  return pageResult;
                
                }
              
              @Override
              public void addServices(Long reservation_id,List<ServicesDto> servicesDto ) {

                ReservationModel reservationModel=reservationRepository.findById(reservation_id)
                .orElseThrow(()->new ResourceNotFoundException("reserva", "id", reservation_id));
               
                if(reservationModel.getStatus()!= StatusEnum.CONFIRM){
                    throw new GeralException("Confirme a reserva para adicionar servicos! ");
                }

                List<Long> id_services=servicesDto.stream()
                .map(((e)->e.getId())).collect(Collectors.toList());

               List<ServicesModel> servicesModel=servicesRepository
               .findAllById(id_services);

                if(servicesModel.size() != id_services.size()){
                  throw new GeralException("servicos inexistentes! ");
                }          

                Set<Reservation_serviceModel> reservation_serviceModel=new HashSet<>();
                   servicesModel.stream().forEach((services)->reservation_serviceModel.add(
                    new Reservation_serviceModel( reservationModel, services)
                   ));                     
    
                reservationModel.setReservation_service(reservation_serviceModel);
               
                reservationRepository.save(reservationModel);
    }

    @Override
    public Reservation_serviceDto getServiceReservationById(Long id_reservation, Long serviceReservation_id) {
      ReservationModel reservationModel=reservationRepository.findById(id_reservation)
      .orElseThrow(()->
       new ResourceNotFoundException("reservas", "id", id_reservation));
     
     Reservation_serviceModel reservation_serviceModel =  reservationModel.getReservation_service().stream()
      .filter(value->value.getId() == serviceReservation_id)
      .findFirst().orElseThrow(()->new ResourceNotFoundException("Reserva_servico", "id", serviceReservation_id));
      
     
     return modelMapper.map(reservation_serviceModel,Reservation_serviceDto.class);
     
    }
    
    @Override
    public void removeServices(Long reservation_id,Long service_id) {
      ReservationModel reservationModel=reservationRepository
      .findById(reservation_id)
      .orElseThrow(()->new ResourceNotFoundException("reserva", "id", reservation_id));
     if(Objects.requireNonNull(reservationModel.getReservation_service()).isEmpty()){
        throw new ResourceNotFoundException("reservation_services","id!",service_id);
      }
      Reservation_serviceModel service=reservationModel
      .getReservation_service().stream()
      .filter(value->value.getId() == service_id)
      .findFirst().orElseThrow(()->new ResourceNotFoundException("reservation_service", "id", service_id));
      
      reservationModel.getReservation_service().remove(service);
      reservationRepository.save(reservationModel);
    }
    
      
      
      private static double totalPrice(LocalDate date1, LocalDate date2,double price){
        long days= ChronoUnit.DAYS.between(date1, date2);
        double priceReservation=price*days;
        return priceReservation;
      }

      public boolean existsServices(List<Long> servicesDto_id,Long servicesModel){
            return servicesDto_id.stream()
            .anyMatch((e)->e.compareTo(servicesModel) == 0);

}
      }
    
  
