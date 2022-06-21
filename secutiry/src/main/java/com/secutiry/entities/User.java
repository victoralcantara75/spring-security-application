package com.secutiry.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Table (name = "users")
public class User {

    @Id
    @Column
    @GeneratedValue(generator = "uuid2")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column
    private String name;

    @Column String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "users_roles", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    Collection<Role> roles = new ArrayList<>();

    User () {}

    User (String name, String password){
        this.setName(name);
        this.setPassword(password);
    }

    public void encryptPassword(){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        this.setPassword(bcrypt.encode(this.password));
    }

    public void setId(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public Collection<Role> getRoles(){
        return this.roles;
    }

    public void setRoles(Collection<Role> roles){
        this.roles = roles;
    }

}
