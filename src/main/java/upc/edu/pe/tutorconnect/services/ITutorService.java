package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.TutorDTO;

import java.util.List;

public interface ITutorService {
    TutorDTO findTutorById(Long id);

    List<TutorDTO> findAllTutor();
}
