package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.User;
import S5_T2.IT_ACADEMY.entity.VirtualPet;
import S5_T2.IT_ACADEMY.repository.VirtualPetRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VirtualPetService {
    private static final Logger logger = LoggerFactory.getLogger(VirtualPetService.class);

    @Autowired
    private VirtualPetRepository petRepository;


    public VirtualPet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    public List<VirtualPet> getAllPets(Long userId) {
        return petRepository.findByUserId(userId);
    }


    public VirtualPet createPet(VirtualPet pet, User user) {
        pet.setUser(user);
        return petRepository.save(pet);
    }

    public VirtualPet updatePet(Long petId, VirtualPet petDetails) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(petDetails.getName());
        pet.setType(petDetails.getType());
        pet.setColor(petDetails.getColor());
        pet.setMood(petDetails.getMood());
        pet.setEnergy(petDetails.getEnergy());
        return petRepository.save(pet);
    }


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
        return petRepository.save(pet);
    }


    public void deletePet(Long petId) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        petRepository.delete(pet);
    }
}
