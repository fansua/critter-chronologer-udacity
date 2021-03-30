package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Schedule save(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return StreamSupport.stream(scheduleRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public List<Schedule> getScheduleByPetId(Long petId){
        System.out.println("ped ID" + petId);
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if(optionalPet.isPresent()){
            System.out.println("Did I get inside the optional");
            Pet pet = optionalPet.get();
            List<Schedule> sc= scheduleRepository.findAll().stream().filter( schedule -> schedule.getPets().contains(pet)).collect(Collectors.toList());
            System.out.println("LST SCh " +sc);
            return sc;
        }
        return new ArrayList<Schedule>();
    }

    @Transactional
    public List<Schedule> getScheduleByEmployeeId(Long employeeId){
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            return scheduleRepository.findAll().stream().filter( schedule -> schedule.getEmployees().contains(employee)).collect(Collectors.toList());
        }
        return new ArrayList<Schedule>();
    }

    public List<Schedule> getScheduleCustomerId(Long customerId){
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            return scheduleRepository.findAll().stream().filter( schedule -> schedule.getPets().containsAll(customer.getPets())).collect(Collectors.toList());
        }
        return new ArrayList<Schedule>();
    }
}
