package S5_T2.IT_ACADEMY.service.implementation;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.entity.VirtualPet;
import S5_T2.IT_ACADEMY.repository.VirtualPetRepository;
import S5_T2.IT_ACADEMY.service.PetService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PetServiceImpl implements PetService {
    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private VirtualPetRepository petRepository;

    @Override
    public VirtualPet getPetById(Long id) {
        logger.info("Fetching pet with ID: {} from database", id);
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    @Override
    @Cacheable(value = "pets", key = "#userEntity.id", unless = "#result == null")
    public List<VirtualPet> getAllPets(UserEntity userEntity) {
        if (userEntity == null || userEntity.getId() == null) {
            throw new IllegalArgumentException("UserEntity or its ID cannot be null");
        }

        if (!isAdmin(userEntity)) {
            Cache cache = cacheManager.getCache("pets");
            if (cache != null ) {
                logger.info("Pets for user {} retrieved from cache", userEntity.getUsername());
            } else {
                logger.info("Pets for user {} not in cache, fetching from database", userEntity.getUsername());
            }
        }

        if (isAdmin(userEntity)) {
            logger.info("Admin user {} fetching all pets", userEntity.getUsername());
            return petRepository.findAll().stream().distinct().collect(Collectors.toList());
        } else {
            logger.info("User {} fetching their pets", userEntity.getUsername());
            return petRepository.findByUserEntity(userEntity).stream().distinct().collect(Collectors.toList());
        }
    }

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Cacheable(value = "userPets", key = "#userEntity.id", unless = "#result == null")
    public List<VirtualPet> getAllPetsForUser(UserEntity userEntity) {
        logger.info("User {} fetching their pets", userEntity.getUsername());
        return petRepository.findByUserEntity(userEntity).stream().distinct().collect(Collectors.toList());
    }


    @Override
    @CacheEvict(value = "pets", key = "#userEntity.id")
    public VirtualPet createPet(VirtualPet pet, UserEntity userEntity) {
        logger.info("Creating pet: {}", pet.getName());
        pet.setUserEntity(userEntity);
        return petRepository.save(pet);
    }

    @Override
    @CacheEvict(value = "pets", allEntries = true)
    public VirtualPet updatePet(Long petId, VirtualPet petDetails) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        logger.info("Updating pet ID: {} with new details: {}", petId, petDetails);
        pet.setName(petDetails.getName());
        pet.setType(petDetails.getType());
        pet.setColor(petDetails.getColor());
        pet.setMood(petDetails.getMood());
        pet.setEnergy(petDetails.getEnergy());
        return petRepository.save(pet);
    }

    @Override
    @CacheEvict(value = "pets", key = "#userEntity.id", allEntries = true)
    public VirtualPet performAction(Long petId, String action) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        logger.info("Performing action '{}' on pet ID: {}", action, petId);
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
    @CacheEvict(value = "pets", key = "#userEntity.id", allEntries = true)
    public void deletePet(Long petId) {
        VirtualPet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        logger.info("Deleting pet ID: {}", petId);
        petRepository.delete(pet);
    }
}
