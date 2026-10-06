package com.academia.service;

import com.academia.model.Aluno;
import com.academia.model.CadastroAlunoForm;
import com.academia.model.Matricula;
import com.academia.model.Plano;
import com.academia.repository.AlunoRepository;
import com.academia.repository.MatriculaRepository;
import com.academia.repository.PlanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final PlanoRepository planoRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            MatriculaRepository matriculaRepository,
            PlanoRepository planoRepository) {

        this.alunoRepository = alunoRepository;
        this.matriculaRepository = matriculaRepository;
        this.planoRepository = planoRepository;
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Integer id) {
        return alunoRepository.findById(id);
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    /*
     * NOVO ALUNO + MATRÍCULA
     */
    @Transactional
    public void cadastrarAlunoComMatricula(CadastroAlunoForm form) {

        Aluno aluno = new Aluno();

        aluno.setNome(form.getNome());
        aluno.setCpf(form.getCpf());
        aluno.setEmail(form.getEmail());
        aluno.setTelefone(form.getTelefone());
        aluno.setAtivo(true);

        alunoRepository.save(aluno);

        Plano plano = planoRepository.findById(form.getPlanoId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Plano não encontrado"));

        if (form.getDataInicio() == null) {
            throw new IllegalArgumentException(
                    "A data de início é obrigatória");
        }

        LocalDate dataFim = form.getDataInicio()
                .plusDays(plano.getDuracaoDias());

        Matricula matricula = new Matricula();

        matricula.setAlunoId(aluno.getId());
        matricula.setPlanoId(plano.getId());
        matricula.setDataInicio(form.getDataInicio());
        matricula.setDataFim(dataFim);

        if (form.getStatus() == null || form.getStatus().isBlank()) {
            matricula.setStatus("ATIVA");
        } else {
            matricula.setStatus(form.getStatus());
        }

        matriculaRepository.save(matricula);
    }

    /*
     * EDITAR ALUNO + MATRÍCULA
     */
    @Transactional
    public void atualizarAlunoComMatricula(
            Integer alunoId,
            CadastroAlunoForm form) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Aluno não encontrado"));

        aluno.setNome(form.getNome());
        aluno.setCpf(form.getCpf());
        aluno.setEmail(form.getEmail());
        aluno.setTelefone(form.getTelefone());

        alunoRepository.save(aluno);

        /*
         * Procura a matrícula desse aluno.
         *
         * Como o sistema possui uma matrícula associada
         * ao cadastro, buscamos a matrícula pelo aluno.
         */
        Matricula matricula = matriculaRepository
                .findFirstByAlunoId(alunoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Matrícula do aluno não encontrada"));

        Plano plano = planoRepository.findById(form.getPlanoId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Plano não encontrado"));

        if (form.getDataInicio() == null) {
            throw new IllegalArgumentException(
                    "A data de início é obrigatória");
        }

        LocalDate dataFim = form.getDataInicio()
                .plusDays(plano.getDuracaoDias());

        matricula.setPlanoId(plano.getId());
        matricula.setDataInicio(form.getDataInicio());
        matricula.setDataFim(dataFim);

        if (form.getStatus() == null || form.getStatus().isBlank()) {
            matricula.setStatus("ATIVA");
        } else {
            matricula.setStatus(form.getStatus());
        }

        matriculaRepository.save(matricula);
    }
}