package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.entities.Subject;
import upc.edu.pe.tutorconnect.mappers.ISubjectMapper;
import upc.edu.pe.tutorconnect.repositories.ISubjectRepository;
import upc.edu.pe.tutorconnect.services.exceptions.ServiceException;

import java.util.List;

@Service
public class SubjectService implements ISubjectService {

    @Autowired
    private ISubjectRepository subjectRepository;

    @Autowired
    private ISubjectMapper subjectMapper;

    @Override
    public List<SubjectDTO> findAllSubjects() throws ServiceException {
        return this.subjectMapper.getSubjectsDTO(this.subjectRepository.findAll());
    }

    @Override
    public SubjectDTO findById(Long id) throws ServiceException {
        Subject subject = this.subjectRepository.findById(id).orElse(null);
        return this.subjectMapper.toDTO(subject);
    }

    @Override
    public SubjectDTO findByName(String name) throws ServiceException {
        return this.subjectMapper.toDTO(this.subjectRepository.findByName(name).orElse(null));
    }

    @Override
    public SubjectDTO saveSubject(SubjectDTO subjectDTO) throws ServiceException {
        subjectDTO.setName(subjectDTO.getName().toUpperCase());
        Subject subject = this.subjectMapper.toEntity(subjectDTO);
        return this.subjectMapper.toDTO(this.subjectRepository.save(subject));
    }

    @Override
    public SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) throws ServiceException {
        SubjectDTO findSubject = this.findById(id);
        if(findSubject == null)  return null;
        subjectDTO.setId(id);
        subjectDTO.setName(subjectDTO.getName().toUpperCase());
        Subject subject = this.subjectMapper.toEntity(subjectDTO);
        return this.subjectMapper.toDTO(this.subjectRepository.save(subject));
    }

    @Override
    public void deleteSubject(Long id) throws ServiceException {
        SubjectDTO findSubject = this.findById(id);
        if(findSubject == null)  throw new ServiceException("Curso con ID: " + String.valueOf(id) + " no existe");
        this.subjectRepository.deleteById(id);
    }
}
