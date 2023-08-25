package com.ramon_silva.projeto_hotel.util;




import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.models.ServicesModel;

public class ServiceCreator {
    
    public static Set<ServicesModel> getServices() {
        Set<ServicesModel> services=new HashSet();
        services.add(newServiceModel());
        services.add(newServiceModel2());
        return services;
    }

    


    public static ServicesModel newServiceModel(){

        return new ServicesModel(null, "alimenticio", "Servico de quarto", 30.00,null);
    }

     public static ServicesModel newServiceModel2(){

        return new ServicesModel(null, "Geral", "Servico de hospedagem", 70.00,null);
    }
}
