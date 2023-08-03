package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.dto.ClientDto;

public class ClientCreator {
    
    
    public static ClientDto createClientToBeSaved(){
        AddressDto addressDto=new AddressDto(1L, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", null);
        return new ClientDto(1L, "Client1", "118.298.766-43","Ramonwj.s@outlook.com","031 98888-8888", addressDto);
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
        AddressDto addressDto=new AddressDto(null, "Brasil", "minas gerais","35.117.222", "coronel fabriciano", "gavea", "116", "hjhdj");
        return new ClientDto(null, "Client2", "511.438.760-08","Ramonw1sj.s@gmail.com","031 98888-8888", addressDto);
    }
    
}
