package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table (name = "UNIT_OF_MEASURE")
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1000642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNIT_OF_MEASURE_ID")
    private Long id;

    @Column(name = "UNIT_OF_MEASURE_NAME")
    private String name;

    public UnitOfMeasure(String yourOwnUnitOfMeasure) {
        this.name = yourOwnUnitOfMeasure;
    }
}