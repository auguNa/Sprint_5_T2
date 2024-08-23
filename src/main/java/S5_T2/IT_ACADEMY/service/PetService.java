package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.Pet;
import S5_T2.IT_ACADEMY.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    // Cache the result of fetching all pets for a user
    @Cacheable(value = "pets", key = "#userId")
    public List<Pet> getAllPets(Long userId) {
        return petRepository.findByUserId(userId);
    }

    // Evict cache when a new pet is created
    @CacheEvict(value = "pets", key = "#pet.user.id", allEntries = true)
    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    // Evict cache when a pet is updated
    @CacheEvict(value = "pets", key = "#petDetails.user.id")
    public Pet updatePet(Long petId, Pet petDetails) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(petDetails.getName());
        pet.setType(petDetails.getType());
        pet.setColor(petDetails.getColor());

        return petRepository.save(pet);
    }

    // Cache the pet's state and evict it when an action is performed
    @Cacheable(value = "pet", key = "#petId")
    public Pet performAction(Long petId, String action) {
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

        // Evict the cache after updating the pet
        evictPetCache(petId);

        return petRepository.save(pet);
    }

    // Evict cache when a pet is deleted
    @CacheEvict(value = "pets", allEntries = true)
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
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
