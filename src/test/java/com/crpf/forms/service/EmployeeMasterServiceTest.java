package com.crpf.forms.service;

import com.crpf.forms.dto.EmployeeMasterRequest;
import com.crpf.forms.dto.EmployeeMasterResponse;
import com.crpf.forms.entity.EmployeeMaster;
import com.crpf.forms.exception.DuplicateRecordException;
import com.crpf.forms.exception.ResourceNotFoundException;
import com.crpf.forms.repository.EmployeeMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeMasterServiceTest {

    @Mock
    private EmployeeMasterRepository employeeMasterRepository;

    private EmployeeMasterService employeeMasterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeMasterService = new EmployeeMasterService(employeeMasterRepository);
    }

    @Test
    void testGetAllEmployees() {
        EmployeeMaster employee = EmployeeMaster.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterRepository.findAllByActiveTrue()).thenReturn(Collections.singletonList(employee));

        List<EmployeeMasterResponse> result = employeeMasterService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CRPF001", result.get(0).getEmployeeId());
        assertEquals("CRPF001", result.get(0).getForceNo());
    }

    @Test
    void testGetEmployeeByIdSuccess() {
        EmployeeMaster employee = EmployeeMaster.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));

        EmployeeMasterResponse result = employeeMasterService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("CRPF001", result.getEmployeeId());
        assertEquals("CRPF001", result.getForceNo());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeMasterRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeMasterService.getEmployeeById(1L));
    }

    @Test
    void testCreateEmployeeSuccess() {
        EmployeeMasterRequest request = EmployeeMasterRequest.builder()
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .rank("SI")
                .unit("Delhi")
                .build();

        when(employeeMasterRepository.existsByEmployeeIdAndActiveTrue("CRPF001")).thenReturn(false);
        when(employeeMasterRepository.existsByForceNoAndActiveTrue("CRPF001")).thenReturn(false);
        when(employeeMasterRepository.save(any(EmployeeMaster.class))).thenAnswer(invocation -> {
            EmployeeMaster employee = invocation.getArgument(0);
            employee.setId(1L);
            return employee;
        });

        EmployeeMasterResponse result = employeeMasterService.createEmployee(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CRPF001", result.getEmployeeId());
        assertEquals("CRPF001", result.getForceNo());
    }

    @Test
    void testCreateEmployeeDuplicateThrowsException() {
        EmployeeMasterRequest request = EmployeeMasterRequest.builder()
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .build();

        when(employeeMasterRepository.existsByEmployeeIdAndActiveTrue("CRPF001")).thenReturn(true);

        assertThrows(DuplicateRecordException.class, () -> employeeMasterService.createEmployee(request));
    }

    @Test
    void testDeleteEmployee() {
        EmployeeMaster employee = EmployeeMaster.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        employeeMasterService.deleteEmployee(1L);

        assertFalse(employee.isActive());
        verify(employeeMasterRepository, times(1)).save(employee);
    }
}
