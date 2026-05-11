package nl.miwnn.cohort19.DeExparts.Overview.model;

import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * @author wat doe ik?
 * Abstract base entity representing a person with shared personal details
 * such as name, contact information, birth date, and description.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotBlank(message = "Voornaam is verplicht")
    private String firstName;

    @NotBlank(message = "Achternaam is verplicht")
    private String lastName;

    @NotBlank(message = "E-mailadres is verplicht")
    private String emailAdress;

    @NotBlank(message = "Woonplaats is verplicht")
    private String city;

    @NotBlank(message = "Telefoonnummer is verplicht")
    private String phoneNumber;

    @CsvDate(value = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String description;


    public Person(Long id, String firstName, String lastName, String emailAdress,
                  String city, String phoneNumber, String description, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAdress = emailAdress;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.description = description;
    }

    public Person(){}

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

    public String getFullName(){
        return String.format("%s %s",this.firstName,this.lastName);
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getAgeInYears(LocalDate birthDate) {
        return Math.abs(ChronoUnit.YEARS.between(LocalDate.now(),birthDate));
    }

    public String getBirthday() {
        LocalDate today = LocalDate.now();
        LocalDate birthdayThisYear = this.birthDate.withYear(today.getYear());

        LocalDate nextBirthday = birthdayThisYear.isBefore(today) ?
                birthdayThisYear.plusYears(1) :
                birthdayThisYear;

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(new Locale("nl", "NL"));

        return nextBirthday.format(formatter);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
