package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.ReservationModel;


public class ReservationCreator {
       private static LocalDate currentDate = LocalDate.now();
       private static LocalDate oneWeekLater = currentDate.plusWeeks(1);
       private static LocalDate oneWeekFiveDaysLater = currentDate.plusWeeks(1).plusDays(5);
       private static long daysDifference = calculateDaysDifference(oneWeekLater, oneWeekFiveDaysLater);  
       private static double total_pay=daysDifference * RoomCreator.updateModelRoom().getPrice();

       
     
      public static ReservationModel ReservationModelUpdateConfirm(){
        return new ReservationModel(1L, ClientCreator.updateModelClient(), RoomCreator.updateModelRoom(),oneWeekLater, oneWeekFiveDaysLater, StatusEnum.CONFIRM,total_pay ,Reservation_serviceCreator.getModelReservation_service());
    }

        
        private static long calculateDaysDifference(LocalDate startDate, LocalDate endDate) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
    }

