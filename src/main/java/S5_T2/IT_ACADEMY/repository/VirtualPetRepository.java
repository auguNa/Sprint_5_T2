package S5_T2.IT_ACADEMY.repository;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.entity.VirtualPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VirtualPetRepository extends JpaRepository<VirtualPet, Long> {
    List<VirtualPet> findByUserEntity(UserEntity userEntity);
}