package petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petshop.model.*;
import petshop.repository.ServicoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    /**
     * Cria o serviço de acordo com o discriminador informado.
     * @param tipo "BANHO", "VETERINARIO" ou "HOSPEDAGEM"
     */
    public Servico criarServico(String nome, String descricao, Double preco, String tipo) {
        Servico servico;
        switch (tipo.toUpperCase()) {
            case "BANHO":
                servico = new ServicoBanho();
                break;
            case "VETERINARIO":
                servico = new ServicoVeterinario();
                break;
            case "HOSPEDAGEM":
                servico = new ServicoHospedagem();
                break;
            default:
                throw new IllegalArgumentException("Tipo de serviço inválido: " + tipo);
        }
        servico.setNome(nome);
        servico.setDescricao(descricao);
        servico.setPreco(java.math.BigDecimal.valueOf(preco));
        return servicoRepository.save(servico);
    }

    public void excluir(Long id) {
        servicoRepository.deleteById(id);
    }
}