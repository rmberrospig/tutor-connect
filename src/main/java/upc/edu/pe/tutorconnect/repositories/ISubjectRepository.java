package upc.edu.pe.tutorconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.tutorconnect.entities.Subject;

import java.util.Optional;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);

}
