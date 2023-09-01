package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;


public class ReservationCreator {
       private static LocalDate currentDate = LocalDate.now();
       private static LocalDate oneWeekLater = currentDate.plusWeeks(1);
       private static LocalDate oneWeekFiveDaysLater = currentDate.plusWeeks(1).plusDays(5);
       private static long daysDifference = calculateDaysDifference(oneWeekLater, oneWeekFiveDaysLater);  
   
     
      public static ReservationModel newReservationModel(){
        ClientModel clientModel=ClientCreator.newClientModel();
        clientModel.setId(1L);
        clientModel.getAddress().setId(1L);
        RoomModel roomModel=RoomCreator.newModelRoom();
        double total_pay=calculateTotalPay(daysDifference, roomModel.getPrice());
        roomModel.setId(1L);
        return new ReservationModel(null, clientModel, roomModel,oneWeekLater, oneWeekFiveDaysLater, StatusEnum.CONFIRM,total_pay,null);
    }

    public static ReservationModel newReservationModel2(){
        ClientModel clientModel=ClientCreator.newClientModel2();
        clientModel.setId(2L);
        clientModel.getAddress().setId(2L);

        RoomModel roomModel=RoomCreator.newModelRoom2();
        roomModel.setId(2L);
        double total_pay=calculateTotalPay(daysDifference, roomModel.getPrice());
        return new ReservationModel(null, clientModel, roomModel,oneWeekLater, oneWeekFiveDaysLater, StatusEnum.PENDING,total_pay ,null);
    }

     public static ReservationModel newReservationModel3(){
        ClientModel clientModel=ClientCreator.newClientModel2();
        clientModel.setId(2L);
        clientModel.getAddress().setId(2L);

        RoomModel roomModel=RoomCreator.newModelRoom();
        roomModel.setId(1L);
        double total_pay=calculateTotalPay(daysDifference, roomModel.getPrice());
        return new ReservationModel(null, clientModel, roomModel,oneWeekLater, oneWeekFiveDaysLater, StatusEnum.PENDING,total_pay ,null);
    }
        
        private static long calculateDaysDifference(LocalDate startDate, LocalDate endDate) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
        private static Double calculateTotalPay(long daysDifference, Double price){
            return  daysDifference * price;

       
     
        }
    }

