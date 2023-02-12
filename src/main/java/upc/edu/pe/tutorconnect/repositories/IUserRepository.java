package upc.edu.pe.tutorconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.tutorconnect.entities.User;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUserTypeIdEquals(Long id);
}
