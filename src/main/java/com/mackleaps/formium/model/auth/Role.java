package com.mackleaps.formium.model.auth;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "role")
public class Role implements Serializable,GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long id;

    @Column(name = "ds_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(RoleType roleType) {
        this.roleType = roleType;
    }

    @SuppressWarnings("unused")
    public Role() {

    }

    @Override
    public String getAuthority() {
        return this.roleType.name();
    }

    public Long getId() {
        return this.id;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public Collection<User> getUsers() {
        return this.users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
