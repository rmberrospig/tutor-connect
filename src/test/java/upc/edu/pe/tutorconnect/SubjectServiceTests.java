package upc.edu.pe.tutorconnect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.mappers.ISubjectMapper;
import upc.edu.pe.tutorconnect.repositories.ISubjectRepository;
import upc.edu.pe.tutorconnect.services.ISubjectService;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SubjectServiceTests {

    @MockBean
    private ISubjectRepository subjectRepository;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private ISubjectMapper subjectMapper;

    @Test
    void findAllSubjectsTest() throws ServiceException {
        List<Subject> lst = new ArrayList<Subject>() {
            {
                add(new Subject(1L,"Cálculo I", null));
                add(new Subject(2L,"Cálculo II", null));
                add(new Subject(3L,"Física I",null));
            }
        };

        when(this.subjectRepository.findAll()).thenReturn(lst);

        List<SubjectDTO> result = this.subjectService.findAllSubjects();
        Assertions.assertEquals(3, result.size());
        verify(subjectRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findSubjectByIdTest() throws ServiceException {
        Long subjectId1 = 1L;
        List<Subject> lst = new ArrayList<Subject>() {
            {
                add(new Subject(1L,"Cálculo I", null));
                add(new Subject(2L,"Cálculo II", null));
                add(new Subject(3L,"Física I",null));
            }
        };

        when(this.subjectRepository.findById(subjectId1)).thenReturn(lst.stream().filter(subject -> subjectId1.equals(subject.getId())).findAny());

        SubjectDTO result = this.subjectService.findById(subjectId1);
        Assertions.assertEquals("Cálculo I", result.getName());
        verify(subjectRepository, Mockito.times(1)).findById(subjectId1);

        Long subjectId2 = 4L;
        when(this.subjectRepository.findById(subjectId2)).thenReturn(lst.stream().filter(subject -> subjectId2.equals(subject.getId())).findAny());
        result = this.subjectService.findById(subjectId2);
        Assertions.assertEquals(null, result);

        verify(subjectRepository, Mockito.times(1)).findById(subjectId2);
    }

    @Test
    void findSubjectByNameTest() throws ServiceException {
        String name = "Cálculo I";
        List<Subject> lst = new ArrayList<Subject>() {
            {
                add(new Subject(1L,"Cálculo I", null));
                add(new Subject(2L,"Cálculo II", null));
                add(new Subject(3L,"Física I",null));
            }
        };

        when(this.subjectRepository.findByName(name)).thenReturn(lst.stream().filter(subject -> name.equals(subject.getName())).findAny());

        SubjectDTO result = this.subjectService.findByName(name);
        Assertions.assertEquals("Cálculo I", result.getName());
        verify(subjectRepository, Mockito.times(1)).findByName(name);
    }

    @Test
    void saveSubjectTest() throws ServiceException {
        String name = "Cálculo I";
        Subject subject = new Subject(1L,"CÁLCULO I", new ArrayList<Tutor>());

        when(this.subjectRepository.save(any(Subject.class))).thenReturn(subject);

        SubjectDTO subjectDTO = this.subjectMapper.toDTO(subject);

        SubjectDTO result = this.subjectService.saveSubject(subjectDTO);
        Assertions.assertEquals(name.toUpperCase(), result.getName());
        verify(subjectRepository, Mockito.times(1)).save(subject);
    }

    @Test
    void updateSubjectTest() throws ServiceException {
        Long id = 1L;
        String name = "Cálculo II";
        Subject subject = new Subject(1L,"CÁLCULO II", new ArrayList<Tutor>());

        when(this.subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(this.subjectRepository.save(any(Subject.class))).thenReturn(subject);

        SubjectDTO subjectDTO = this.subjectMapper.toDTO(subject);
        subjectDTO.setName(name);

        SubjectDTO result = this.subjectService.updateSubject(id, subjectDTO);
        Assertions.assertEquals(name.toUpperCase(), result.getName());
        verify(subjectRepository, Mockito.times(1)).save(subject);
    }

    @Test
    void deleteSubjectTest() throws ServiceException {
        Long id = 1L;
        Subject subject = new Subject(1L,"CÁLCULO II", new ArrayList<Tutor>());
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        subjectService.deleteSubject(subject.getId());
        verify(subjectRepository, Mockito.times(1)).deleteById(id);

        id = 4L;
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        try{
            subjectService.deleteSubject(id);
        }catch (Exception ex) {
            Assertions.assertEquals("Curso con ID: " + id.toString() + " no existe",ex.getMessage());
        }
        verify(subjectRepository, Mockito.times(1)).deleteById(id);
    }
}
