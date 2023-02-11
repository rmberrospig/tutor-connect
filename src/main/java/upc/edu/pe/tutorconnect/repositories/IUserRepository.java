package upc.edu.pe.tutorconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.tutorconnect.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
}
