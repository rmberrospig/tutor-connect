package upc.edu.pe.tutorconnect.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upc.edu.pe.tutorconnect.controllers.constants.ResponseConstant;
import upc.edu.pe.tutorconnect.controllers.generic.GenericController;
import upc.edu.pe.tutorconnect.dtos.AuthRequestDTO;
import upc.edu.pe.tutorconnect.services.AuthService;

@Slf4j
@RestController
@RequestMapping(ResponseConstant.API_AUTH)
public class AuthController extends GenericController {

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO request) {

        try{
            return super.getSuccessRequest(authService.authenticate(request));

        } catch (AuthenticationException ex){
            return super.getBadRequest(ex.getMessage());
        } catch (Exception ex){
            log.error(ex.getMessage());
            return super.getErrorRequest();
        }

    }
}
