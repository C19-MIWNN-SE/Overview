package nl.miwnn.cohort19.DeExparts.Overview.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Anouk de Vos
 * Represents an instructor entity, extending Person with course information,
 * associated cohorts, a linked user account, and a profile image.
 */
@Entity
public class Instructor extends Person{

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Cohort> cohorts = new ArrayList<>();

    private String course;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    public Instructor() {
    }

    public Instructor(Long id,
                      String firstName,
                      String lastName,
                      String emailAdress,
                      String city,
                      String phoneNumber,
                      String description,
                      String course,
                      LocalDate birthDate,
                      User user) { // TODO checken of dit nodig is
        super(id, firstName, lastName, emailAdress, city, phoneNumber, description, birthDate);
        this.course = course;
        this.user = user;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public List<Cohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(List<Cohort> cohorts) {
        this.cohorts = cohorts;
    }

    public void addCohort(Cohort cohort){
        cohorts.add(cohort);
    }

    public void removeCohort(Cohort cohort){
        this.cohorts.remove(cohort);
        cohort.getInstructors().remove(this);
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
