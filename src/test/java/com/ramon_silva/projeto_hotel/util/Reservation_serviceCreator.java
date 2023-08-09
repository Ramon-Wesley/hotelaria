package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;


public class Reservation_serviceCreator {
    
 public static Set<Reservation_serviceModel> getModelReservation_service() {
         Set<Reservation_serviceModel> services=new HashSet<>();
         services.add(updateModelReservation_service());
         services.add(updateModelReservation_service2());
         return services;
     }

       public static Reservation_serviceModel updateModelReservation_service(){

        return new Reservation_serviceModel(1L, null, ServiceCreator.updateModelService(), LocalDateTime.now().plusWeeks(1).plusDays(1));
    }

     public static Reservation_serviceModel updateModelReservation_service2(){

        return new Reservation_serviceModel(1L, null, ServiceCreator.updateModelService2(), LocalDateTime.now().plusWeeks(1).plusDays(2));
    }
}
