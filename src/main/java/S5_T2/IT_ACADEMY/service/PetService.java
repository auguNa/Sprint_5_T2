package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.entity.VirtualPet;

import java.util.List;

public interface PetService {

    VirtualPet getPetById(Long id);

    List<VirtualPet> getAllPets(UserEntity userEntity);

    VirtualPet createPet(VirtualPet pet, UserEntity userEntity);

    VirtualPet updatePet(Long petId, VirtualPet petDetails);

    VirtualPet performAction(Long petId, String action);

    void deletePet(Long petId);
}
