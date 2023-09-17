package com.ramon_silva.projeto_hotel.util;

import com.ramon_silva.projeto_hotel.models.GuestModel;

public class GuestCreator {

   private GuestCreator() {
   }

   public static GuestModel newGuestModel() {
      return new GuestModel(null, "Guest1", "118.298.766-43",
            "ramonwj.s@outlook.com", "031 95538-8888", AddressCreator.newAddressModel(), true);
   }

   public static GuestModel newGuestModel2() {
      return new GuestModel(null, "Guest2", "369.757.090-09",
            "Testw1sj.s@gmail.com", "031 95588-8888", AddressCreator.newAddressModel2(), true);
   }

   public static GuestModel newGuestModel3() {
      return new GuestModel(null, "Guest3", "547.267.940-00",
            "Teste3sj.s@gmail.com", "031 55555-5555", AddressCreator.newAddressModel3(), true);
   }
}
