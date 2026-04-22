package nl.miwnn.cohort19.DeExparts.Overview.repositories;

import nl.miwnn.cohort19.DeExparts.Overview.model.Role;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
