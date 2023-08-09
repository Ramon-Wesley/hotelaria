package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.HotelModel;

public class HotelCreator {
    
    public static HotelModel updateModelHotel(){

        return new HotelModel(1L, "Hotel1", "Hotel bom", 4, null, AddressCreator.updateModelHotel());
    }
}
