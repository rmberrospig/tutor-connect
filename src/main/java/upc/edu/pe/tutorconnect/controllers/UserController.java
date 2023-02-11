package upc.edu.pe.tutorconnect.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upc.edu.pe.tutorconnect.services.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(this.userService.findAllUser());
    }
}
