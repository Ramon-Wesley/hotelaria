package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;


public class ReservationCreator {
       private static LocalDate currentDate = LocalDate.now();
       private static LocalDate oneWeekLater = currentDate.plusWeeks(1);
       private static LocalDate oneWeekFiveDaysLater = currentDate.plusWeeks(1).plusDays(5);
       private static long daysDifference = calculateDaysDifference(oneWeekLater, oneWeekFiveDaysLater);  
   
       private static Set<Reservation_serviceModel> reservation_serviceModel = new HashSet<>();

      public static ReservationModel newReservationModel(){
        GuestModel guestModel=GuestCreator.newGuestModel();
        guestModel.setId(1L);
        guestModel.getAddress().setId(1L);
        RoomModel roomModel=RoomCreator.newModelRoom();
        double total_pay=calculateTotalPay(daysDifference, roomModel.getPrice());
        roomModel.setId(1L);
        return new ReservationModel(null, guestModel, roomModel,oneWeekLater, oneWeekFiveDaysLater, StatusEnum.CONFIRM,total_pay,reservation_serviceModel);
    }

    public static ReservationModel newReservationModel2(){
        GuestModel guestModel=GuestCreator.newGuestModel2();
        guestModel.setId(2L);
        guestModel.getAddress().setId(2L);

        RoomModel roomModel=RoomCreator.newModelRoom2();
        roomModel.setId(2L);
        double total_pay=calculateTotalPay(daysDifference, roomModel.getPrice());
        return new ReservationModel(null, guestModel, roomModel,oneWeekLater, oneWeekFiveDaysLater, StatusEnum.PENDING,total_pay ,reservation_serviceModel);
    }

     public static ReservationModel newReservationModel3(){
        GuestModel guestModel=GuestCreator.newGuestModel2();
        guestModel.setId(2L);
        guestModel.getAddress().setId(2L);

        RoomModel roomModel=RoomCreator.newModelRoom();
        roomModel.setId(1L);
        double total_pay=calculateTotalPay(daysDifference, roomModel.getPrice());
        return new ReservationModel(null, guestModel, roomModel,oneWeekLater, oneWeekFiveDaysLater, StatusEnum.PENDING,total_pay ,reservation_serviceModel);
    }
        
        private static long calculateDaysDifference(LocalDate startDate, LocalDate endDate) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
        private static Double calculateTotalPay(long daysDifference, Double price){
            return  daysDifference * price;

       
     
        }
    }

