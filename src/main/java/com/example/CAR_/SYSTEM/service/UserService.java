package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.UserDTO;
import com.example.CAR_.SYSTEM.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(Long id);
    User create(UserDTO dto);
    User update(Long id, UserDTO dto);
    void delete(Long id);
}

