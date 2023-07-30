package com.ramon_silva.projeto_hotel.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;
import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.Reservation_serviceRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.repositories.ServicesRepository;
import com.ramon_silva.projeto_hotel.util.Constants;
import com.ramon_silva.projeto_hotel.util.MessageMailConstants;

import jakarta.mail.MessagingException;

import com.ramon_silva.projeto_hotel.enums.StatusEnum;

@Service
public class ReservationServiceIMP implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final ServicesRepository servicesRepository;
    private final Reservation_serviceRepository reservation_serviceRepository;
    private final EmailServiceIMP emailServiceIMP;

    public  ReservationServiceIMP(ReservationRepository reservationRepository,
    ClientRepository clientRepository,RoomRepository roomRepository,
    ServicesRepository servicesRepository,
    Reservation_serviceRepository reservation_serviceRepository,
    EmailServiceIMP emailServiceIMP){
        this.reservationRepository=reservationRepository;
        this.clientRepository= clientRepository;
        this.roomRepository=roomRepository;
        this.servicesRepository=servicesRepository;
        this.reservation_serviceRepository=reservation_serviceRepository;
        this.emailServiceIMP=emailServiceIMP;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto,Long client_id,Long room_id) throws MessagingException, DocumentException {
           ClientModel clientModel=clientRepository.findById(client_id)
           .orElseThrow(()->new ResourceNotFoundException("Cliente", "id", client_id));
           RoomModel roomModel=roomRepository.findById(room_id).orElseThrow(()->new ResourceNotFoundException("quarto", "id", room_id));  
           ReservationModel reservationModel=new ReservationModel(null,reservationDto);
           reservationModel.setRoom(roomModel);
           reservationModel.setClient(clientModel);
           reservationModel.setStatus(StatusEnum.PENDING);
           reservationModel.setTotal_pay(totalPrice(reservationModel.getCheckInDate(), reservationModel.getCheckOutDate(), reservationModel.getRoom().getPrice()));
           ReservationModel resultModel=reservationRepository.save(reservationModel);
           ReservationDto resultDto=new ReservationDto(resultModel);
           if(resultDto != null){       
               EmailModel email=new EmailModel();
               email.setEmailFrom(MessageMailConstants.BASIC_EMAIL);
               email.setEmailTo(resultDto.client().email());
               email.setSubject("a");
               email.setText(MessageMailConstants.MESSAGE_RESERVATION);
               emailServiceIMP.sendEmail(email,resultDto,MessageMailConstants.RESERVATION);
      
           }
           return resultDto;
          }

  

    @Override
    public ReservationDto cancelReservation(Long id) {
      ReservationModel reservationModel=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reservas","id",id));
      if(reservationModel.getStatus().equals(StatusEnum.PENDING)){
        reservationModel.setStatus(StatusEnum.CANCELED);
        reservationRepository.save(reservationModel);
        return new ReservationDto(reservationModel);
      }
      throw new GeralException(Constants.STATUS_RESERVATION_ERROR);
    }


    @Override
    public ReservationDto confirmReservation(Long id) {
         ReservationModel reservationModel=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reservas","id",id));
      if(reservationModel.getStatus().equals(StatusEnum.PENDING)){
        reservationModel.setStatus(StatusEnum.CONFIRM);
        reservationRepository.save(reservationModel);
        return new ReservationDto(reservationModel);
      }
      throw new GeralException(Constants.STATUS_RESERVATION_ERROR);
    }


    @Override
    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        ReservationModel reservationModel=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reservas","id",id));
      if(reservationModel.getStatus().equals(StatusEnum.PENDING)){
        reservationModel.setTotal_pay(ReservationServiceIMP.totalPrice(reservationModel.getCheckInDate(),reservationModel.getCheckOutDate(), reservationModel.getRoom().getPrice()));
        reservationRepository.save(reservationModel);
        return new ReservationDto(reservationModel);
      }
       throw new GeralException(Constants.CONFLICT_DATE_RESERVATION);
    }


    @Override
    public ReservationDto getReservationById(Long id) {
      
    ReservationModel reservation=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reserva", "id", id));
    return new ReservationDto(reservation);
    }

    @Override
    public PageDto<ReservationDto> getAllReservation(int pageNumber, int pageSize, String sortBy, String sortOrder) {
       Sort sort=sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
       Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
       Page<ReservationModel> page=reservationRepository.findAll(pageable);
       List<ReservationDto> reservationDtos=page.getContent().stream().map(ReservationDto::new).collect(Collectors.toList());
       PageDto<ReservationDto> result=new PageDto<>(reservationDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
       page.getTotalPages(), page.getTotalElements());
       return result;
    }

    
    @Override
    public void addServices(Long reservation_id, Long service_id) {
      ReservationModel reservationModel=reservationRepository.findById(reservation_id).orElseThrow(()->new ResourceNotFoundException("reserva", "id", reservation_id));
      ServicesModel servicesModel=servicesRepository.findById(service_id).orElseThrow(()->new ResourceNotFoundException("servico", "id", service_id));
      Reservation_serviceModel reservation_serviceModel=new Reservation_serviceModel();
      reservation_serviceModel.setReservation(reservationModel);
      reservation_serviceModel.setServico(servicesModel);
      reservationModel.getReservation_service().add(reservation_serviceModel);
      reservationRepository.save(reservationModel);
    }
    
    @Override
    public void removeServices(Long reservation_id,Long service_id) {
      ReservationModel reservationModel=reservationRepository.findById(reservation_id).orElseThrow(()->new ResourceNotFoundException("reserva", "id", reservation_id));
      Reservation_serviceModel reservation_serviceModel=reservationModel.getReservation_service().stream()
      .filter(value->value.getId() == service_id)
      .findFirst().orElseThrow(()->new ResourceNotFoundException("Service", "id", service_id));

      reservationModel.getReservation_service().remove(reservation_serviceModel);
      reservationRepository.save(reservationModel);
    }

    @Override
    public PageDto<Reservation_serviceDto> getAllServicesReservation(Long reservation_id,int pageNumber,int pageSize,String sortBy,String sortOrder)
    {
      Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
      Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
      Page<Reservation_serviceModel> page=reservation_serviceRepository.findAllByReservationId(reservation_id, pageable);
      Set<Reservation_serviceDto> result=page.getContent().stream().map(Reservation_serviceDto::new).collect(Collectors.toSet());
      PageDto<Reservation_serviceDto> pageResult=new PageDto<>(result,page.getNumber(), page.getNumberOfElements(), page.getSize(),
      page.getTotalPages(), page.getTotalElements());
      return pageResult;
       }

    @Override
    public PageDto<Reservation_serviceDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {
      Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
      Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
      Page<Reservation_serviceModel> page=reservation_serviceRepository.findAll(pageable);
      Set<Reservation_serviceDto> result=page.getContent().stream().map(Reservation_serviceDto::new).collect(Collectors.toSet());
      PageDto<Reservation_serviceDto> pageResult=new PageDto<>(result,page.getNumber(), page.getNumberOfElements(), page.getSize(),
      page.getTotalPages(), page.getTotalElements());
      return pageResult;
    
    }

    public static double totalPrice(LocalDate date1, LocalDate date2,double price){
           long days= ChronoUnit.DAYS.between(date1, date2);
           double priceReservation=price*days;
           return priceReservation;
    }
    
  }
