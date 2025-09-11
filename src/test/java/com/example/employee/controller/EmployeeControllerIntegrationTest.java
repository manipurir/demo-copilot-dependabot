package com.example.employee.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.employee.dto.CreateEmployeeRequest;
import com.example.employee.dto.UpdateEmployeeRequest;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class EmployeeControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        employeeRepository.deleteAll();
        
        testEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .position("Software Engineer")
                .build();
        
        testEmployee = employeeRepository.save(testEmployee);
    }

    @Test
    void createEmployee_ShouldReturnCreated_WhenValidRequest() throws Exception {
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .department("Marketing")
                .position("Marketing Manager")
                .build();

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$.department").value("Marketing"))
                .andExpect(jsonPath("$.position").value("Marketing Manager"));
    }

    @Test
    void createEmployee_ShouldReturnConflict_WhenEmailAlreadyExists() throws Exception {
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("john.doe@example.com") // Same email as existing employee
                .department("Marketing")
                .position("Marketing Manager")
                .build();

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Employee with email john.doe@example.com already exists"));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{id}", testEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getEmployeeById_ShouldReturnNotFound_WhenEmployeeNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with ID: 999"));
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee_WhenValidRequest() throws Exception {
        UpdateEmployeeRequest request = UpdateEmployeeRequest.builder()
                .firstName("Johnny")
                .lastName("Doe")
                .email("johnny.doe@example.com")
                .department("Engineering")
                .position("Senior Software Engineer")
                .build();

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.email").value("johnny.doe@example.com"))
                .andExpect(jsonPath("$.position").value("Senior Software Engineer"));
    }

    @Test
    void deleteEmployee_ShouldReturnNoContent_WhenEmployeeExists() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployee_ShouldReturnNotFound_WhenEmployeeNotExists() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with ID: 999"));
    }
}
