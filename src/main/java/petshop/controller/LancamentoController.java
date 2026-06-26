package petshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import petshop.model.Animal;
import petshop.model.Lancamento;
import petshop.model.Servico;
import petshop.service.LancamentoService;
import petshop.dto.LancamentoInputDTO;
import petshop.dto.RelatorioCompletoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:8080}")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @GetMapping
    public List<Lancamento> listar() {
        return lancamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Lancamento buscar(@PathVariable Long id) {
        return lancamentoService.buscarPorId(id);
    }

    @PostMapping
public ResponseEntity<Lancamento> criar(@Valid @RequestBody LancamentoInputDTO input) {
    Lancamento lancamento = new Lancamento();
    lancamento.setData(input.getData());
    lancamento.setObservacoes(input.getObservacoes());
    
    // Apenas IDs - o service vai buscar as entidades reais
    Animal animal = new Animal();
    animal.setId(input.getAnimalId());
    Servico servico = new Servico() { // placeholder concreto mínimo para evitar null
        @Override
        public BigDecimal calcularValor(Animal animal) { return BigDecimal.ZERO; }
    };
    servico.setId(input.getServicoId());
    
    lancamento.setAnimal(animal);
    lancamento.setServico(servico);
    
    Lancamento salvo = lancamentoService.criar(lancamento);
    return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
}

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        lancamentoService.excluir(id);
    }

    // Histórico por animal com filtros combinados
    @GetMapping("/animal/{animalId}/historico")
    public List<Lancamento> historicoAnimal(
            @PathVariable Long animalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long servicoId) {
        return lancamentoService.historicoAnimal(animalId, dataInicio, dataFim, servicoId);
    }

    // Relatório completo
    @GetMapping("/relatorio")
    public RelatorioCompletoDTO relatorio(
            @RequestParam Long proprietarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return lancamentoService.gerarRelatorio(proprietarioId, dataInicio, dataFim);
    }
}