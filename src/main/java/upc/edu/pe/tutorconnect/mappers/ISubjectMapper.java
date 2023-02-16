package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.edu.pe.tutorconnect.dtos.SubjectDTO;
import upc.edu.pe.tutorconnect.entities.Subject;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ISubjectMapper extends ITutorSubjectMapper {

    Subject toEntity(SubjectDTO source);

    @Mapping(target = "tutorsDTO", expression = "java(getTutorsDTO(target.getTutors()))")
    SubjectDTO toDTO(Subject target);


}
