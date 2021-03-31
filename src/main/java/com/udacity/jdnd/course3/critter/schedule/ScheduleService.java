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

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Schedule save(Schedule schedule, List<Long> employeesIds, List<Long> petIds){
        List<Pet> pets = petRepository.findAllById(petIds);
        List<Employee> employees = employeeRepository.findAllById(employeesIds);
        schedule.setPets(pets);
        schedule.setEmployees(employees);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        List<Schedule> lst = scheduleRepository.findAll();
        return lst;
    }

    public List<Schedule> getScheduleByPetId(Long petId){
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if(optionalPet.isPresent()){
            Pet pet = optionalPet.get();
            List<Schedule> sc= scheduleRepository.findAll().stream().filter( schedule -> schedule.getPets().contains(pet)).collect(Collectors.toList());
            return sc;
        }
        return new ArrayList<Schedule>();
    }

    @Transactional
    public List<Schedule> getScheduleByEmployeeId(Long employeeId){

        Employee e = employeeRepository.getOne(employeeId);
        List<Schedule> sch=  scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getEmployees().contains(e))
                .collect(Collectors.toList());
        return sch ;
    }

    public List<Schedule> getScheduleCustomerId(Long customerId){

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            return scheduleRepository.findAll()
                    .stream()
                    .filter(schedule -> schedule.getPets().size()  == customer.getPetIds().size())
                    .collect(Collectors.toList());
        }
        return new ArrayList<Schedule>();

    }
}
