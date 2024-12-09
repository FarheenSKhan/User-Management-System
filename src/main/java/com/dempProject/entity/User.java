package com.dempProject.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private String password;
    private String address;
    private String pincode;
    private Long otp;




}