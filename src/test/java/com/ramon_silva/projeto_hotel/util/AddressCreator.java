package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.models.AddressModel;

public class AddressCreator {
    

    public static AddressModel updateModelHotel(){
        return new AddressModel(1L, "Brasil", "minas", "35.170-301", "coronel fabriciano", "gavea", "111", "complemento");
    }

     public static AddressDto updateDtoHotel(){
        return new AddressDto(1L, "Brasil", "minas", "35.170-301", "coronel fabriciano", "gavea", "111", "complemento");
    }
}
