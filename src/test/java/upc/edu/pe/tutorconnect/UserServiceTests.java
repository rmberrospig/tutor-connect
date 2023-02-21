package upc.edu.pe.tutorconnect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.entities.UserType;
import upc.edu.pe.tutorconnect.mappers.ISubjectMapper;
import upc.edu.pe.tutorconnect.mappers.IUserMapper;
import upc.edu.pe.tutorconnect.repositories.ISubjectRepository;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;
import upc.edu.pe.tutorconnect.services.ISubjectService;
import upc.edu.pe.tutorconnect.services.IUserService;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTests {
    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private ISubjectRepository subjectRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private ISubjectMapper subjectMapper;

    @Test
    void findAllUserTest() throws ServiceException {
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", null, null));
                add(new User(2L,"pepe","password","Pepe","Perez", "pepe@gmail.com", null, null));
                add(new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", null, null));
            }
        };

        when(this.userRepository.findAll()).thenReturn(lst);

        List<UserDTO> result = this.userService.findAllUser();
        Assertions.assertEquals(3, result.size());
        verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllStudentsTest() throws ServiceException {
        Long userType = 2L;
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null));
                add(new User(2L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
                add(new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
            }
        };

        when(this.userRepository.findAllByUserTypeIdEquals(userType)).thenReturn(lst.stream().filter(user -> userType.equals(user.getUserType().getId())).collect(Collectors.toList()));

        List<UserDTO> result = this.userService.findAllStudents();
        Assertions.assertEquals(2, result.size());
        verify(userRepository, Mockito.times(1)).findAllByUserTypeIdEquals(userType);
    }

    @Test
    void findAllTutorTest() throws ServiceException {
        Long userType = 1L;
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null));
                add(new User(2L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
                add(new User(3L,"ana","password","Ana","Perez", "rmberrospig@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
            }
        };

        when(this.userRepository.findAllByUserTypeIdEquals(userType)).thenReturn(lst.stream().filter(user -> userType.equals(user.getUserType().getId())).collect(Collectors.toList()));

        List<UserDTO> result = this.userService.findAllTutor();
        Assertions.assertEquals(1, result.size());
        verify(userRepository, Mockito.times(1)).findAllByUserTypeIdEquals(userType);
    }

    @Test
    void findByUsernameTest() throws ServiceException {
        String username = "rmberrospig";
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", null, null));
                add(new User(2L,"pepe","password","Pepe","Perez", "pepe@gmail.com", null, null));
                add(new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", null, null));
            }
        };
        when(this.userRepository.findByUsername(username)).thenReturn(lst.stream().filter(user -> username.equals(user.getUsername())).findAny());

        UserDTO result = this.userService.findByUsername(username);
        Assertions.assertEquals("Rodrigo", result.getFirstName());
        verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void findByEmailTest() throws ServiceException {
        String email = "rmberrospig@gmail.com";
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", null, null));
                add(new User(2L,"pepe","password","Pepe"," Perez", "pepe@gmail.com", null, null));
                add(new User(3L,"ana","password","Ana"," Perez", "ana@gmail.com", null, null));
            }
        };
        when(this.userRepository.findByEmail(email)).thenReturn(lst.stream().filter(user -> email.equals(user.getEmail())).findAny());

        UserDTO result = this.userService.findByEmail(email);
        Assertions.assertEquals("Berrospi", result.getLastName());
        verify(userRepository, Mockito.times(1)).findByEmail(email);
    }

    @Test
    void findTutorByIdTest() throws ServiceException {
        Long userType = 1L;
        Long id = 1L;
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null));
                add(new User(2L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
                add(new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
            }
        };
        when(this.userRepository.findByIdAndUserTypeId(id, userType)).thenReturn(lst.stream().filter(user -> id.equals(user.getId()) && userType.equals(user.getUserType().getId())).findAny());

        UserDTO result = this.userService.findTutorById(id);
        Assertions.assertEquals("Berrospi", result.getLastName());
        verify(userRepository, Mockito.times(1)).findByIdAndUserTypeId(id, userType);
    }

    @Test
    void findStudentByIdTest() throws ServiceException {
        Long userType = 2L;
        Long id = 3L;
        List<User> lst = new ArrayList<User>() {
            {
                add(new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null));
                add(new User(2L,"pepe","password","Pepe","Perez", "pepe@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
                add(new User(3L,"ana","password","Ana","Perez", "ana@gmail.com", new UserType(2L, "ESTUDIANTE"), null));
            }
        };
        when(this.userRepository.findByIdAndUserTypeId(id, userType)).thenReturn(lst.stream().filter(user -> id.equals(user.getId()) && userType.equals(user.getUserType().getId())).findAny());

        UserDTO result = this.userService.findStudentById(id);
        Assertions.assertEquals("Ana", result.getFirstName());
        verify(userRepository, Mockito.times(1)).findByIdAndUserTypeId(id, userType);
    }

    @Test
    void saveUserTest() throws ServiceException {
        Subject subject = new Subject(1L, "Calculo I", null);
        User user =  new User(1L,"rmberrospig","password","Rodrigo Miguel","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null);
        Tutor tutor = new Tutor(1L, "Tutor con experiencia", 60.00, user, new ArrayList<Subject>() {{
            add(subject);
        }});
        user.setTutor(tutor);

        when(this.userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = this.userMapper.toDTO(user);

        UserDTO result = this.userService.saveUser(userDTO);
        Assertions.assertEquals(userDTO.getFirstName(), result.getFirstName());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void updateUserTest() throws ServiceException {
        Long id = 1L;
        User user =  new User(1L,"rmberrospig","password","Rodrigo","Berrospi", "rmberrospig@gmail.com", new UserType(1L, "TUTOR"), null);
        Tutor tutor = new Tutor(1L, "Tutor con experiencia", 60.00, user, new ArrayList<Subject>() {{
            add(new Subject(1L, "Calculo I", null));
        }});
        user.setTutor(tutor);

        when(this.userRepository.findById(id)).thenReturn(Optional.of(user));
        when(this.userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = this.userMapper.toDTO(user);

        UserDTO result = this.userService.updateUser(id, userDTO);
        Assertions.assertEquals(userDTO.getFirstName(), result.getFirstName());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void getListSubjectsTest() throws ServiceException {
        List<Subject> subjects = new ArrayList<Subject>(){
            {
                add(new Subject(1L, "Calculo I", null));
                add(new Subject(2L, "Calculo II", null));
            }
        };
        List<SubjectDTO> subjectDTOS = this.subjectMapper.getSubjectsDTO(subjects);

        when(this.subjectRepository.findById(1L)).thenReturn(subjects.stream().filter(subject -> subject.getId().equals(1L)).findAny());
        when(this.subjectRepository.findById(2L)).thenReturn(subjects.stream().filter(subject -> subject.getId().equals(2L)).findAny());

        List<Subject> result = userService.getListSubjects(subjectDTOS);
        Assertions.assertEquals(2, result.size());

    }


}
