package com.airwaymanagement.authservice.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
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
    private Date dateOfJoining;

    @Column(name = "salary")
    private String salary;

}
