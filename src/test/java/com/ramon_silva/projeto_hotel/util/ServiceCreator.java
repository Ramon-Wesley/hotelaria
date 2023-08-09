package com.ramon_silva.projeto_hotel.util;

import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;

public class ServiceCreator {
    
  
    


    public static ServicesModel updateModelService(){

        return new ServicesModel(1L, "alimenticio", "Servico de quarto", 30.00);
    }

     public static ServicesModel updateModelService2(){

        return new ServicesModel(2L, "Geral", "Servico de hospedagem", 70.00);
    }
}
