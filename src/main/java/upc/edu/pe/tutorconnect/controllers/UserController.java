package upc.edu.pe.tutorconnect.controllers;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.tutorconnect.controllers.constants.ResponseConstant;
import upc.edu.pe.tutorconnect.controllers.generic.GenericController;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.services.IUserService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(ResponseConstant.API_USER)
public class UserController extends GenericController {
    @Autowired
    private IUserService userService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> lst = this.userService.findAllUser();
            if(lst == null || lst.size() == 0){
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
            if(lst == null || lst.size() == 0){
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
            if(lst == null || lst.size() == 0){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO result = this.userService.saveUser(userDTO);
            return super.getSuccessRequest(result);
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
