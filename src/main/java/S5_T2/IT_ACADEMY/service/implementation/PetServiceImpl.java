package S5_T2.IT_ACADEMY.service.implementation;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.entity.VirtualPet;
import S5_T2.IT_ACADEMY.repository.VirtualPetRepository;
import S5_T2.IT_ACADEMY.service.PetService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class PetServiceImpl implements PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    private VirtualPetRepository petRepository;

    @Override
    public VirtualPet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    @Override
    public List<VirtualPet> getAllPets(UserEntity userEntity) {
        // Check if userEntity or its id is null
        if (userEntity == null || userEntity.getId() == null) {
            throw new IllegalArgumentException("UserEntity or its ID cannot be null");
        }

        if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            // Fetch distinct pets if the user is an admin to avoid duplicates
            return petRepository.findAll().stream().distinct().collect(Collectors.toList());
        } else {
            // Fetch distinct pets for the specific user to avoid duplicates
            return petRepository.findByUserEntity(userEntity).stream().distinct().collect(Collectors.toList());
        }
    }

    @Override
    public VirtualPet createPet(VirtualPet pet, UserEntity userEntity) {
        pet.setUserEntity(userEntity);
        return petRepository.save(pet);
    }

    @Override
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

    @Override
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

    @Override
    public void deletePet(Long petId) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        petRepository.delete(pet);
    }
}
