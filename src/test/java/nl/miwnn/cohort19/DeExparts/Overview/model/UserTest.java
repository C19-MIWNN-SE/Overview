package nl.miwnn.cohort19.DeExparts.Overview.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */

class UserTest {

    private User testInstructor;
    private User testParticipant;

    @BeforeEach
    void setUp() {
        testInstructor = new User("Docent", "pw");
        testInstructor.setInstructor(new Instructor());

        testParticipant = new User("Deelnemer", "pw");
        testParticipant.setParticipant(new Participant());
    }

    @Test
    @DisplayName("Returns true when user is an instructor")
    void isInstructor_returns_true_for_instructor(){
        assertTrue(testInstructor.isInstructor());
    }

    @Test
    @DisplayName("Returns true when user is a participant")
    void returnsTrueWhenUserIsAParticipant() {
        assertTrue(testParticipant.isParticipant());
    }

    @Test
    @DisplayName("Returns false when participant is instructor")
    void returnsFalseWhenParticipantIsInstructor() {
        assertFalse(testParticipant.isInstructor());
    }
}
