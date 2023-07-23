package com.ramon_silva.projeto_hotel.enums;

public enum PaymentMethodEnum {
    CREDIT("credito"),
    DEBIT("debito"),
    BANKER_TRANSFER("transferencia_bancaria"),
    MONEY("dinheiro");

    private String role;

    PaymentMethodEnum(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
