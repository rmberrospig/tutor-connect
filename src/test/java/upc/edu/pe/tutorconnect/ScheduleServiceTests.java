package upc.edu.pe.tutorconnect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import upc.edu.pe.tutorconnect.dtos.*;
import upc.edu.pe.tutorconnect.entities.*;
import upc.edu.pe.tutorconnect.mappers.ISchedulerMapper;
import upc.edu.pe.tutorconnect.repositories.IScheduleRepository;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;
import upc.edu.pe.tutorconnect.services.IScheduleService;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ScheduleServiceTests {

    @MockBean
    private IScheduleRepository scheduleRepository;
    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private ITutorRepository tutorRepository;
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private ISchedulerMapper schedulerMapper;

    @Test
    void findScheduleByIdTest(){
        Long id = 1L;
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, null));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), null, null));
            }
        };

        when(this.scheduleRepository.findById(id)).thenReturn(lst.stream().filter(schedule -> id.equals(schedule.getId())).findAny());

        ScheduleDTO result = this.scheduleService.findById(id);
        Assertions.assertEquals(LocalDate.parse("2023-02-03"), result.getDate());
        verify(scheduleRepository, Mockito.times(1)).findById(id);
    }
    @Test
    void findAllScheduleTest() {
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, null));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), null, null));
            }
        };

        when(this.scheduleRepository.findAll()).thenReturn(lst);
        List<ScheduleDTO> result = this.scheduleService.findAllSchedule();
        Assertions.assertEquals(2, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAll();
    }
    @Test
    void findAllScheduleAvailableTest() {
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, null));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), null));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), null));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, null));
            }
        };

        when(this.scheduleRepository.findAllByUserIdIsNull()).thenReturn(lst.stream().filter(schedule -> schedule.getUser() == null).collect(Collectors.toList()));
        List<ScheduleDTO> result = this.scheduleService.findAllScheduleAvailable();
        Assertions.assertEquals(2, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAllByUserIdIsNull();

    }

    @Test
    void findAllScheduleAvailableByDateTest() {
        LocalDate date = LocalDate.parse("2023-02-04");
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, null));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), null));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), null));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, null));
            }
        };

        when(this.scheduleRepository.findAllByDateAndUserIdIsNull(date)).thenReturn(lst.stream().filter(schedule -> schedule.getUser() == null && date.equals(schedule.getDate())).collect(Collectors.toList()));
        List<ScheduleDTO> result = this.scheduleService.findAllScheduleAvailableByDate(date);
        Assertions.assertEquals(1, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAllByDateAndUserIdIsNull(date);
    }

    @Test
    void findAllScheduleAvailableByTutorTest() {
        Long id = 1L;
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(3L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
            }
        };

        when(this.scheduleRepository.findAllByTutorIdAndUserIdIsNull(id)).thenReturn(lst.stream().filter(schedule -> schedule.getUser() == null && id.equals(schedule.getTutor().getId())).collect(Collectors.toList()));
        List<ScheduleDTO> result = this.scheduleService.findAllScheduleAvailableByTutor(id);
        Assertions.assertEquals(2, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAllByTutorIdAndUserIdIsNull(id);

    }

    @Test
    void findAllScheduleAvailableByTutorAndDateTest() {
        LocalDate date = LocalDate.parse("2023-02-04");
        Long id = 1L;
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(3L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
            }
        };

        when(this.scheduleRepository.findAllByTutorIdAndDateAndUserIdIsNull(id, date)).thenReturn(lst.stream().filter(schedule -> schedule.getUser() == null && id.equals(schedule.getTutor().getId()) && date.equals(schedule.getDate())).collect(Collectors.toList()));
        List<ScheduleDTO> result = this.scheduleService.findAllScheduleAvailableByTutorAndDate(id, date);
        Assertions.assertEquals(1, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAllByTutorIdAndDateAndUserIdIsNull(id, date);

    }

    @Test
    void findAllScheduleByTutorTest() {
        Long id = 1L;
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(3L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
            }
        };

        when(this.scheduleRepository.findAllByTutorIdEquals(id, Sort.by(Sort.Direction.ASC, "id"))).thenReturn(lst.stream().filter(schedule -> id.equals(schedule.getTutor().getId())).collect(Collectors.toList()));
        List<ScheduleDTO> result = this.scheduleService.findAllScheduleByTutor(id);
        Assertions.assertEquals(3, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAllByTutorIdEquals(id, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void findAllScheduleByStudentTest() {
        Long id = 3L;
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(3L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
            }
        };

        when(this.scheduleRepository.findAllByUserIdEquals(id, Sort.by(Sort.Direction.ASC, "id"))).thenReturn(lst.stream().filter(schedule -> schedule.getUser() != null && id.equals(schedule.getUser().getId())).collect(Collectors.toList()));
        List<ScheduleDTO> result = this.scheduleService.findAllScheduleByStudent(id);
        Assertions.assertEquals(1, result.size());
        verify(scheduleRepository, Mockito.times(1)).findAllByUserIdEquals(id, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void isValidRangeTimeTest() {
        LocalDate date = LocalDate.parse("2023-02-04");
        Long id = 1L;
        List<Schedule> lst = new ArrayList<Schedule>() {
            {
                add(new Schedule(1L, LocalDate.parse("2023-02-03"), LocalTime.parse("10:00"),LocalTime.parse("11:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(2L, LocalDate.parse("2023-02-04"), LocalTime.parse("11:00"),LocalTime.parse("12:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(3L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(3L, LocalDate.parse("2023-02-04"), LocalTime.parse("12:00"),LocalTime.parse("13:00"), new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
                add(new Schedule(4L, LocalDate.parse("2023-02-04"), LocalTime.parse("13:00"),LocalTime.parse("14:00"), null, new Tutor(1L,"Tutor con 5 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>())));
            }
        };

        ScheduleDTO scheduleDTO = new ScheduleDTO(5L, LocalDate.parse("2023-02-04"), "13:00","12:00", new UserDTO(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserTypeDTO(2L, "ESTUDIANTE"), null), new TutorDTO(id,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<SubjectDTO>()));
        List<Map<String, String>> result = this.scheduleService.isValidRangeTime(scheduleDTO);
        Assertions.assertEquals(1, result.size());

        scheduleDTO = new ScheduleDTO(5L, LocalDate.parse("2023-02-04"), "10:00","12:00", new UserDTO(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserTypeDTO(2L, "ESTUDIANTE"), null), new TutorDTO(id,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<SubjectDTO>()));
        result = this.scheduleService.isValidRangeTime(scheduleDTO);
        Assertions.assertEquals(1, result.size());

        when(this.scheduleRepository.findAllByTutorIdAndDate(id, date)).thenReturn(lst.stream().filter(schedule -> date.equals(schedule.getDate()) && id.equals(schedule.getTutor().getId())).collect(Collectors.toList()));
        scheduleDTO = new ScheduleDTO(5L, LocalDate.parse("2023-02-04"), "12:00","13:00", new UserDTO(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserTypeDTO(2L, "ESTUDIANTE"), null), new TutorDTO(id,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<SubjectDTO>()));
        result = this.scheduleService.isValidRangeTime(scheduleDTO);
        Assertions.assertEquals(2, result.size());
        //verify(scheduleRepository, Mockito.times(1)).findAllByTutorIdAndDate(id, date);

        //when(this.scheduleRepository.findAllByTutorIdAndDate(id, date)).thenReturn(lst.stream().filter(schedule -> date.equals(schedule.getDate()) && id.equals(schedule.getTutor().getId())).collect(Collectors.toList()));
        scheduleDTO = new ScheduleDTO(5L, LocalDate.parse("2023-02-04"), "12:30","13:30", new UserDTO(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserTypeDTO(2L, "ESTUDIANTE"), null), new TutorDTO(id,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<SubjectDTO>()));
        result = this.scheduleService.isValidRangeTime(scheduleDTO);
        Assertions.assertEquals(2, result.size());
        verify(scheduleRepository, Mockito.times(2)).findAllByTutorIdAndDate(id, date);

        scheduleDTO = new ScheduleDTO(5L, LocalDate.parse("2023-02-04"), "15:00","16:00", new UserDTO(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserTypeDTO(2L, "ESTUDIANTE"), null), new TutorDTO(id,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<SubjectDTO>()));
        result = this.scheduleService.isValidRangeTime(scheduleDTO);
        Assertions.assertEquals(0, result.size());
        verify(scheduleRepository, Mockito.times(3)).findAllByTutorIdAndDate(id, date);
    }

    @Test
    void saveScheduleTest() {
        Schedule schedule = new Schedule(5L, LocalDate.parse("2023-02-04"), LocalTime.parse("15:00"),LocalTime.parse("16:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>()));

        when(this.scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        ScheduleDTO scheduleDTO = this.schedulerMapper.toDTO(schedule);
        ScheduleDTO result = this.scheduleService.saveSchedule(scheduleDTO);
        Assertions.assertEquals(5, result.getId());
        verify(scheduleRepository, Mockito.times(1)).save(any(Schedule.class));
    }

    @Test
    void updateScheduleTest() {
        Long id = 1L;
        Long userId = 1L;
        Long tutorId = 1L;

        Schedule schedule = new Schedule(id, LocalDate.parse("2023-02-04"), LocalTime.parse("15:00"),LocalTime.parse("16:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>()));
        User user =  new User(userId,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null);
        Tutor tutor = new Tutor(tutorId, "Tutor con experiencia","productImage", "profileImage", 60.00, user, new ArrayList<Subject>() {{
            add(new Subject(1L, "Calculo I", null));
        }});
        user.setTutor(tutor);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.tutorRepository.findById(tutorId)).thenReturn(Optional.of(tutor));
        when(this.scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        when(this.scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        ScheduleDTO scheduleDTO = this.schedulerMapper.toDTO(schedule);
        ScheduleDTO result = this.scheduleService.updateSchedule(id, scheduleDTO);

        Assertions.assertEquals(1, result.getId());
        verify(scheduleRepository, Mockito.times(1)).save(any(Schedule.class));

    }

    @Test
    void deleteScheduleTest() throws ServiceException {
        Long id = 1L;
        Schedule schedule = new Schedule(id, LocalDate.parse("2023-02-04"), LocalTime.parse("15:00"),LocalTime.parse("16:00"), new User(1L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null), new Tutor(1L,"Tutor con 15 años de experiencia","productImage", "profileImage",60.00, null, new ArrayList<Subject>()));
        Mockito.when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        scheduleService.deleteSchedule(schedule.getId());
        verify(scheduleRepository, Mockito.times(1)).deleteById(id);
    }
}
