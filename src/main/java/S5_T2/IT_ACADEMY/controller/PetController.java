package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.entity.Pet;
import S5_T2.IT_ACADEMY.entity.User;
import S5_T2.IT_ACADEMY.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public List<Pet> getAllPets(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName()); // Assuming the user ID is stored as the principal
        return petService.getAllPets(userId);
    }

    @PostMapping("/create")
    public Pet createPet(@RequestBody Pet pet, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName()); // Assuming the user ID is stored as the principal
        pet.setUser(new User(userId)); // Set user to the authenticated user
        return petService.createPet(pet);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        return petService.updatePet(id, pet);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
}