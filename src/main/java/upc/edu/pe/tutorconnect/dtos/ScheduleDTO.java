package upc.edu.pe.tutorconnect.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

    private Long id;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private UserDTO userDTO;
    private TutorDTO tutorDTO;
}
