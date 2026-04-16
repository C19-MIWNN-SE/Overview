package nl.miwnn.cohort19.DeExparts.Overview.model;

import jakarta.persistence.*;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String subject;

    // TODO convert startDate to LocalDate
    @Column(nullable = true)
    private String startDate;

    // TODO convert startDate to LocalDate
    @Column(nullable = true)
    private String endDate;

    @OneToMany(mappedBy = "cohorts")
    List<Participant> participants = new ArrayList<>();

    @ManyToMany(mappedBy = "cohorts")
    List<Instructor> instructors = new ArrayList<>();

    public Cohort(String name, String subject, String startDate, String endDate) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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