package petshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import petshop.model.Proprietario;
import petshop.service.ProprietarioService;
import petshop.dto.ProprietarioAnimaisDTO;

import java.util.List;

@RestController
@RequestMapping("/api/proprietarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:8080}")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    @GetMapping
    public List<Proprietario> listar() {
        return proprietarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Proprietario buscar(@PathVariable Long id) {
        return proprietarioService.buscarPorId(id);
    }

    @PostMapping
    public Proprietario criar(@Valid @RequestBody Proprietario proprietario) {
        return proprietarioService.salvar(proprietario);
    }

    @PutMapping("/{id}")
    public Proprietario atualizar(@PathVariable Long id, @Valid @RequestBody Proprietario proprietario) {
        proprietario.setId(id);
        return proprietarioService.salvar(proprietario);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        proprietarioService.excluir(id);
    }

    // NOVO: dados do cliente com seus animais
    @GetMapping("/{id}/dados-com-animais")
    public ProprietarioAnimaisDTO dadosComAnimais(@PathVariable Long id) {
        return proprietarioService.obterDadosComAnimais(id);
    }
}