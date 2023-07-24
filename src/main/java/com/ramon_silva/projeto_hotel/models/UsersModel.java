package com.ramon_silva.projeto_hotel.models;
import java.util.Collection;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.ramon_silva.projeto_hotel.enums.UsersEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "usuarios")
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class UsersModel implements UserDetails{
    

    public UsersModel(String login, String password, UsersEnum role){
        this.login=login;
        this.password=password;
        this.role=role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 2)
    @Column(name="login",unique = true)
    private String login;

    @NotBlank
    @Length(min = 8)
    @Column(name="senha")
    private String password;

    
    @Enumerated
    @Column(name="tipo_usuario")
    @NotNull
    private UsersEnum role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UsersEnum.RECEPCIONIST) return List.of
        (
        new SimpleGrantedAuthority("ROLE_ADMIN"),
        new SimpleGrantedAuthority("ROLE_USER")
        );
        else return  List.of(
          new SimpleGrantedAuthority("ROLE_USER")
        );
       
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
       
    return true;
}

    @Override
    public boolean isAccountNonLocked() {
    return true;
 }

    @Override
    public boolean isCredentialsNonExpired() {
    return true;
 }

    @Override
    public boolean isEnabled() {
     
    return true;
 }
}
