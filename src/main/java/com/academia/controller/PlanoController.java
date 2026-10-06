package com.academia.controller;

import com.academia.model.Plano;
import com.academia.service.PlanoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("planos", planoService.listarTodos());
        return "planos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("plano", new Plano());
        return "planos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Plano plano) {
        planoService.salvar(plano);
        return "redirect:/planos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Plano plano = planoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado"));

        model.addAttribute("plano", plano);

        return "planos/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        planoService.excluir(id);
        return "redirect:/planos";
    }
}
