package nl.miwnn.cohort19.DeExparts.Overview.model;

import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Voornaam mag niet leeg zijn")
    private String firstName;

    @NotBlank(message = "Achternaam mag niet leeg zijn")
    private String lastName;

    @NotBlank(message = "E-mailadres mag niet leeg zijn")
    private String emailAdress;

    @NotBlank(message = "Woonplaats mag niet leeg zijn")
    private String city;

    @NotBlank(message = "Telefoonnummer mag niet leeg zijn")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Cohort> cohorts = new ArrayList<>();

    private String description;

    private String course;

    @CsvDate(value = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
