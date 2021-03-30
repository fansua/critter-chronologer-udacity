package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;


   // @Transactional
    public Pet save(Pet pet, Long ownerId){
        System.out.println("owneID" + ownerId);
        if(ownerId != 0){
            Optional<Customer> optionalCustomer = customerRepository.findById(ownerId);
            if(optionalCustomer.isPresent()){
                System.out.println("do I get here?");
                Customer petOwner = optionalCustomer.get();
                pet.setCustomer(petOwner);
                Pet savedPet = petRepository.save(pet);
                if(petOwner.getPetIds() == null){
                    List<Long> lst = new ArrayList<>();
                    lst.add(savedPet.getId());
                    petOwner.setPetIds(lst);
                }
                else{
                    petOwner.getPetIds().add(savedPet.getId());
                }

                customerRepository.save(petOwner);
                return savedPet;
            }
        }
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets(){
        return StreamSupport.stream(petRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public Optional<Pet> getPetById(long id){
        return petRepository.findById(id);
    }

    public List<Pet> getPetsByCustomerId(Long customerId){
        return petRepository.getAllByCustomerId(customerId);
    }
}
