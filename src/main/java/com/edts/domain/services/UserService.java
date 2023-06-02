package com.edts.domain.services;

import com.edts.domain.helper.IDGenerator;
import com.edts.domain.model.dto.UserRequestDTO;
import com.edts.domain.model.entities.User;
import com.edts.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setId(IDGenerator.generateID());
        user.setName(userRequestDTO.getName());
        user.setNik(userRequestDTO.getNik());
        user.setAddress(userRequestDTO.getAddress());
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(Timestamp.valueOf(now));
        user.setUpdatedAt(Timestamp.valueOf(now));

        userRepository.save(user);

        return user;
    }
}
