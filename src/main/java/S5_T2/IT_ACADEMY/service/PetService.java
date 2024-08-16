package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.Pet;
import S5_T2.IT_ACADEMY.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<Pet> getAllPets(Long userId) {
        return petRepository.findByUserId(userId);
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Long petId, Pet petDetails) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(petDetails.getName());
        pet.setType(petDetails.getType());
        pet.setColor(petDetails.getColor());

        return petRepository.save(pet);
    }

    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        petRepository.delete(pet);
    }
}