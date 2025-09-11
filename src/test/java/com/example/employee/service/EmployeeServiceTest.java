package com.example.employee.service;

import com.example.employee.dto.CreateEmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.dto.UpdateEmployeeRequest;
import com.example.employee.exception.EmployeeAlreadyExistsException;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;
    private CreateEmployeeRequest createRequest;
    private UpdateEmployeeRequest updateRequest;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .position("Software Engineer")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        createRequest = CreateEmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .position("Software Engineer")
                .build();

        updateRequest = UpdateEmployeeRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .department("Marketing")
                .position("Marketing Manager")
                .build();
    }

    @Test
    void createEmployee_ShouldReturnEmployeeResponse_WhenValidRequest() {
        // Given
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // When
        EmployeeResponse response = employeeService.createEmployee(createRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository).existsByEmail("john.doe@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_ShouldThrowException_WhenEmailAlreadyExists() {
        // Given
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> employeeService.createEmployee(createRequest))
                .isInstanceOf(EmployeeAlreadyExistsException.class)
                .hasMessageContaining("already exists");
        
        verify(employeeRepository).existsByEmail("john.doe@example.com");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        // When
        EmployeeResponse response = employeeService.getEmployeeById(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeById_ShouldThrowException_WhenEmployeeNotExists() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("not found");
        
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        // Given
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<EmployeeResponse> responses = employeeService.getAllEmployees();

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getFirstName()).isEqualTo("John");
        verify(employeeRepository).findAll();
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee_WhenValidRequest() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // When
        EmployeeResponse response = employeeService.updateEmployee(1L, updateRequest);

        // Then
        assertThat(response).isNotNull();
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee_WhenEmployeeExists() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // When
        employeeService.deleteEmployee(1L);

        // Then
        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_ShouldThrowException_WhenEmployeeNotExists() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("not found");
        
        verify(employeeRepository).existsById(1L);
        verify(employeeRepository, never()).deleteById(1L);
    }
}
