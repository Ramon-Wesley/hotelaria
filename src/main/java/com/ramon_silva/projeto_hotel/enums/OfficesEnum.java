package com.ramon_silva.projeto_hotel.enums;

public enum OfficesEnum {
    RECEPCIONIST("recepcionista"),
    CLEANER("faxineira"),
    VALET_PARKING("manobrista"),
    CHEF("cozinheiro");

    private String role;
    OfficesEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
  
}
