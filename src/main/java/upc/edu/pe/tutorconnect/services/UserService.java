package upc.edu.pe.tutorconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.tutorconnect.dtos.UserDTO;
import upc.edu.pe.tutorconnect.mappers.IUserMapper;
import upc.edu.pe.tutorconnect.repositories.IUserRepository;

import java.util.List;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public List<UserDTO> findAllUser() {
        return this.userMapper.getUsersDTO(this.userRepository.findAll());
    }
}
