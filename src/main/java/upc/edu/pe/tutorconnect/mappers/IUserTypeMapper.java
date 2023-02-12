package upc.edu.pe.tutorconnect.mappers;

import org.mapstruct.Mapper;
import upc.edu.pe.tutorconnect.dtos.UserTypeDTO;
import upc.edu.pe.tutorconnect.entities.UserType;

@Mapper(componentModel = "spring")
public interface IUserTypeMapper {

    UserType toEntity(UserTypeDTO source);

    UserTypeDTO toDTO(UserType target);
}
