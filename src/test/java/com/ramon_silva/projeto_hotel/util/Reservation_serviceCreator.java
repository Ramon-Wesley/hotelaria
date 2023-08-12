package com.ramon_silva.projeto_hotel.util;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;


public class Reservation_serviceCreator {
    
 public static Set<Reservation_serviceModel> getModelReservation_service() {
         Set<Reservation_serviceModel> services=new HashSet<>();
         services.add(newReservation_serviceModel());
         services.add(newReservation_serviceModel2());
         return services;
     }

       public static Reservation_serviceModel newReservation_serviceModel(){
        ServicesModel servicesModel=ServiceCreator.newServiceModel();
        servicesModel.setId(1L);
        return new Reservation_serviceModel(1L, null, servicesModel, LocalDateTime.now().plusWeeks(1).plusDays(1));
    }

     public static Reservation_serviceModel newReservation_serviceModel2(){
        ServicesModel servicesModel=ServiceCreator.newServiceModel2();
        servicesModel.setId(2L);
        return new Reservation_serviceModel(2L, null, servicesModel, LocalDateTime.now().plusWeeks(1).plusDays(2));
    }
}
