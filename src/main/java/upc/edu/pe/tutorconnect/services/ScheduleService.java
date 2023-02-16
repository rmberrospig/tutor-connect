package upc.edu.pe.tutorconnect.services;

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

import java.util.List;

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
    public List<ScheduleDTO> findAllSchedule() {
        return this.schedulerMapper.getSchedulersDTO(this.scheduleRepository.findAll());
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
    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = this.schedulerMapper.toEntity(scheduleDTO);
        Tutor tutor = this.tutorRepository.findById(schedule.getTutor().getId()).orElse(null);
        schedule.setTutor(tutor);
        User user = this.userRepository.findById(schedule.getUser().getId()).orElse(null);
        schedule.setUser(user);
        return this.schedulerMapper.toDTO(this.scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        Schedule schedule = this.schedulerMapper.toEntity(scheduleDTO);
        return this.schedulerMapper.toDTO(this.scheduleRepository.save(schedule));
    }

    @Override
    public void deleteSchedule(Long id) {
        this.scheduleRepository.deleteById(id);
    }
}
