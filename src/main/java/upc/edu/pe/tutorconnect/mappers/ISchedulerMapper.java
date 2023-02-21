package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.edu.pe.tutorconnect.dtos.ScheduleDTO;
import upc.edu.pe.tutorconnect.entities.Schedule;

import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ISchedulerMapper {

    @Mapping(source = "userDTO", target = "user")
    @Mapping(source = "tutorDTO", target = "tutor")
    @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "startTime", expression = "java(getTime(source.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(getTime(source.getEndTime()))")
    Schedule toEntity(ScheduleDTO source);

    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "user.userType", target = "userDTO.userTypeDTO")
    @Mapping(source = "tutor", target = "tutorDTO")
    @Mapping(source = "tutor.user", target = "tutorDTO.userDTO")
    ScheduleDTO toDTO(Schedule target);

    List<ScheduleDTO> getSchedulersDTO(List<Schedule> schedulers);

    default LocalTime getTime(String value) {
        return LocalTime.parse(value);
    }
}
