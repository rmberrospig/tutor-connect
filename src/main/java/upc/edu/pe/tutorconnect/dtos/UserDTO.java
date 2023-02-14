package upc.edu.pe.tutorconnect.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "Username es obligatorio")
    @NotBlank(message = "Username no debe estar vacio")
    private String username;

    @NotBlank(message = "Password no debe estar vacio")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String firstName;
    private String lastName;

    @NotNull(message = "Email es obligatorio")
    @Email(message = "Formato No Válido")
    private String email;
    private UserTypeDTO userTypeDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TutorDTO tutorDTO;


}
