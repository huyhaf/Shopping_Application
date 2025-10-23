package com.huyhaf.shopapp.controllers;

import com.huyhaf.shopapp.components.LocalizationUtils;
import com.huyhaf.shopapp.dtos.UserDTO;
import com.huyhaf.shopapp.dtos.UserLoginDTO;
import com.huyhaf.shopapp.models.User;
import com.huyhaf.shopapp.responses.LoginResponse;
import com.huyhaf.shopapp.responses.RegisterResponse;
import com.huyhaf.shopapp.sevices.IUserService;
import com.huyhaf.shopapp.utils.MessageKeys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    // can we register an "admin" user ?
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()){
                List<String> errorMessages= result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(RegisterResponse.builder().message(
                        localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAIL, String.join(", ", errorMessages))
                ).build());
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body(RegisterResponse.builder().message(null).build());
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(RegisterResponse.builder()
                .user(user)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESS))
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAIL,e.getMessage()))
                .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        // kiem tra thong tin dang nhap va sinh token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword(),userLoginDTO.getRoleId());
            // tra ve token trong response
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESS))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAIL,e.getMessage()))
                .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @Valid @RequestBody UserDTO userDTO) {
        try {
            User updateUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(updateUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
