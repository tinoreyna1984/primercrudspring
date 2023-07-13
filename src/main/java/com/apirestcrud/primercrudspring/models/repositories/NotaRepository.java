package com.apirestcrud.primercrudspring.models.repositories;

import com.apirestcrud.primercrudspring.models.entities.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {
}
