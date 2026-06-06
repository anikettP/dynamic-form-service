package com.crpf.forms.controller;

import com.crpf.forms.dto.EmployeeMasterRequest;
import com.crpf.forms.dto.EmployeeMasterResponse;
import com.crpf.forms.service.EmployeeMasterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeMasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeMasterService employeeMasterService;

    @Test
    @WithMockUser(authorities = "VIEW")
    void testGetAllEmployees() throws Exception {
        EmployeeMasterResponse response = EmployeeMasterResponse.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterService.getAllEmployees()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_id").value("CRPF001"))
                .andExpect(jsonPath("$[0].force_no").value("CRPF001"));
    }

    @Test
    @WithMockUser(authorities = "VIEW")
    void testGetEmployeeByEmployeeId() throws Exception {
        EmployeeMasterResponse response = EmployeeMasterResponse.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterService.getEmployeeByEmployeeId("CRPF001")).thenReturn(response);

        mockMvc.perform(get("/api/employees/employee-id/CRPF001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_id").value("CRPF001"))
                .andExpect(jsonPath("$.force_no").value("CRPF001"));
    }

    @Test
    @WithMockUser(authorities = "VIEW")
    void testGetEmployeeByForceNo() throws Exception {
        EmployeeMasterResponse response = EmployeeMasterResponse.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterService.getEmployeeByForceNo("CRPF001")).thenReturn(response);

        mockMvc.perform(get("/api/employees/force-no/CRPF001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_id").value("CRPF001"))
                .andExpect(jsonPath("$.force_no").value("CRPF001"));
    }

    @Test
    @WithMockUser(authorities = "CREATE")
    void testCreateEmployee() throws Exception {
        EmployeeMasterRequest request = EmployeeMasterRequest.builder()
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .build();

        EmployeeMasterResponse response = EmployeeMasterResponse.builder()
                .id(1L)
                .employeeId("CRPF001")
                .forceNo("CRPF001")
                .employeeName("Demo Employee")
                .active(true)
                .build();

        when(employeeMasterService.createEmployee(any(EmployeeMasterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employee_id").value("CRPF001"))
                .andExpect(jsonPath("$.force_no").value("CRPF001"));
    }
}
