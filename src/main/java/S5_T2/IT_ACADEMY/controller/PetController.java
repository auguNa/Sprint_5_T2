package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.entity.Pet;
import S5_T2.IT_ACADEMY.entity.User;
import S5_T2.IT_ACADEMY.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Pet> getAllPets(Authentication authentication) {
        // Get the username from the authentication object
        String username = authentication.getName();

        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch and return all pets for the user
        return petService.getAllPets(user.getId());
    }

    @PostMapping("/create")
    public Pet createPet(@RequestBody Pet pet, Authentication authentication) {
        // Get the username from the authentication object
        String username = authentication.getName();
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Set the fetched user to the pet
        pet.setUser(user);
        // Save and return the created pet
        return petService.createPet(pet);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        return petService.updatePet(id, pet);
    }

    // New endpoints for actions
    @PostMapping("/{id}/feed")
    public Pet feedPet(@PathVariable Long id) {
        return petService.performAction(id, "feed");
    }

    @PostMapping("/{id}/play")
    public Pet playWithPet(@PathVariable Long id) {
        return petService.performAction(id, "play");
    }

    @PostMapping("/{id}/rest")
    public Pet restPet(@PathVariable Long id) {
        return petService.performAction(id, "rest");
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
}