package nl.miwnn.cohort19.DeExparts.Overview.model;

import jakarta.persistence.*;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    public String firstName;

    @Column(nullable = false)
    public String lastName;

    public Instructor(String firstName, String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public Instructor(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
