package upc.edu.pe.tutorconnect.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.tutorconnect.controllers.constants.ResponseConstant;
import upc.edu.pe.tutorconnect.controllers.generic.GenericController;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.services.ISubjectService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ResponseConstant.API_SUBJECT)
public class SubjectController extends GenericController {
    @Autowired
    private ISubjectService subjectService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllSubjects() {
        try {
            List<SubjectDTO> lst = this.subjectService.findAllSubjects();
            if(lst == null || lst.isEmpty()) {
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findSubjectById(@PathVariable Long id) {
        try {
            SubjectDTO subjectDTO = this.subjectService.findById(id);
            if(subjectDTO == null){
                return super.getNotFoundRequest();
            }
            return super.getSuccessRequest(subjectDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> saveSubject(@Valid @RequestBody SubjectDTO subjectDTO, BindingResult result) {
        if(result.hasErrors()){
            return super.getBadRequest(result);
        }

        try {
            SubjectDTO userValid = this.subjectService.findByName(subjectDTO.getName().toUpperCase());
            if(userValid != null)  return super.getBadRequest("Curso ya se encuentra registrado");

            SubjectDTO resultSubjectDTO = this.subjectService.saveSubject(subjectDTO);
            return super.getCreatedRequest(resultSubjectDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, @RequestBody SubjectDTO subjectDTO) {
        try {
            SubjectDTO result = this.subjectService.updateSubject(id, subjectDTO);
            if(result == null) return super.getNotFoundRequest();
            return super.getSuccessRequest(result);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }
}
