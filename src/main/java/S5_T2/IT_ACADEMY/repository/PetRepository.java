package S5_T2.IT_ACADEMY.repository;

import S5_T2.IT_ACADEMY.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUserId(Long userId);
}