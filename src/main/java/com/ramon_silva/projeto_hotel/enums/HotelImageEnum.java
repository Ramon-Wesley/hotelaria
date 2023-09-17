package com.ramon_silva.projeto_hotel.enums;

public enum HotelImageEnum {
    FRONTDESK("recepcao"),
    RESTAURANT("restaurante"),
    RECREATION_AREA("area de lazer"),
    OTHER("outro");

    private String role;

    HotelImageEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
