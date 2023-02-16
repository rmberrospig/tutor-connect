package upc.edu.pe.tutorconnect.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.tutorconnect.controllers.constants.ResponseConstant;
import upc.edu.pe.tutorconnect.controllers.generic.GenericController;
import upc.edu.pe.tutorconnect.dtos.ScheduleDTO;
import upc.edu.pe.tutorconnect.dtos.TutorDTO;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.services.IScheduleService;
import upc.edu.pe.tutorconnect.services.ITutorService;
import upc.edu.pe.tutorconnect.services.IUserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ResponseConstant.API_SCHEDULE)
public class ScheduleController extends GenericController {

    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITutorService tutorService;

    @GetMapping(value = "/tutor/{id}")
    public ResponseEntity<?> getAllSchedulerTutorById(@PathVariable Long id) {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleByTutor(id);
            if(lst == null || lst.size() == 0){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/student/{id}")
    public ResponseEntity<?> getAllSchedulerStudentById(@PathVariable Long id) {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleByStudent(id);
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
    public ResponseEntity<?> saveSchedule(@RequestBody ScheduleDTO scheduleDTO, BindingResult result) {
        if(result.hasErrors()){
            return super.getBadRequest(result);
        }

        try {

            TutorDTO tutorDTO = this.tutorService.findTutorById(scheduleDTO.getTutorDTO().getId());
            if(tutorDTO == null)  return super.getBadRequest("Tutor no se encuentra registrado");
            UserDTO studentDTO = this.userService.findStudentById(scheduleDTO.getUserDTO().getId());
            if(studentDTO == null)  return super.getBadRequest("Estudiante no se encuentra registrado");

            ScheduleDTO resultScheduleDTO = this.scheduleService.saveSchedule(scheduleDTO);
            return super.getCreatedRequest(resultScheduleDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }
}
