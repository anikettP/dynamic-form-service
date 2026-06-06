package com.crpf.forms.service;

import com.crpf.forms.dto.AuthRequest;
import com.crpf.forms.dto.AuthResponse;
import com.crpf.forms.entity.EmployeeMasterEntity;
import com.crpf.forms.repository.EmployeeMasterEntityRepository;
import com.crpf.forms.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMasterEntityRepository employeeMasterEntityRepository;

    public EmployeeMasterEntity serachEmployeeMasterEntity(String forceNo) {
        return employeeMasterEntityRepository.findByForceNoAndActiveIsTrue(forceNo);
    }
}
