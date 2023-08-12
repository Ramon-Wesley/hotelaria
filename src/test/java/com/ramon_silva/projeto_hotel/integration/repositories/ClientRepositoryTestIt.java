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

import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.util.ClientCreator;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;


@DataJpaTest
@DisplayName("Testes para o repositorio")

public class ClientRepositoryTestIt {

    @Autowired
    private ClientRepository clientRepository;

    

    private ClientModel clientModel;
    private ClientModel clientModel2;

@BeforeEach
void setUp(){
    clientModel=clientRepository.save(ClientCreator.newClientModel());
    clientModel2=clientRepository.save(ClientCreator.newClientModel3());
}

@AfterEach
void setDown(){
    clientRepository.deleteAll();
}

    @Test
    @DisplayName("Salvar cliente")
    void Test_save_persist_client_when_success() {
        clientModel=ClientCreator.newClientModel2();
        ClientModel result=this.clientRepository.save(clientModel);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(clientModel.getEmail());
        Assertions.assertThat(result.getCpf()).isEqualTo(clientModel.getCpf());
    }

    @Test
    @DisplayName("Salvar cliente com dados vazios ou nulos!")
   
    void Test_save_client_with_dates_null_or_empty(){
        ClientModel result=new ClientModel(null, null, null, null, null, null);
        Assertions.assertThatThrownBy(()->this.clientRepository.save(result)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("atualizar cliente")
    void Test_update_persist_client_when_success() {
        
        clientModel.setName("Teste1");

        ClientModel updateResult=clientRepository.save(clientModel);
        Assertions.assertThat(updateResult).isNotNull();
        Assertions.assertThat(updateResult.getId()).isNotNull();
        Assertions.assertThat(updateResult.getId()).isEqualTo(clientModel.getId());
        Assertions.assertThat(updateResult.getName()).isEqualTo(clientModel.getName());
    }

    @Test
    @DisplayName("atualizar cliente com email ou cpf ja existente")
    void Test_update_persist_client_with_cpf_or_email_exists() {
        clientModel.setCpf(clientModel2.getCpf());
        clientModel.setEmail(clientModel2.getEmail());
    
        ClientModel ex=clientRepository.save(clientModel);        
   
    }

      @Test
    @DisplayName("deletar cliente")
    void Test_delete_client_when_success() {
       
        this.clientRepository.deleteById(clientModel.getId());

        Optional<ClientModel> resultFind= this.clientRepository.findById(clientModel.getId());

        Assertions.assertThat(resultFind.isEmpty()).isTrue();
    }

     @Test
    @DisplayName("encontrar cliente pelo email")
    void Test_find_client_by_email_when_success() {
    
        String email=clientModel.getEmail();
        ClientModel resultFind= this.clientRepository.findByEmail(email);

        Assertions.assertThat(resultFind).isNotNull();
        Assertions.assertThat(resultFind.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("falha ao encontrar cliente com email inexistente")
    void Test_find_client_by_email_when_failure() {
       

        String email="test@gmail.com";
        ClientModel resultFind= this.clientRepository.findByEmail(email);

        Assertions.assertThat(resultFind).isNull();        
    }

}
