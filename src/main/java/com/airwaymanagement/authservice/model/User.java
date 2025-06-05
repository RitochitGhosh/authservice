package com.airwaymanagement.authservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @JoinColumn(name = "user_role")
    @ManyToOne
    private Role role;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    private String govId; // TODO: Handle this more carefully for scalability

    @Column(name = "passport_id")
    private String passportId;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private String gender;

    @Column(nullable = false, name = "password")
    private String password;


}
