package S5_T2.IT_ACADEMY;

import S5_T2.IT_ACADEMY.entity.VirtualPet;
import S5_T2.IT_ACADEMY.repository.VirtualPetRepository;
import S5_T2.IT_ACADEMY.service.implementation.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ExtendWith(SpringExtension.class)

public class PetServiceImplTest {

    @MockBean
    private VirtualPetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    private VirtualPet pet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pet = new VirtualPet();
        pet.setId(1L);
        pet.setName("Fluffy");
    }

    @Test
    public void testCreatePet_CachePut() {
        // Arrange: Mock the repository to save a pet
        when(petRepository.save(pet)).thenReturn(pet);
        // Act: Create a new pet
        VirtualPet createdPet = petService.createPet(pet, null);
        assertEquals(pet.getName(), createdPet.getName());
        // Check that the repository save method was called
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    public void testDeletePet_CacheEvict() {
        // Arrange: Mock the repository to find a pet
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        // Act: Delete the pet
        petService.deletePet(1L);
        // Verify that the repository delete method was called
        verify(petRepository, times(1)).delete(pet);
    }
}