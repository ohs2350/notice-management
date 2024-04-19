package com.ohs.rms.domain.admin;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "admin")
public class Admin {

    @Id @Column(name = "admin_id")
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private Admin() {
    }

    public Admin(String name) {
        this.name = name;
    }
}
