package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.entities.Tutor;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.mappers.ITutorMapper;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;

import java.util.List;

@Service
public class TutorService implements ITutorService {
    @Autowired
    private ITutorRepository tutorRepository;
    @Autowired
    private ITutorMapper tutorMapper;

    @Override
    public TutorDTO findTutorById(Long id) {
        return this.tutorMapper.toDTO(this.tutorRepository.findById(id).orElse(null));
    }

    @Override
    public List<TutorDTO> findAllTutor() {
        List<Tutor> lst = this.tutorRepository.findAll();
        return this.tutorMapper.getTutorsDTO(lst);
    }
}
