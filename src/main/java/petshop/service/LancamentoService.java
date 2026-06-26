package petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petshop.model.*;
import petshop.repository.LancamentoRepository;
import petshop.dto.RelatorioDiarioServicoDTO;
import petshop.dto.RelatorioCompletoDTO;
import petshop.dto.ServicoTotalDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final AnimalService animalService;
    private final ServicoService servicoService;

    public List<Lancamento> listarTodos() {
        return lancamentoRepository.findAll();
    }

    public Lancamento buscarPorId(Long id) {
        return lancamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado"));
    }

    /**
     * Cria um lançamento, calculando o valor automaticamente com base no
     * polimorfismo do Servico (chama calcularValor(animal)).
     */
    public Lancamento criar(Lancamento lancamento) {
        // Busca as entidades reais do banco
        Animal animal = animalService.buscarPorId(lancamento.getAnimal().getId());
        Servico servico = servicoService.buscarPorId(lancamento.getServico().getId());

        // Calcula valor usando polimorfismo
        BigDecimal valorCalculado = servico.calcularValor(animal);

        lancamento.setAnimal(animal);
        lancamento.setServico(servico);
        lancamento.setValor(valorCalculado);

        return lancamentoRepository.save(lancamento);
    }

    public void excluir(Long id) {
        lancamentoRepository.deleteById(id);
    }

    // Histórico por animal com filtros opcionais
    public List<Lancamento> historicoAnimal(Long animalId, LocalDate inicio, LocalDate fim, Long servicoId) {
        if (servicoId != null && inicio != null && fim != null) {
            return lancamentoRepository.findByAnimalIdAndServicoIdAndDataBetween(animalId, servicoId, inicio, fim);
        } else if (inicio != null && fim != null) {
            return lancamentoRepository.findByAnimalIdAndDataBetween(animalId, inicio, fim);
        } else {
            return lancamentoRepository.findByAnimalId(animalId);
        }
    }

    // Relatório completo: totais por serviço e por data
    public RelatorioCompletoDTO gerarRelatorio(Long proprietarioId, LocalDate inicio, LocalDate fim) {
        List<Lancamento> lancamentos = lancamentoRepository.findByProprietarioIdAndPeriodo(proprietarioId, inicio, fim);

        // Agrupamento por serviço
        Map<String, BigDecimal> porServico = lancamentos.stream()
                .collect(Collectors.groupingBy(l -> l.getServico().getNome(),
                        Collectors.reducing(BigDecimal.ZERO, Lancamento::getValor, BigDecimal::add)));

        List<ServicoTotalDTO> servicoTotais = porServico.entrySet().stream()
                .map(e -> new ServicoTotalDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        // Agrupamento diário por serviço (consulta otimizada)
        List<RelatorioDiarioServicoDTO> diario = lancamentoRepository.totalDiarioPorServico(proprietarioId, inicio,
                fim);

        BigDecimal totalGeral = lancamentos.stream()
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RelatorioCompletoDTO dto = new RelatorioCompletoDTO();
        dto.setTotalGeral(totalGeral);
        dto.setTotaisPorServico(servicoTotais);
        dto.setDiarioPorServico(diario);
        return dto;
    }
}