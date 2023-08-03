package com.ramon_silva.projeto_hotel.enums;

public enum StatusEmailEnum {
    SEND("enviado"),
    ERROR("Erro");

     private String role;

       StatusEmailEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
