package S5_T2.IT_ACADEMY.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Pet {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String type;

    @Getter @Setter
    private String color;

    // New attributes
    @Getter @Setter
    private int mood;

    @Getter @Setter
    private int energy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    // You can add logic to initialize mood and energy, e.g., setting default values
    public Pet() {
        this.mood = 100; // Max mood
        this.energy = 100; // Max energy
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