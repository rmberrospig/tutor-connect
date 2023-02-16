package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.mappers.ITutorMapper;
import upc.edu.pe.tutorconnect.repositories.ITutorRepository;

@Service
public class TutorService implements  ITutorService {
    @Autowired
    private ITutorRepository tutorRepository;
    @Autowired
    private ITutorMapper tutorMapper;

    @Override
    public TutorDTO findTutorById(Long id) {
        return this.tutorMapper.toDTO(this.tutorRepository.findById(id).orElse(null));
    }
}
