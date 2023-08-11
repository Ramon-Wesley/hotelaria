package com.ramon_silva.projeto_hotel.integration.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.util.ClientCreator;

import jakarta.validation.ConstraintViolationException;


@DataJpaTest
@DisplayName("Testes para o repositorio")

public class ClientRepositoryTestIt {

    @Autowired
    private ClientRepository clientRepository;

    private ClientModel clientModel;

    @Test
    @DisplayName("Salvar cliente")
    void Test_save_persist_client_when_success() {
        clientModel=ClientCreator.createNewClientModel();
        ClientModel result=this.clientRepository.save(clientModel);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(clientModel.getEmail());
        Assertions.assertThat(result.getCpf()).isEqualTo(clientModel.getCpf());
    }

    @Test
    @DisplayName("Salvar cliente com dados vazios ou nulos!")
    void Test_save_client_with_dates_null_or_empty(){
        clientModel=new ClientModel();
        Assertions.assertThatThrownBy(()->this.clientRepository.save(clientModel)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("atualizar cliente")
    void Test_update_persist_client_when_success() {
        clientModel=ClientCreator.createNewClientModel();
        ClientModel result=this.clientRepository.save(clientModel);

        result.setName("Teste1");

        ClientModel updateResult=clientRepository.save(result);
        Assertions.assertThat(updateResult).isNotNull();
        Assertions.assertThat(updateResult.getId()).isNotNull();
        Assertions.assertThat(updateResult.getId()).isEqualTo(result.getId());
        Assertions.assertThat(updateResult.getName()).isEqualTo(result.getName());
    }

      @Test
    @DisplayName("deletar cliente")
    void Test_delete_client_when_success() {
        clientModel=ClientCreator.createNewClientModel();
        ClientModel result=this.clientRepository.save(clientModel);

        this.clientRepository.deleteById(result.getId());

        Optional<ClientModel> resultFind= this.clientRepository.findById(result.getId());

        Assertions.assertThat(resultFind.isEmpty()).isTrue();
    }

     @Test
    @DisplayName("encontrar cliente pelo email")
    void Test_find_client_by_email_when_success() {
        clientModel=ClientCreator.createNewClientModel();
        ClientModel result=this.clientRepository.save(clientModel);
        String email=result.getEmail();
        ClientModel resultFind= this.clientRepository.findByEmail(email);

        Assertions.assertThat(resultFind).isNotNull();
        Assertions.assertThat(resultFind.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("falha ao encontrar cliente com email inexistente")
    void Test_find_client_by_email_when_failure() {
        clientModel=ClientCreator.createNewClientModel();
        this.clientRepository.save(clientModel);
        String email="test@gmail.com";
        ClientModel resultFind= this.clientRepository.findByEmail(email);

        Assertions.assertThat(resultFind).isNull();        
    }

}
