package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private EmployeeService employeeService;
    private CustomerService customerService;

    public UserController(EmployeeService employeeService, CustomerService customerService) {
        this.employeeService = employeeService;
        this.customerService = customerService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertEntityToCustomerDTO(customerService.save(convertCustomerDTOToEntity(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.findCustomers().stream().map( customer -> convertEntityToCustomerDTO(customer)).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long d){
        return convertEntityToCustomerDTO(customerService.getCustomerByPetId(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return convertEntityToEmployeeDTO(employeeService.save(convertEmployeeDTOToEntity(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Optional<Employee> optionalEmployee = employeeService.getEmployee(employeeId);
        if(optionalEmployee.isPresent()){
            return convertEntityToEmployeeDTO(optionalEmployee.get());
        }
        return new EmployeeDTO();
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
         Optional<Employee> optionalEmployee = employeeService.setEmployeeAvailability(daysAvailable,employeeId);
         if(!optionalEmployee.isPresent()){
             System.out.println("SUCCESS");
         }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        return  employeeService.getEmployeeAvailability(employeeDTO.getSkills(),employeeDTO.getDate())
                .stream().map(employee -> convertEntityToEmployeeDTO(employee))
                .collect(Collectors.toList());
    }

    private static EmployeeDTO convertEntityToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private static Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private static CustomerDTO convertEntityToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    private static Customer convertCustomerDTOToEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

}
