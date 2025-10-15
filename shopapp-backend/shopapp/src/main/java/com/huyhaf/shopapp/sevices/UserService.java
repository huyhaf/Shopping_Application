package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.components.JwtTokenUtils;
import com.huyhaf.shopapp.dtos.UserDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.exceptions.PermissionDenyException;
import com.huyhaf.shopapp.models.Role;
import com.huyhaf.shopapp.models.User;
import com.huyhaf.shopapp.repositories.RoleRepository;
import com.huyhaf.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You can not register an admin account");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
        newUser.setRole(role);
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception{
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number / password");
        }
//        return optionalUser.get();
        User existingUser = optionalUser.get();
        // check password
        if( existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if (!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(phoneNumber, password, existingUser.getAuthorities());
        // authentication with security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User updateUser(UserDTO userDTO) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("User with phone number "+userDTO.getPhoneNumber()+" is not existed");
        }
        User existingUser = optionalUser.get();
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        return userRepository.save(existingUser);
    }
}
