package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.ScheduleDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IScheduleService {

    ScheduleDTO findById(Long id);
    List<ScheduleDTO> findAllSchedule();
    List<ScheduleDTO> findAllScheduleAvailable();
    List<ScheduleDTO> findAllScheduleAvailableByDate(LocalDate date);
    List<ScheduleDTO> findAllScheduleAvailableByTutor(Long tutorId);
    List<ScheduleDTO> findAllScheduleAvailableByTutorAndDate(Long tutorId, LocalDate date);
    List<ScheduleDTO> findAllScheduleByTutor(Long id);
    List<ScheduleDTO> findAllScheduleByStudent(Long id);
    List<Map<String, String>> isValidRangeTime(ScheduleDTO scheduleDTO);
    ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO);
    ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO);
    void deleteSchedule(Long id);
}
