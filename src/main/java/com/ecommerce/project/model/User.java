package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(name = "username")
    private String username;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    //cascade persists saves both the entities together, merge update both entities together, fetch eager loads both immediately
    //this is done because otherwise JPA will not save in role table on its own for roles when a user is created
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany( mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    //    @JoinTable(name = "user_address",
//                joinColumns = @JoinColumn(name = "user_id"),
//                inverseJoinColumns = @JoinColumn(name = "address_id"))
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    private Cart cart;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
