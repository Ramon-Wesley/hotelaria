package com.ramon_silva.projeto_hotel.enums;

public enum TypeRoomEnum{
    COMMON("comum"),
    SUITE("suite"),
    LUX("luxo"),
    PRESIDENTIAL("presidencial");
    
private String role;
    TypeRoomEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
