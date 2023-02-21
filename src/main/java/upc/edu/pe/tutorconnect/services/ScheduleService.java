package upc.edu.pe.tutorconnect.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.ScheduleDTO;
import upc.edu.pe.tutorconnect.entities.Schedule;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.mappers.ISchedulerMapper;
import upc.edu.pe.tutorconnect.repositories.IScheduleRepository;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ScheduleService implements IScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;
    @Autowired
    private ITutorRepository tutorRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ISchedulerMapper schedulerMapper;

    @Override
    public ScheduleDTO findById(Long id) {
        return this.schedulerMapper.toDTO(this.scheduleRepository.findById(id).orElse(null));
    }

    @Override
    public List<ScheduleDTO> findAllSchedule() {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAll());
    }

    @Override
    public List<ScheduleDTO> findAllScheduleAvailable() {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByUserIdIsNull());
    }

    @Override
    public List<ScheduleDTO> findAllScheduleAvailableByDate(LocalDate date) {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByDateAndUserIdIsNull(date));
    }

    @Override
    public List<ScheduleDTO> findAllScheduleAvailableByTutor(Long tutorId) {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByTutorIdAndUserIdIsNull(tutorId));
    }

    @Override
    public List<ScheduleDTO> findAllScheduleAvailableByTutorAndDate(Long tutorId, LocalDate date) {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByTutorIdAndDateAndUserIdIsNull(tutorId, date));
    }

    @Override
    public List<ScheduleDTO> findAllScheduleByTutor(Long id) {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByTutorIdEquals(id, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Override
    public List<ScheduleDTO> findAllScheduleByStudent(Long id) {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByUserIdEquals(id, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Override
    public List<Map<String, String>> isValidRangeTime(ScheduleDTO scheduleDTO) {
        List<Map<String, String>> messages = new ArrayList<>();
        String horaInicio = "horaInicio";
        String horaFin = "horaFin";

        LocalTime startTime = null;
        LocalTime endTime = null;

        startTime = LocalTime.parse(scheduleDTO.getStartTime());
        endTime = LocalTime.parse(scheduleDTO.getEndTime());

        if (startTime.isAfter(endTime)) {
            Map<String, String> error = new HashMap<>();
            error.put(horaInicio, "La hora inicio no puede ser superior a la hora fin");
            messages.add(error);
            return messages;
        }

        if (startTime.until(endTime, ChronoUnit.HOURS) != 1) {
            Map<String, String> error = new HashMap<>();
            error.put(horaInicio, "La diferencia entre hora inicio y hora fin solo puede ser de 1 hora");
            messages.add(error);
            return messages;
        }

        List<ScheduleDTO> scheduleDTOList = this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAllByTutorIdAndDate(scheduleDTO.getTutorDTO().getId(), scheduleDTO.getDate()));

        boolean isValidStartTime = this.isValidStartTime(startTime, scheduleDTOList);
        boolean isValidEndTime = this.isValidEndTime(endTime, scheduleDTOList);

        if (!isValidStartTime) {
            Map<String, String> error = new HashMap<>();
            error.put(horaInicio, "La hora inicio ya se encuentra registrada o se cruza con otro horario ya registrado");
            messages.add(error);
        }

        if (!isValidEndTime) {
            Map<String, String> error = new HashMap<>();
            error.put(horaFin, "La hora fin ya se encuentra registrada o se cruza con otro horario ya registrado");
            messages.add(error);
        }

        return messages;
    }

    private boolean isValidStartTime(LocalTime startTime, List<ScheduleDTO> scheduleDTOList) {
        return scheduleDTOList.stream().noneMatch(s -> {
            LocalTime sStartTime = LocalTime.parse(s.getStartTime());
            LocalTime sEndTime = LocalTime.parse(s.getEndTime());
            return sStartTime.equals(startTime) ||
                    sStartTime.isBefore(startTime) && sEndTime.isAfter(startTime);
        });
    }

    private boolean isValidEndTime(LocalTime endTime, List<ScheduleDTO> scheduleDTOList) {
        return scheduleDTOList.stream().noneMatch(s -> {
            LocalTime sStartTime = LocalTime.parse(s.getStartTime());
            LocalTime sEndTime = LocalTime.parse(s.getEndTime());
            return sEndTime.equals(endTime) ||
                    sStartTime.isBefore(endTime) && sEndTime.isAfter(endTime);
        });
    }

    @Override
    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = this.schedulerMapper.toEntity(scheduleDTO);
        Tutor tutor = this.tutorRepository.findById(schedule.getTutor().getId()).orElse(null);
        schedule.setTutor(tutor);
        User user = this.userRepository.findById(schedule.getUser().getId()).orElse(null);
        schedule.setUser(user);
        return this.schedulerMapper.toDTO(this.scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleDTO updateSchedule(@NonNull Long id, @NonNull ScheduleDTO scheduleDTO) {
        Objects.requireNonNull(id, "El id no puede ser nulo");
        Objects.requireNonNull(scheduleDTO, "El objeto ScheduleDTO no puede ser nulo");

        return this.scheduleRepository.findById(id)
                .map(schedule -> {
                    Tutor tutor = this.tutorRepository.findById(scheduleDTO.getTutorDTO().getId())
                            .orElseThrow(() -> new IllegalArgumentException("El tutor no existe"));
                    schedule.setTutor(tutor);

                    User user = this.userRepository.findById(scheduleDTO.getUserDTO().getId())
                            .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
                    schedule.setUser(user);

                    return this.schedulerMapper.toDTO(this.scheduleRepository.save(schedule));
                })
                .orElseThrow(() -> new IllegalArgumentException("El Schedule no existe"));
    }

    @Override
    public void deleteSchedule(Long id) {
        this.scheduleRepository.deleteById(id);
    }
}
