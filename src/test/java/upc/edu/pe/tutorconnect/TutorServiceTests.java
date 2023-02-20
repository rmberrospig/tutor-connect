package upc.edu.pe.tutorconnect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;
import upc.edu.pe.tutorconnect.services.ITutorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TutorServiceTests {
    @MockBean
    private ITutorRepository tutorRepository;
    @Autowired
    private ITutorService tutorService;

    @Test
    void findTutorByIdTest() {
        Long id = 2L;
        List<Tutor> lst = new ArrayList<Tutor>() {
            {
                add(new Tutor(1L,"Tutor con 5 años de experiencia",60.00, null, new ArrayList<Subject>()));
                add(new Tutor(2L,"Tutor con 7 años de experiencia",80.00, null, new ArrayList<Subject>()));
            }
        };
        when(this.tutorRepository.findById(id)).thenReturn(lst.stream().filter(tutor -> id.equals(tutor.getId())).findAny());

        TutorDTO result = this.tutorService.findTutorById(id);
        Assertions.assertEquals(80.00, result.getPricePerHour());
        verify(tutorRepository, Mockito.times(1)).findById(id);
    }
}
