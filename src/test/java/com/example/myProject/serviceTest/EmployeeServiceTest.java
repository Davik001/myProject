package com.example.myProject.serviceTest;

import com.example.myProject.dto.alldtos.EmployeeDTO;
import com.example.myProject.dto.common.EmployeeResponseDTO;
import com.example.myProject.dto.create.EmployeeCreateDTO;
import com.example.myProject.dto.update.EmployeeUpdateDTO;
import com.example.myProject.entity.Employee;
import com.example.myProject.map.EmployeeMapper;
import com.example.myProject.repository.EmployeeRepository;
import com.example.myProject.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeMapper employeeMapper;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    Employee employee;
    EmployeeResponseDTO employeeResponseDTO;
    EmployeeCreateDTO employeeCreateDTO;
    EmployeeUpdateDTO employeeUpdateDTO;

    @BeforeEach
    void setUp(){
        employee = new Employee(1L, "Coco", "Jambo", "cocojambo999@mail.ru",
                "qwerty12345", "Admin");

        employeeResponseDTO = new EmployeeResponseDTO(1L, "Coco", "Jambo","cocojambo999@mail.ru", "Admin");
        employeeCreateDTO = new EmployeeCreateDTO("Coco", "Jambo","cocojambo999@mail.ru", "qwerty12345", "Admin");
        employeeUpdateDTO = new EmployeeUpdateDTO(1L, "Coco", "Jambo","cocojambo999@mail.ru", "qwerty12345", "Admin");
    }

    @Test
    void testCreateEmployee(){
        when(employeeMapper.toEntity(any(EmployeeCreateDTO.class))).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDTO(employee)).thenReturn(employeeResponseDTO);

        EmployeeResponseDTO result = employeeService.createEmployee(employeeCreateDTO);

        assertNotNull(result);
        assertEquals("Coco", result.getFirstName());
        assertEquals("Jambo", result.getLastName());

        verify(employeeMapper, times(1)).toEntity(any(EmployeeCreateDTO.class));
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper, times(1)).toResponseDTO(employee);
    }

    @Test
    void testGetEmployee_Success(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponseDTO(employee)).thenReturn(employeeResponseDTO);

        EmployeeResponseDTO result = employeeService.getEmployeeById(1L);
        assertNotNull(result);
        assertEquals("Coco", result.getFirstName());
        assertEquals("Jambo", result.getLastName());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeMapper, times(1)).toResponseDTO(employee);
    }

    @Test
    void testGetEmployee_NotFound(){
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(2L));

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateEmployee_Success(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee updatedEmployee = new Employee(1L, "Rayan", "Gosling", "gosling2025@gmail.com",
                "drive341", "Admin");

        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        when(employeeMapper.toResponseDTO(updatedEmployee)).thenReturn(new EmployeeResponseDTO(
                updatedEmployee.getId(),
                updatedEmployee.getFirstName(),
                updatedEmployee.getLastName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getRole()
        ));

        EmployeeResponseDTO result = employeeService.updateEmployee(1L, employeeUpdateDTO);

        assertEquals("Rayan", result.getFirstName());
        assertEquals("Gosling", result.getLastName());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toResponseDTO(updatedEmployee);
    }

    @Test
    void testUpdateEmployee_NotFound(){
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> employeeService.updateEmployee(2L, employeeUpdateDTO));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteEmployee_Success(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound(){
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(2L));
        assertEquals("Employee not found", exception.getMessage());

        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeRepository, never()).deleteById(2L);
    }
}
