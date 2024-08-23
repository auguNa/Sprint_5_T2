package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.entity.VirtualPet;
import S5_T2.IT_ACADEMY.entity.User;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.VirtualPetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")

public class PetController {
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private VirtualPetService petService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/pets")
    public List<VirtualPet> getAllPets(Authentication authentication) {
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
        List<VirtualPet> pets = petService.getAllPets(user.getId());
        logger.info("Fetched {} pets for user: {}", pets.size(), username);
        return pets;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public VirtualPet createPet(@Valid @RequestBody VirtualPet pet, Authentication authentication) {
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
        VirtualPet createdPet = petService.createPet(pet, user);
        logger.info("Pet created with ID: {} for user: {}", createdPet.getId(), username);
        return createdPet;
    }

    @Autowired
    private VirtualPetService virtualPetService;

    @PreAuthorize("hasRole('ROLE_USER') and @securityService.hasAccessToPet(authentication, #id)")
    @GetMapping("/pets/{id}")
    public VirtualPet getPet(@PathVariable Long id) {
        return virtualPetService.getPetById(id);
    }

    @PutMapping("/{id}")
    public VirtualPet updatePet(@PathVariable Long id, @RequestBody VirtualPet pet) {
        logger.info("Updating pet with ID: {}", id);
        VirtualPet updatedPet = petService.updatePet(id, pet);
        logger.info("Pet updated with ID: {}", updatedPet.getId());
        return updatedPet;
    }

    // New endpoints for actions
    @PostMapping("/{id}/feed")
    public VirtualPet feedPet(@PathVariable Long id) {
        logger.info("Feeding pet with ID: {}", id);
        VirtualPet pet = petService.performAction(id, "feed");
        logger.info("Pet fed with ID: {}", id);
        return pet;
    }

    @PostMapping("/{id}/play")
    public VirtualPet playWithPet(@PathVariable Long id) {
        logger.info("Playing with pet with ID: {}", id);
        VirtualPet pet = petService.performAction(id, "play");
        logger.info("Pet played with ID: {}", id);
        return pet;
    }

    @PostMapping("/{id}/rest")
    public VirtualPet restPet(@PathVariable Long id) {
        logger.info("Resting pet with ID: {}", id);
        VirtualPet pet = petService.performAction(id, "rest");
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