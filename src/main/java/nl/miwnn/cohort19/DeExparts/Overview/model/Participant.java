package nl.miwnn.cohort19.DeExparts.Overview.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Author: Anouk de Vos
 * Represents a participant entity, extending Person with employer information,
 * an assigned cohort, a linked user account, and a profile image.
 */

@Entity
public class Participant extends Person{

    @ManyToOne(fetch = FetchType.LAZY)
    private Cohort cohorts;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private User user;

    private String employer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    //TODO dubbele code Instructor & Participant
    public Participant(Long id,
                       String firstName,
                       String lastName,
                       String emailAdress,
                       String city,
                       String phoneNumber,
                       String description,
                       LocalDate birthDate,
                       String employer,
                       User user) {
        super(id, firstName,lastName,emailAdress,city,phoneNumber,description,birthDate);
        this.employer = employer;
        this.user = user;
    }

    public Participant() {
    }

    public Cohort getCohorts() {
        return cohorts;
    }

    public void setCohorts(Cohort cohort) {
        this.cohorts = cohort;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}