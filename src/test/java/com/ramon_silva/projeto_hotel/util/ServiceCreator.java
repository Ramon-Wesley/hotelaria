package com.ramon_silva.projeto_hotel.util;




import com.ramon_silva.projeto_hotel.models.ServicesModel;

public class ServiceCreator {
    
  
    


    public static ServicesModel newServiceModel(){

        return new ServicesModel(null, "alimenticio", "Servico de quarto", 30.00);
    }

     public static ServicesModel newServiceModel2(){

        return new ServicesModel(null, "Geral", "Servico de hospedagem", 70.00);
    }
}
