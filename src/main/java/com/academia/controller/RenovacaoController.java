package com.academia.controller;

import com.academia.model.Aluno;
import com.academia.model.Matricula;
import com.academia.model.Plano;
import com.academia.repository.AlunoRepository;
import com.academia.repository.PlanoRepository;
import com.academia.service.MatriculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/matriculas")
public class RenovacaoController {

    private final MatriculaService matriculaService;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;

    public RenovacaoController(
            MatriculaService matriculaService,
            AlunoRepository alunoRepository,
            PlanoRepository planoRepository) {

        this.matriculaService = matriculaService;
        this.alunoRepository = alunoRepository;
        this.planoRepository = planoRepository;
    }

    @GetMapping("/renovar/{id}")
    public String formularioRenovacao(
            @PathVariable Integer id,
            Model model) {

        Matricula matricula = matriculaService.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Matrícula não encontrada"));

        Aluno aluno = alunoRepository.findById(
                matricula.getAlunoId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Aluno não encontrado"));

        Plano plano = planoRepository.findById(
                matricula.getPlanoId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Plano não encontrado"));

        /*
         * Sugestão de nova data:
         * vencimento atual + duração do plano.
         */
        LocalDate novaDataSugerida =
                matricula.getDataFim()
                        .plusDays(plano.getDuracaoDias());

        model.addAttribute("matricula", matricula);
        model.addAttribute("aluno", aluno);
        model.addAttribute("plano", plano);
        model.addAttribute("novaDataSugerida", novaDataSugerida);

        return "matriculas/renovar";
    }

    @PostMapping("/renovar/{id}")
    public String renovar(
            @PathVariable Integer id,
            @RequestParam("novaDataFim") LocalDate novaDataFim) {

        matriculaService.renovar(id, novaDataFim);

        return "redirect:/";
    }
}