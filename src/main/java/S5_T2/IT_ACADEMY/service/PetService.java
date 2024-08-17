package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.Pet;
import S5_T2.IT_ACADEMY.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class PetService {
    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    private PetRepository petRepository;

    @Cacheable(value = "pets", key = "#userId")
    public List<Pet> getAllPets(Long userId) {
        logger.info("Fetching all pets for user ID: {}", userId);
        return petRepository.findByUserId(userId);
    }

    @CacheEvict(value = "pets", key = "#pet.user.id", allEntries = true)
    public Pet createPet(Pet pet) {
        logger.info("Creating new pet: {}", pet.getName());
        Pet createdPet = petRepository.save(pet);
        logger.debug("Pet created with ID: {}", createdPet.getId());
        return createdPet;
    }

    @CacheEvict(value = "pets", key = "#petDetails.user.id")
    public Pet updatePet(Long petId, Pet petDetails) {
        logger.info("Updating pet with ID: {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(petDetails.getName());
        pet.setType(petDetails.getType());
        pet.setColor(petDetails.getColor());

        Pet updatedPet = petRepository.save(pet);
        logger.debug("Pet updated: {}", updatedPet.getName());
        return updatedPet;
    }
    // Cache the pet's state and evict it when an action is performed
    @Cacheable(value = "pet", key = "#petId")
    public Pet performAction(Long petId, String action) {
        logger.info("Performing action '{}' on pet with ID: {}", action, petId);
        Pet pet = petRepository.findById(petId)
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
        Pet updatedPet = petRepository.save(pet);
        logger.debug("Action '{}' performed on pet: {}", action, updatedPet.getName());

        evictPetCache(petId);

        return updatedPet;
    }

    @CacheEvict(value = "pets", allEntries = true)
    public void deletePet(Long petId) {
        logger.info("Deleting pet with ID: {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        petRepository.delete(pet);
        logger.debug("Pet with ID: {} has been deleted", petId);
        evictPetCache(petId);
    }

    // Method to manually evict the cache for a specific pet
    @CacheEvict(value = "pet", key = "#petId")
    public void evictPetCache(Long petId) {
        logger.debug("Evicting cache for pet with ID: {}", petId);
    }
}