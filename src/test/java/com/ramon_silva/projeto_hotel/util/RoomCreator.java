package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;
import com.ramon_silva.projeto_hotel.models.RoomModel;

public class RoomCreator {
    
    public static RoomModel updateModelRoom(){
        return new RoomModel(1L, HotelCreator.updateModelHotel(),"111", TypeRoomEnum.COMMON, "Quarto comum", 150.00);
    }
}
