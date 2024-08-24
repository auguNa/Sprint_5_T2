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
        String username = authentication.getName();
        log.info("Fetching pets for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found for username: {}", username);
                    return new RuntimeException("User not found");
                });

        List<VirtualPet> pets = petService.getAllPets(user.getId());
        log.info("Fetched {} pets for user: {}", pets.size(), username);
        return pets;
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<VirtualPet> getAllPetsUser(Authentication authentication) {
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
        log.info("Incoming pet data: name={}, type={}, color={}", pet.getName(), pet.getType(), pet.getColor());

        String username = authentication.getName();
        log.info("Creating pet for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found for username: {}", username);
                    return new RuntimeException("User not found");
                });

        pet.setUser(user);
        VirtualPet createdPet = petService.createPet(pet, user);
        log.info("Pet created with ID: {} for user: {}", createdPet.getId(), username);
        return createdPet;
    }

    @PreAuthorize("hasRole('ROLE_USER') and @securityService.hasAccessToPet(authentication, #id)")
    @GetMapping("/{id}")
    public VirtualPet getPet(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER') and @securityService.hasAccessToPet(authentication, #id)")
    @PutMapping("/{id}")
    public VirtualPet updatePet(@PathVariable Long id, @RequestBody VirtualPet pet) {
        log.info("Updating pet with ID: {}", id);
        VirtualPet updatedPet = petService.updatePet(id, pet);
        log.info("Pet updated with ID: {}", updatedPet.getId());
        return updatedPet;
    }

    @PreAuthorize("hasRole('ROLE_USER') and @securityService.hasAccessToPet(authentication, #id)")
    @PostMapping("/{id}/feed")
    public VirtualPet feedPet(@PathVariable Long id) {
        log.info("Feeding pet with ID: {}", id);
        VirtualPet pet = petService.performAction(id, "feed");
        log.info("Pet fed with ID: {}", id);
        return pet;
    }

    @PreAuthorize("hasRole('ROLE_USER') and @securityService.hasAccessToPet(authentication, #id)")
    @PostMapping("/{id}/play")
    public VirtualPet playWithPet(@PathVariable Long id) {
        log.info("Playing with pet with ID: {}", id);
        VirtualPet pet = petService.performAction(id, "play");
        log.info("Pet played with ID: {}", id);
        return pet;
    }

    @PreAuthorize("hasRole('ROLE_USER') and @securityService.hasAccessToPet(authentication, #id)")
    @PostMapping("/{id}/rest")
    public VirtualPet restPet(@PathVariable Long id) {
        log.info("Resting pet with ID: {}", id);
        VirtualPet pet = petService.performAction(id, "rest");
        log.info("Pet rested with ID: {}", id);
        return pet;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') and @securityService.hasAccessToPet(authentication, #id)")
    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        log.warn("Deleting pet with ID: {}", id);
        petService.deletePet(id);
        log.info("Pet deleted with ID: {}", id);
    }
}
