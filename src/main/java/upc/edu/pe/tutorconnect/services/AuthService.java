package upc.edu.pe.tutorconnect.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.AuthRequestDTO;
import upc.edu.pe.tutorconnect.dtos.AuthResponseDTO;
import upc.edu.pe.tutorconnect.entities.User;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        UserDetails user = null;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        } catch (AuthenticationException e){
            throw new BadCredentialsException("Username o password no válidos");
        }
        String jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
