package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.UserDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
    User updateUser(UserDTO userDTO) throws DataNotFoundException;
}
