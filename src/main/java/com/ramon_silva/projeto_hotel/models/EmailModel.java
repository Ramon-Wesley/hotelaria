package com.ramon_silva.projeto_hotel.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ramon_silva.projeto_hotel.enums.StatusEmailEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="emails")
@Table(name="emails")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmailModel implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String owerRef;
    private String emailFrom;
    private String emailTo;
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime sendDateEmail=LocalDateTime.now();

    @Enumerated
    private StatusEmailEnum StatusEmail;
}
