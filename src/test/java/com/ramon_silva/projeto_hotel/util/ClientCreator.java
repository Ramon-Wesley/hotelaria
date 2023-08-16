package com.ramon_silva.projeto_hotel.util;



import com.ramon_silva.projeto_hotel.models.ClientModel;

public class ClientCreator {


     public static ClientModel newClientModel(){
    return new ClientModel(null, "Client1", "118.298.766-43",
    "ramonwj.s@outlook.com","031 95538-8888",AddressCreator.newAddressModel());
}
public static ClientModel newClientModel2(){
   return new ClientModel(null, "Client2", "369.757.090-09",
   "Testw1sj.s@gmail.com","031 95588-8888", AddressCreator.newAddressModel2());
}

public static ClientModel newClientModel3(){
   return new ClientModel(null, "Client3", "547.267.940-00",
   "Teste3sj.s@gmail.com","031 55555-5555", AddressCreator.newAddressModel3());
}
}
