package com.airwaymanagement.authservice.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, unique = true, name="user_name")
    private String userName;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, name="email")
    private String email;

    @Column(nullable = false, unique = true, name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "gov_id")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
