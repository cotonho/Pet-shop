package petshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import petshop.model.Servico;
import petshop.service.ServicoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:8080}")
public class ServicoController {

    private final ServicoService servicoService;

    @GetMapping
    public List<Servico> listar() {
        return servicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Servico buscar(@PathVariable Long id) {
        return servicoService.buscarPorId(id);
    }

    // Criação com tipo explícito: espera {"nome":..., "descricao":..., "preco":..., "tipo":"BANHO"}
    @PostMapping
    public Servico criar(@RequestBody Map<String, Object> body) {
        String nome = (String) body.get("nome");
        String descricao = (String) body.get("descricao");
        Double preco = ((Number) body.get("preco")).doubleValue();
        String tipo = (String) body.get("tipo");
        return servicoService.criarServico(nome, descricao, preco, tipo);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        servicoService.excluir(id);
    }
}