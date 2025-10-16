package com.huyhaf.shopapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huyhaf.shopapp.sevices.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping("")
    public ResponseEntity<?> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
