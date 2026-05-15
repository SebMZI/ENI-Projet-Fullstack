package fr.eni.backend.bo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder

@Entity
@Table(name = "ADMINISTRATOR")
//@DiscriminatorValue(value = "A")
public class Administrateur extends Utilisateur {

    @Column(name = "ADMINISTRATOR_SERVICE", length = 100)
    private String service;
}
