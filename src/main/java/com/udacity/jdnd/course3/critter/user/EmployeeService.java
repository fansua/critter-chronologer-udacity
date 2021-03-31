package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployee(long id){
        return employeeRepository.findById(id);
    }

    public Optional<Employee> setEmployeeAvailability(Set<DayOfWeek> availableDays, Long employeeId){
         Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
         if(optionalEmployee.isPresent()){
             Employee e = optionalEmployee.get();
             e.setDaysAvailable(availableDays);
             return Optional.of(employeeRepository.save(e));
         }
         return Optional.empty();
    }

    public List<Employee> getEmployeeAvailability(Set<EmployeeSkill> skills, LocalDate date){
        return employeeRepository.getAllByDaysAvailableContains(date.getDayOfWeek()).stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
}
