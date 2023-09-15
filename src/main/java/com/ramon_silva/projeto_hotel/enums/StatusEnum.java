package com.ramon_silva.projeto_hotel.enums;

public enum StatusEnum {
    PAY("pago"),
    CONFIRM("confirmado"),
    PENDING("pendente"),
    CANCELED("cancelado");

    private String role;

    StatusEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
