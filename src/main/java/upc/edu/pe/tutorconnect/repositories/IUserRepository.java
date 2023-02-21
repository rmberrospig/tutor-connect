package upc.edu.pe.tutorconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.tutorconnect.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUserTypeIdEquals(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);
    Optional<User> findByIdAndUserTypeId(Long userId, Long userTypeId);
}
