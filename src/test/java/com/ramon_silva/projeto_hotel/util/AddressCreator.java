package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.AddressModel;

public class AddressCreator {
    private AddressCreator() {
    }

    public static AddressModel newAddressModel() {
        return new AddressModel(null, "Brasil", "minas", "35.170-301", "coronel fabriciano", "gavea", "111",
                "complemento");
    }

    public static AddressModel newAddressModel2() {
        return new AddressModel(null, "Brasil", "Sao paulo", "11.111-111", "Sao paulo", "morumbi", "222",
                "complemento2");
    }

    public static AddressModel newAddressModel3() {
        return new AddressModel(null, "Brasil", "Rio de janeiro", "22.222-222", "Rio de janeiro", "gavea", "333",
                "complemento2");
    }
}
