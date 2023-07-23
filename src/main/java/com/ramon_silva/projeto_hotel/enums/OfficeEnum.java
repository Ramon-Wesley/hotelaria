package com.ramon_silva.projeto_hotel.enums;

public enum OfficeEnum {
    RECEPCIONIST("recepcionista"),
    CLEANER("faxineira"),
    VALET_PARKING("manobrista"),
    CHEF("cozinheiro");

    private String role;

    OfficeEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return this.role;
    }
}
