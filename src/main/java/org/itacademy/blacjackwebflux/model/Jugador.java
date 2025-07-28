package org.itacademy.blacjackwebflux.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("jugadores")
public class Jugador {
    @Id
    private Long id;
    private String nombre;
    private int puntuacion;
}
