package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertEntityToScheduleDTO(scheduleService.save(convertScheduleDTOToEntity(scheduleDTO), scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds()));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> temp = scheduleService.getAllSchedules();
        if(!temp.isEmpty()){
            List<ScheduleDTO> lst = temp.stream().map(schedule -> {
                return convertEntityToScheduleDTO(schedule);}).collect(Collectors.toList());
            return lst;
        }
       return null;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getScheduleByPetId(petId).stream().map(schedule -> convertEntityToScheduleDTO(schedule)).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getScheduleByEmployeeId(employeeId).stream().map(schedule -> convertEntityToScheduleDTO(schedule)).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.getScheduleCustomerId(customerId).stream().map(pet -> convertEntityToScheduleDTO(pet)).collect(Collectors.toList());
    }
    private static ScheduleDTO convertEntityToScheduleDTO2(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        return scheduleDTO;
    }

    private static Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
      schedule.setActivities(scheduleDTO.getActivities());
      schedule.setDate(scheduleDTO.getDate());
        return schedule;
    }

    private static ScheduleDTO convertEntityToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setActivities(schedule.getActivities());
        scheduleDTO.setDate(schedule.getDate());
        if(schedule.getEmployees() == null ){
            return scheduleDTO;
        }
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIds);
        if(schedule.getPets() == null){
            return scheduleDTO;

        }
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setPetIds(petIds);

        return scheduleDTO;
    }

}
