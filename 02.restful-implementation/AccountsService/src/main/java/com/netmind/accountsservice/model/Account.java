package com.netmind.accountsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El typo debe tener valor")
    @NonNull
    @Size(min = 3, max = 50)
    private String type;

    @NotBlank(message = "El fecha debe tener valor")
    @NonNull
    @DateTimeFormat
    Date openingDate;


    private int balance;


    private Long ownerId;

    @Transient
    Customer owner;


}
