package petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petshop.dto.ProprietarioAnimaisDTO;
import petshop.dto.RelatorioCompletoDTO;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final LancamentoService lancamentoService;
    private final ProprietarioService proprietarioService;

    public RelatorioCompletoDTO relatorioFinanceiro(Long proprietarioId, LocalDate inicio, LocalDate fim) {
        return lancamentoService.gerarRelatorio(proprietarioId, inicio, fim);
    }

    public ProprietarioAnimaisDTO dadosClienteComAnimais(Long proprietarioId) {
        return proprietarioService.obterDadosComAnimais(proprietarioId);
    }
}