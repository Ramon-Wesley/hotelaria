package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.HotelModel;

public class HotelCreator {
    
    public static HotelModel newModelHotel(){

        return new HotelModel(null, "Hotel1", "Hotel bom", 4, null, AddressCreator.newAddressModel());
    }
}
