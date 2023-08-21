package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.HotelModel;

public class HotelCreator {
    
    public static HotelModel newModelHotel(){

        return new HotelModel(null, "Hotel1","85.377.666/0001-16", "Hotel bom", 4, null, AddressCreator.newAddressModel());
    }

    public static HotelModel newModelHotel2(){

        return new HotelModel(null, "Hotel2","50.477.031/0001-06", "Hotel muito bom", 5, null, AddressCreator.newAddressModel2());
    }
}
