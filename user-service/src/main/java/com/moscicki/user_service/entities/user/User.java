package com.moscicki.user_service.entities.user;


import com.moscicki.user_service.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "`user`")
public class User extends BaseEntity {
    private String email;
    private String name;
    private String lastName;

    public User() {
    }

    public User(String id, String email, String name, String lastName) {
        this.setId(id);
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
