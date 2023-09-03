package com.ramon_silva.projeto_hotel.integration.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.util.GuestCreator;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;


@DataJpaTest
@DisplayName("Testes para o repositorio")

public class GuestRepositoryTestIt {

    @Autowired
    private GuestRepository guestRepository;

    private GuestModel guestModel;
    private GuestModel guestModel2;

@BeforeEach
void setUp(){
    guestModel=guestRepository.save(GuestCreator.newGuestModel());
    guestModel2=guestRepository.save(GuestCreator.newGuestModel3());
}

@AfterEach
void setDown(){
    guestRepository.deleteAll();
}

    @Test
    @DisplayName("Salvar hospede")
    void Test_save_persist_guest_when_success() {
        guestModel=GuestCreator.newGuestModel2();
        GuestModel result=this.guestRepository.save(guestModel);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(guestModel.getEmail());
        Assertions.assertThat(result.getCpf()).isEqualTo(guestModel.getCpf());
    }

    @Test
    @DisplayName("Salvar hospede com dados vazios ou nulos!")
   
    void Test_save_guest_with_dates_null_or_empty(){
        GuestModel result=new GuestModel(null, null, null, null, null, null);
        Assertions.assertThatThrownBy(()->this.guestRepository.save(result)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("atualizar hospede")
    void Test_update_persist_guest_when_success() {
        
        guestModel.setName("Teste1");

        GuestModel updateResult=guestRepository.save(guestModel);
        Assertions.assertThat(updateResult).isNotNull();
        Assertions.assertThat(updateResult.getId()).isNotNull();
        Assertions.assertThat(updateResult.getId()).isEqualTo(guestModel.getId());
        Assertions.assertThat(updateResult.getName()).isEqualTo(guestModel.getName());
    }

    @Test
    @DisplayName("atualizar hospede com email ou cpf ja existente")
    void Test_update_persist_guest_with_cpf_or_email_exists() {
        guestModel.setCpf(guestModel2.getCpf());
        guestModel.setEmail(guestModel2.getEmail());
    
        GuestModel ex=guestRepository.save(guestModel);        
   
    }

      @Test
    @DisplayName("deletar hospede")
    void Test_delete_guest_when_success() {
       
        this.guestRepository.deleteById(guestModel.getId());

        Optional<GuestModel> resultFind= this.guestRepository.findById(guestModel.getId());

        Assertions.assertThat(resultFind.isEmpty()).isTrue();
    }

     @Test
    @DisplayName("encontrar hospede pelo email")
    void Test_find_guest_by_email_when_success() {
    
        String email=guestModel.getEmail();
        GuestModel resultFind= this.guestRepository.findByEmail(email);

        Assertions.assertThat(resultFind).isNotNull();
        Assertions.assertThat(resultFind.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("falha ao encontrar hospede com email inexistente")
    void Test_find_guest_by_email_when_failure() {
       

        String email="test@gmail.com";
        GuestModel resultFind= this.guestRepository.findByEmail(email);

        Assertions.assertThat(resultFind).isNull();        
    }

}
