package com.ramon_silva.projeto_hotel.enums;

public enum StatusReservationEnum {
    CONFIRM("confirmado"),
    PENDING("pendente"),
    CANCELED("cancelado");

    private String role;

    StatusReservationEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
