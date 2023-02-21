package upc.edu.pe.tutorconnect.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.tutorconnect.controllers.commons.ResponseREST;
import upc.edu.pe.tutorconnect.controllers.constants.ResponseConstant;
import upc.edu.pe.tutorconnect.controllers.generic.GenericController;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.services.ISubjectService;
import upc.edu.pe.tutorconnect.services.ITutorService;
import upc.edu.pe.tutorconnect.services.IUserService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(ResponseConstant.API_TUTOR)
public class TutorController extends GenericController {
    @Autowired
    private ITutorService tutorService;


    @GetMapping(value = "/")
    public ResponseEntity<ResponseREST> getAllTutor() {
        try {
            List<TutorDTO> lst = this.tutorService.findAllTutor();
            if (lst == null || lst.isEmpty()) {
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseREST> getTutorById(@PathVariable Long id) {
        try {
            TutorDTO tutorDTO = this.tutorService.findTutorById(id);
            if (tutorDTO == null) {
                return super.getNotFoundRequest();
            }
            return super.getSuccessRequest(tutorDTO);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }


}
