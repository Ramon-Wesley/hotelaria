package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.dto.EmailDto;

public class EmailCreator {
    private EmailCreator() {
    }

    public static EmailDto createEmail() {

        return new EmailDto("teste", "Teste@gmail.com", "teste2@gmail.com", "ok", "Testando");
    }
}
