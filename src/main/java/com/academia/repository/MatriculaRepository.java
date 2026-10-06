package com.academia.repository;

import com.academia.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    Optional<Matricula> findFirstByAlunoId(Integer alunoId);
}