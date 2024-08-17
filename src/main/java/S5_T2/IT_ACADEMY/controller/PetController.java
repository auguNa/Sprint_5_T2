package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.entity.Pet;
import S5_T2.IT_ACADEMY.entity.User;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")
public class PetController {
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private PetService petService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Pet> getAllPets(Authentication authentication) {
        // Get the username from the authentication object
        String username = authentication.getName();
        logger.info("Fetching pets for user: {}", username);

        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found for username: {}", username);
                    return new RuntimeException("User not found");
                });
        logger.debug("User found: {}", user);
        // Fetch and return all pets for the user
        List<Pet> pets = petService.getAllPets(user.getId());
        logger.info("Fetched {} pets for user: {}", pets.size(), username);
        return pets;
    }

    @PostMapping("/create")
    public Pet createPet(@RequestBody Pet pet, Authentication authentication) {
        // Get the username from the authentication object
        String username = authentication.getName();
        logger.info("Creating pet for user: {}", username);

        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found for username: {}", username);
                    return new RuntimeException("User not found");
                });
        // Set the fetched user to the pet
        pet.setUser(user);
        Pet createdPet = petService.createPet(pet);
        logger.info("Pet created with ID: {} for user: {}", createdPet.getId(), username);
        return createdPet;
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        logger.info("Updating pet with ID: {}", id);
        Pet updatedPet = petService.updatePet(id, pet);
        logger.info("Pet updated with ID: {}", updatedPet.getId());
        return updatedPet;
    }

    // New endpoints for actions
    @PostMapping("/{id}/feed")
    public Pet feedPet(@PathVariable Long id) {
        logger.info("Feeding pet with ID: {}", id);
        Pet pet = petService.performAction(id, "feed");
        logger.info("Pet fed with ID: {}", id);
        return pet;
    }

    @PostMapping("/{id}/play")
    public Pet playWithPet(@PathVariable Long id) {
        logger.info("Playing with pet with ID: {}", id);
        Pet pet = petService.performAction(id, "play");
        logger.info("Pet played with ID: {}", id);
        return pet;
    }

    @PostMapping("/{id}/rest")
    public Pet restPet(@PathVariable Long id) {
        logger.info("Resting pet with ID: {}", id);
        Pet pet = petService.performAction(id, "rest");
        logger.info("Pet rested with ID: {}", id);
        return pet;
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        logger.warn("Deleting pet with ID: {}", id);
        petService.deletePet(id);
        logger.info("Pet deleted with ID: {}", id);
    }
}