
package com.ramon_silva.projeto_hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.ramon_silva.projeto_hotel.models.UsersModel;



public interface UserRepository  extends JpaRepository<UsersModel,Long>
{
    UserDetails findByLogin(String login);

}
