package com.academia.service;

import com.academia.model.Matricula;
import com.academia.model.Plano;
import com.academia.repository.MatriculaRepository;
import com.academia.repository.PlanoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final PlanoRepository planoRepository;
    private final EntityManager entityManager;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            PlanoRepository planoRepository,
            EntityManager entityManager) {

        this.matriculaRepository = matriculaRepository;
        this.planoRepository = planoRepository;
        this.entityManager = entityManager;
    }

    public List<Matricula> listarTodos() {
        return matriculaRepository.findAll();
    }

    public Optional<Matricula> buscarPorId(Integer id) {
        return matriculaRepository.findById(id);
    }

    public Matricula salvar(Matricula matricula) {

        Plano plano = planoRepository.findById(matricula.getPlanoId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Plano não encontrado"));

        if (matricula.getDataInicio() == null) {
            throw new IllegalArgumentException(
                    "A data de início é obrigatória");
        }

        LocalDate dataFim = matricula.getDataInicio()
                .plusDays(plano.getDuracaoDias());

        matricula.setDataFim(dataFim);

        if (matricula.getStatus() == null ||
                matricula.getStatus().isBlank()) {

            matricula.setStatus("ATIVA");
        }

        return matriculaRepository.save(matricula);
    }

    @Transactional
    public void renovar(Integer matriculaId, LocalDate novaDataFim) {

        if (novaDataFim == null) {
            throw new IllegalArgumentException(
                    "A nova data de vencimento é obrigatória");
        }

        StoredProcedureQuery procedure =
                entityManager.createStoredProcedureQuery(
                        "renovar_matricula");

        procedure.registerStoredProcedureParameter(
                1,
                Integer.class,
                ParameterMode.IN
        );

        procedure.registerStoredProcedureParameter(
                2,
                java.sql.Date.class,
                ParameterMode.IN
        );

        procedure.setParameter(1, matriculaId);
        procedure.setParameter(
                2,
                java.sql.Date.valueOf(novaDataFim)
        );

        procedure.execute();
    }

    public void excluir(Integer id) {
        matriculaRepository.deleteById(id);
    }
}