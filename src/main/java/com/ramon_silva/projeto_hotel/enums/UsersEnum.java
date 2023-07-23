package com.ramon_silva.projeto_hotel.enums;

public enum UsersEnum {
    RECEPCIONIST("recepcionista"),
    CLIENT("cliente");

    private String role;

    UsersEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
