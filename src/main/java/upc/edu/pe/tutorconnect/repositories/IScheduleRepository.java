package upc.edu.pe.tutorconnect.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.tutorconnect.entities.Schedule;

import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByUserIdEquals(Long id, Sort sort);
    List<Schedule> findAllByTutorIdEquals(Long id, Sort sort);
}
