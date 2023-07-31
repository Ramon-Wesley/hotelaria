package com.ramon_silva.projeto_hotel.enums;

public enum TypesControllersEnum {
    
    RESERVATION("reservas"),
    PAYMENT("pagamento"),
    AUTHENTICATION("autenticacao"),
    SERVICES("servicos"),
    ROOM("quartos");

    private String role;
       TypesControllersEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
