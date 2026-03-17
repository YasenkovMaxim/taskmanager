package com.maxim.taskmanager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "seqUserId", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "seqUserId")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private int age;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Instant created;

    private Instant updated;

    @PrePersist
    protected void onCreate() {
        created = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = Instant.now();
    }
}
