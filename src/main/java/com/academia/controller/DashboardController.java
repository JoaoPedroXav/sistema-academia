package com.academia.controller;

import com.academia.model.ViewMatricula;
import com.academia.repository.ViewMatriculaRepository;
import com.academia.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final AlunoService alunoService;
    private final ViewMatriculaRepository viewMatriculaRepository;

    public DashboardController(
            AlunoService alunoService,
            ViewMatriculaRepository viewMatriculaRepository) {

        this.alunoService = alunoService;
        this.viewMatriculaRepository = viewMatriculaRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        List<ViewMatricula> matriculas =
                viewMatriculaRepository.findAll();

        long alunos = alunoService.listarTodos().size();

        long matriculasAtivas = matriculas.stream()
                .filter(m -> "ATIVA".equalsIgnoreCase(m.getSituacao()))
                .count();

        long matriculasVencidas = matriculas.stream()
                .filter(m -> "VENCIDA".equalsIgnoreCase(m.getSituacao()))
                .count();

        model.addAttribute("alunos", alunos);
        model.addAttribute("matriculasAtivas", matriculasAtivas);
        model.addAttribute("matriculasVencidas", matriculasVencidas);
        model.addAttribute("matriculas", matriculas);

        return "dashboard";
    }
}
