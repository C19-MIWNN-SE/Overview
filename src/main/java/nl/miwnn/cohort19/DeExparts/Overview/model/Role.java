package nl.miwnn.cohort19.DeExparts.Overview.model;

import jakarta.persistence.*;

import java.util.Collection;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(Long id, String authority, String name, Collection<User> users) {
        this.id = id;
        this.authority = authority;
        this.name = name;
        this.users = users;
    }

    public Role() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
