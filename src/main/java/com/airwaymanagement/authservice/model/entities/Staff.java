package com.airwaymanagement.authservice.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "staff_id")
    private Long staffId;

    @JoinColumn(name = "user", nullable = false)
    @OneToOne
    private User user;

    @Column(name = "staff_role")
    private String staffRole;

    @Column(name = "date_of_joining")
    private LocalDateTime dateOfJoining;

    @Column(name = "salary")
    private String salary;

}
