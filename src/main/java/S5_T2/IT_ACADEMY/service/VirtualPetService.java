package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.controller.PetController;
import S5_T2.IT_ACADEMY.entity.User;
import S5_T2.IT_ACADEMY.entity.VirtualPet;
import S5_T2.IT_ACADEMY.repository.VirtualPetRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
public class VirtualPetService {
    private static final Logger logger = LoggerFactory.getLogger(VirtualPetService.class);
    @Autowired
    private VirtualPetRepository petRepository;

    @Cacheable("pets")
    public VirtualPet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }
    // Cache the result of fetching all pets for a user
    @Cacheable(value = "pets", key = "#userId")
    public List<VirtualPet> getAllPets(Long userId) {
        return petRepository.findByUserId(userId);
    }

    // Evict cache when a new pet is created
    @CacheEvict(value = "pets", key = "#pet.user.id", allEntries = true)
    public VirtualPet createPet(VirtualPet pet, User owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        if (pet == null) {
            throw new IllegalArgumentException("Pet cannot be null");
        }
        pet.setUser(owner);

        try {
            VirtualPet savedPet = petRepository.save(pet);
            logger.info("Pet created successfully: {}", savedPet);
            return savedPet;
        } catch (Exception e) {
            logger.error("Error creating pet: ", e);
            throw new RuntimeException("Failed to create pet", e);
        }
    }

    // Evict cache when a pet is updated
    @CacheEvict(value = "pets", key = "#petDetails.user.id")
    public VirtualPet updatePet(Long petId, VirtualPet petDetails) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(petDetails.getName());
        pet.setType(petDetails.getType());
        pet.setColor(petDetails.getColor());

        return petRepository.save(pet);
    }

    // Cache the pet's state and evict it when an action is performed
    @Cacheable(value = "pet", key = "#petId")
    public VirtualPet performAction(Long petId, String action) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        switch (action.toLowerCase()) {
            case "feed":
                pet.feed();
                break;
            case "play":
                pet.play();
                break;
            case "rest":
                pet.rest();
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }

        // Evict the cache after updating the pet
        evictPetCache(petId);

        return petRepository.save(pet);
    }

    // Evict cache when a pet is deleted
    @CacheEvict(value = "pets", allEntries = true)
    public void deletePet(Long petId) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        petRepository.delete(pet);

        // Evict the cache after deleting the pet
        evictPetCache(petId);
    }

    // Method to manually evict the cache for a specific pet
    @CacheEvict(value = "pet", key = "#petId")
    public void evictPetCache(Long petId) {
        // Cache eviction logic
    }
}
