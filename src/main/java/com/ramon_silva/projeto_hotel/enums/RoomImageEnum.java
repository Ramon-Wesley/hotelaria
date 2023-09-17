package com.ramon_silva.projeto_hotel.enums;

public enum RoomImageEnum {
    BATHROOM("banheiro"),
    SLEEPING_AREA("area de dormir"),
    OTHER("outro");

    private String role;

    RoomImageEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
