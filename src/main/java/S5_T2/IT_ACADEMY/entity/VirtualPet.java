package S5_T2.IT_ACADEMY.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class VirtualPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name cannot exceed 50 characters")

    private String name;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Type cannot exceed 30 characters")
    private String type;

    @NotBlank(message = "Color is mandatory")
    @Size(max = 20, message = "Color cannot exceed 20 characters")
    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Min(value = 0, message = "Mood cannot be less than 0")
    @Max(value = 100, message = "Mood cannot exceed 100")
    private int mood = 100;

    @Min(value = 0, message = "Energy cannot be less than 0")
    @Max(value = 100, message = "Energy cannot exceed 100")
    private int energy = 100;

    public VirtualPet() {

    }

    // Methods to perform actions
    public void feed() {
        this.energy = Math.min(100, this.energy + 10);
    }

    public void play() {
        this.mood = Math.min(100, this.mood + 10);
        this.energy = Math.max(0, this.energy - 10); // Playing reduces energy
    }

    public void rest() {
        this.energy = Math.min(100, this.energy + 20);
        this.mood = Math.max(0, this.mood - 5); // Resting may slightly reduce mood
    }
}
