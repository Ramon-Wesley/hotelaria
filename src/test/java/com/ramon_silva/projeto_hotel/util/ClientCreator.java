package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.ClientModel;

public class ClientCreator {
     public static ClientModel updateModelClient(){
    return new ClientModel(1L, "Client2", "118.298.766-43",
    "Ramonwj.s@outlook.com","031 95538-8888",AddressCreator.updateModelHotel());
}
public static ClientModel createNewClientModel(){
   AddressModel addressmodel=new AddressModel(null, "Brasil", "minas gerais","35.117.242", "coronel fabriciano", "gavea", "116", "hjhdj");
   return new ClientModel(null, "Client2", "369.757.090-09","Testw1sj.s@gmail.com","031 95588-8888", addressmodel);
}

public static ClientDto createClientToBeSaved(){
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(null, "Crlient1", "118.298.766-43","Ramonwj.s@outlook.com","031 98888-8888", addressDto);
    }
    public static ClientDto createClientToBeSaved2(){
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(null, "Crlient1", "374.192.330-39","test3wj.s@outlook.com","031 99998-8888", addressDto);
    }

    public static ClientDto createClientEmailEqualsToBeSaved(){
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(null, "Client1", "152.567.910-44","Ramonwj.s@outlook.com","031 98888-8888", addressDto);
   }

    public static ClientDto createClientCPFEqualsSaved(){
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(null, "Client1", "118.298.766-43","Ramonw1j.s@outlook.com","031 98888-8888", addressDto);
    }

    public static ClientDto createNewClient(){
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.242", "coronel fabriciano", "gavea", "116", "hjhdj");
        return new ClientDto(null, "Client2", "511.438.760-08","Ramonwsj.s@gmail.com","031 95577-8888", addressDto);
    }

      public static ClientDto createToBeSave2(){
        AddressDto addressDto=new AddressDto(2L, "EUA", "Boston","35.117.242", "new york", "brookilin", "112", "hjhdj");
        return new ClientDto(2L, "Client2", "118.298.766-43","Ramonwj.s@outlook.com","031 95538-8888", addressDto);
    }
     public static ClientDto createToBeSave3(){
        AddressDto addressDto=new AddressDto(3L, "EUA", "Boston","35.117.242", "new york", "brookilin", "112", "hjhdj");
        return new ClientDto(3L, "Austin", "511.438.760-08","cliew1sj.s@gmail.com","031 95538-8888", addressDto);
    }
     public static ClientModel createClientModelToBeSaved(){
        AddressModel addressModel=new AddressModel(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientModel(null, "Crlient1", "118.298.766-43","Ramonwj.s@outlook.com","031 98888-8888", addressModel);
    }
     public static ClientModel createClientModelToBeSaved2(){
        AddressModel addressModel=new AddressModel(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientModel(null, "Crlient1", "374.192.330-39","test3wj.s@outlook.com","031 99998-8888", addressModel);
    }
}
