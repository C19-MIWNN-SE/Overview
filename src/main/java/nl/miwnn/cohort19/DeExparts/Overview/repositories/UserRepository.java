package nl.miwnn.cohort19.DeExparts.Overview.repositories;

import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Coen Cuppes
 * !! TODO include description !!
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
