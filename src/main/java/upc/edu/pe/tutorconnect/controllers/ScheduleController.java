package upc.edu.pe.tutorconnect.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllScheduler() {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllSchedule();
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/available/")
    public ResponseEntity<?> getAllSchedulerAvailable() {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleAvailable();
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/available")
    public ResponseEntity<?> getAllSchedulerAvailableByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date) {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleAvailableByDate(date);
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/tutor/{id}")
    public ResponseEntity<?> getAllSchedulerTutorById(@PathVariable Long id) {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleByTutor(id);
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/tutor/{id}/available/")
    public ResponseEntity<?> getAllSchedulerAvailableByTutorId(@PathVariable Long id) {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleAvailableByTutor(id);
            if(lst == null || lst.isEmpty()){
                return super.getNotContentRequest();
            }
            return super.getSuccessRequest(lst);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @GetMapping(value = "/tutor/{id}/available")
    public ResponseEntity<?> getAllSchedulerAvailableByTutorId(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date) {
        try {
            List<ScheduleDTO> lst = this.scheduleService.findAllScheduleAvailableByTutorAndDate(id, date);
            if(lst == null || lst.isEmpty()){
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
            if(lst == null || lst.isEmpty()){
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
            //UserDTO studentDTO = this.userService.findStudentById(scheduleDTO.getUserDTO().getId());
            //if(studentDTO == null)  return super.getBadRequest("Estudiante no se encuentra registrado");

            List<Map<String, String>> resultValidRangeTime = this.scheduleService.isValidRangeTime(scheduleDTO);
            if(resultValidRangeTime.size() > 0) {
                return super.getBadRequest(resultValidRangeTime);
            }

            ScheduleDTO resultScheduleDTO = this.scheduleService.saveSchedule(scheduleDTO);
            //ScheduleDTO resultScheduleDTO = null;
            return super.getCreatedRequest(resultScheduleDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> saveSchedule(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO, BindingResult result) {
        if(result.hasErrors()){
            return super.getBadRequest(result);
        }

        ScheduleDTO findScheduleDTO = this.scheduleService.findById(id);
        if(findScheduleDTO == null) return super.getBadRequest("Horario no se encuentra registrado");

        try {
            TutorDTO tutorDTO = this.tutorService.findTutorById(scheduleDTO.getTutorDTO().getId());
            if(tutorDTO == null)  return super.getBadRequest("Tutor no se encuentra registrado");
            UserDTO studentDTO = this.userService.findStudentById(scheduleDTO.getUserDTO().getId());
            if(studentDTO == null)  return super.getBadRequest("Estudiante no se encuentra registrado");

            //List<Map<String, String>> resultValidRangeTime = this.scheduleService.isValidRangeTime(scheduleDTO);
            //if(resultValidRangeTime.size() > 0) {
            //    return super.getBadRequest(resultValidRangeTime);
            //}

            ScheduleDTO resultScheduleDTO = this.scheduleService.updateSchedule(id, scheduleDTO);
            //ScheduleDTO resultScheduleDTO = null;
            return super.getCreatedRequest(resultScheduleDTO);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }
    }
}
