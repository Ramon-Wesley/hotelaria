package com.ramon_silva.projeto_hotel.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AddressDto implements Serializable{  
    @JsonProperty("id")
    private Long id;

    @JsonProperty("country")
    @NotBlank
    @Length(min=2)
     private String country;
    
     @JsonProperty("state")
    @NotBlank
    @Length(min=2)
     private String state;

     @JsonProperty("zapcode")
    @NotBlank
    @Length(min = 10)
     private String zapCode;

     @JsonProperty("city")
    @NotBlank
    @Length(min = 2)
    private  String city;

    @JsonProperty("neighbothood")
    @NotBlank
     private String neighborhood;
    
     @JsonProperty("number")
    @NotBlank
     private String number;

     @JsonProperty("complement")
     private String complemement;

}
