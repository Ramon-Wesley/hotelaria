package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.dto.ClientDto;

public class ClientCreator {
    
    
    public static ClientDto createClientToBeSaved(){
        AddressDto addressDto=new AddressDto(1L, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(1L, "Crlient1", "118.298.766-43","Ramonwj.s@outlook.com","031 98888-8888", addressDto);
    }

    public static ClientDto createClientEmailEqualsToBeSaved(){
        AddressDto addressDto=new AddressDto(2L, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(2L, "Client1", "152.567.910-44","Ramonwj.s@outlook.com","031 98888-8888", addressDto);
   }

    public static ClientDto createClientCPFEqualsSaved(){
        AddressDto addressDto=new AddressDto(2l, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(2L, "Client1", "118.298.766-43","Ramonw1j.s@outlook.com","031 98888-8888", addressDto);
    }

    public static ClientDto createNewClient(){
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.242", "coronel fabriciano", "gavea", "116", "hjhdj");
        return new ClientDto(null, "Client2", "511.438.760-08","Ramonw1sj.s@gmail.com","031 95588-8888", addressDto);
    }

      public static ClientDto createToBeSave2(){
        AddressDto addressDto=new AddressDto(2L, "EUA", "Boston","35.117.242", "new york", "brookilin", "112", "hjhdj");
        return new ClientDto(2L, "Client2", "511.438.760-08","client2w1sj.s@gmail.com","031 95538-8888", addressDto);
    }
    
}
