package nl.miwnn.cohort19.DeExparts.Overview.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import org.hibernate.engine.internal.Cascade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wat doe ik? TODO change author name
 * Information about a cohort (that includes participants, instructors and more)
 */
@Entity
public class Cohort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "name")
    @Column(nullable = false)
    private String name;

    @CsvBindByName(column = "subject")
    @Column(nullable = false)
    private String subject;

    @CsvBindByName(column = "startDate")
    @CsvDate(value = "yyyy-MM-dd")
    @Column(nullable = true)
    private LocalDate startDate;

    @CsvBindByName(column = "endDate")
    @CsvDate(value = "yyyy-MM-dd")
    @Column(nullable = true)
    private LocalDate endDate;

    @OneToMany(mappedBy = "cohorts")
    List<Participant> participants = new ArrayList<>();

    @ManyToMany(mappedBy = "cohorts", fetch = FetchType.EAGER)
    List<Instructor> instructors = new ArrayList<>();

    public Cohort(String name, String subject, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Cohort() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}