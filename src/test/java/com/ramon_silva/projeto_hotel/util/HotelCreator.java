package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.HotelModel;

public class HotelCreator {
    
    public static HotelModel newModelHotel(){

        return new HotelModel(null, "Hotel1","85.377.666/0001-16","test@gmail.com","(00)00000-0000", "Hotel bom", 4, null, AddressCreator.newAddressModel());
    }

    public static HotelModel newModelHotel2(){

        return new HotelModel(null, "Hotel2","50.477.031/0001-06","test2@gmail.com","(11)11111-1111", "Hotel muito bom", 5, null, AddressCreator.newAddressModel2());
    }

    public static HotelModel newModelHotel3(){

        return new HotelModel(null, "Hotel3","59.036.890/0001-34","test3@gmail.com","(11)11111-1111", "Hotel muito bom", 5, null, AddressCreator.newAddressModel3());
    }
}
