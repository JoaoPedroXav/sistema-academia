package com.academia.controller;

import com.academia.model.Aluno;
import com.academia.model.CadastroAlunoForm;
import com.academia.repository.MatriculaRepository;
import com.academia.repository.PlanoRepository;
import com.academia.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final PlanoRepository planoRepository;
    private final MatriculaRepository matriculaRepository;

    public AlunoController(
            AlunoService alunoService,
            PlanoRepository planoRepository,
            MatriculaRepository matriculaRepository) {

        this.alunoService = alunoService;
        this.planoRepository = planoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "alunos",
                alunoService.listarTodos()
        );

        return "alunos/lista";
    }

    /*
     * NOVO ALUNO
     */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("form", new CadastroAlunoForm());
        model.addAttribute("planos", planoRepository.findAll());
        model.addAttribute("formAction", "/alunos/salvar");

        return "alunos/formulario";
    }

    /*
     * SALVAR NOVO ALUNO
     */
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("form") CadastroAlunoForm form) {

        alunoService.cadastrarAlunoComMatricula(form);

        return "redirect:/";
    }

    /*
     * EDITAR ALUNO
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        Aluno aluno = alunoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        CadastroAlunoForm form = new CadastroAlunoForm();

        form.setNome(aluno.getNome());
        form.setCpf(aluno.getCpf());
        form.setEmail(aluno.getEmail());
        form.setTelefone(aluno.getTelefone());

        matriculaRepository.findFirstByAlunoId(id)
                .ifPresent(matricula -> {
                    form.setPlanoId(matricula.getPlanoId());
                    form.setDataInicio(matricula.getDataInicio());
                    form.setStatus(matricula.getStatus());
                });

        model.addAttribute("form", form);
        model.addAttribute("alunoId", id);
        model.addAttribute("planos", planoRepository.findAll());

        // URL que o formulário deverá enviar
        model.addAttribute("formAction", "/alunos/editar/" + id);

        return "alunos/formulario";
    }

    /*
     * ATUALIZAR ALUNO
     */
    @PostMapping("/editar/{id}")
    public String atualizar(
            @PathVariable Integer id,
            @ModelAttribute("form") CadastroAlunoForm form) {

        alunoService.atualizarAlunoComMatricula(id, form);

        return "redirect:/";
    }
}