package com.example.auth.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "number_phone",unique = true,length = 11,nullable = false)
    private String numberPhone;

    @Column(name = "email",unique = true,length = 20,nullable = false)
    private String email;

    @Column(name = "password",nullable = false,length = 100)
    private String password;

    @Column(name = "firstname",length = 30,nullable = false)
    private String firstname;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
   private List<Role>roles;
}
