package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.ScheduleDTO;

import java.util.List;

public interface IScheduleService {

    List<ScheduleDTO> findAllSchedule();
    List<ScheduleDTO> findAllScheduleByTutor(Long id);
    List<ScheduleDTO> findAllScheduleByStudent(Long id);
    ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO);
    ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO);
    void deleteSchedule(Long id);
}
