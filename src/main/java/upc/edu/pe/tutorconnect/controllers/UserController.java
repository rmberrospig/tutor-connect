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
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.services.ISubjectService;
import upc.edu.pe.tutorconnect.services.IUserService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(ResponseConstant.API_USER)
public class UserController extends GenericController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISubjectService subjectService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> lst = this.userService.findAllUser();
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/students/")
    public ResponseEntity<?> getAllStudents() {
        try {
            List<UserDTO> lst = this.userService.findAllStudents();
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/tutors/")
    public ResponseEntity<?> getAllTutors() {
        try {
            List<UserDTO> lst = this.userService.findAllTutor();
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/tutors/{id}")
    public ResponseEntity<?> getTutorById(@PathVariable Long id) {
        try {
            UserDTO userDTO = this.userService.findTutorById(id);
            if(userDTO == null){
                return super.getNotFoundRequest();
            }
            return super.getSuccessRequest(userDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            UserDTO userDTO = this.userService.findStudentById(id);
            if(userDTO == null){
                return super.getNotFoundRequest();
            }
            return super.getSuccessRequest(userDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if(result.hasErrors()){
            return super.getBadRequest(result);
        }

        try {
            UserDTO userValid = this.userService.findByUsername(userDTO.getUsername());
            if(userValid != null)  return super.getBadRequest("Username ya se encuentra registrado");
            userValid = this.userService.findByEmail(userDTO.getEmail());
            if(userValid != null)  return super.getBadRequest("Email ya se encuentra registrado");
            if(userDTO.getUserTypeDTO().getId() == 1) {
                for(SubjectDTO subject: userDTO.getTutorDTO().getSubjectsDTO()){
                    SubjectDTO subjectValid = this.subjectService.findById(subject.getId());
                    if(subjectValid == null)  return super.getBadRequest("Curso no se encuentra registrado");
                }
            }

            UserDTO resultUserDTO = this.userService.saveUser(userDTO);
            return super.getCreatedRequest(resultUserDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO result = this.userService.updateUser(id, userDTO);
            if(result == null) return super.getNotFoundRequest();
            return super.getSuccessRequest(result);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }
}
