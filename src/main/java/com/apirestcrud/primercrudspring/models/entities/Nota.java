package com.apirestcrud.primercrudspring.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

// Se puede usar @Getter y @Setter de forma general para todos los atributos desde aquí
// O para los atributos que lo requieran
@Entity
@Table(name = "notas")
@Getter
@Setter
@NoArgsConstructor
public class Nota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El título no puede quedar en blanco")
    private String titulo;

    @Column(nullable = false)
    @NotBlank(message = "El contenido no puede quedar en blanco")
    private String contenido;

    @Column(insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="America/Guayaquil")
    private Date fecha;

    @PrePersist
    public void prePersist(){
        fecha = new Date();
    }

}
