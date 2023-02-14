package upc.edu.pe.tutorconnect.services;

import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.List;

public interface ISubjectService {

    List<SubjectDTO> findAllSubjects() throws ServiceException;

    SubjectDTO findById(Long id) throws ServiceException;

    SubjectDTO findByName(String name) throws ServiceException;

    SubjectDTO saveSubject(SubjectDTO subjectDTO) throws ServiceException;


    SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) throws ServiceException;

    void deleteSubject(Long id) throws ServiceException;
}
