package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.TutorDTO;

public interface ITutorService {
    TutorDTO findTutorById(Long id);
}
