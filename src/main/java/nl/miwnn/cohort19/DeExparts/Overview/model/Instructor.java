package nl.miwnn.cohort19.DeExparts.Overview.model;

import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String emailAdress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Cohort> cohorts = new ArrayList<>();

    private String description;

    private String course;

    @CsvDate(value = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @OneToOne
    private User user;

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
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAdress = emailAdress;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.course = course;
        this.birthDate = birthDate;
        this.user = user;
    }

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

    public String getFullName() {
        return String.format("%s %s",firstName,lastName);
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getAgeInYears(LocalDate birthDate){
        return Math.abs(ChronoUnit.YEARS.between(LocalDate.now(),birthDate));
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
}
