package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;

public class RoomCreator {
    
    public static RoomModel newModelRoom(){
        HotelModel hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(1L);
        hotelModel.getAddress().setId(1L);
        return new RoomModel(null, hotelModel,"111", TypeRoomEnum.COMMON, "Quarto comum", 150.00);
    }

    public static RoomModel newModelRoom2(){
        HotelModel hotelModel=HotelCreator.newModelHotel();
        hotelModel.setId(2L);
        hotelModel.getAddress().setId(2L);
        return new RoomModel(null, hotelModel,"222", TypeRoomEnum.LUX, "Quarto de luxo", 450.00);
    }
}
