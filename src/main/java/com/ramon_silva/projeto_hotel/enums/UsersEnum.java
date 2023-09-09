package com.ramon_silva.projeto_hotel.enums;

public enum UsersEnum {
    ADMIN("admin"),
    HOTEL("hotel"),
    USER("user")
    ;

    private String role;

    UsersEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
