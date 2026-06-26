package petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petshop.model.Proprietario;
import petshop.repository.ProprietarioRepository;
import petshop.dto.ProprietarioAnimaisDTO;
import petshop.dto.AnimalDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProprietarioService {

    private final ProprietarioRepository proprietarioRepository;

    public List<Proprietario> listarTodos() {
        return proprietarioRepository.findAll();
    }

    public Proprietario buscarPorId(Long id) {
        return proprietarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));
    }

    public Proprietario salvar(Proprietario proprietario) {
        return proprietarioRepository.save(proprietario);
    }

    public void excluir(Long id) {
        proprietarioRepository.deleteById(id);
    }

    /**
     * Relatório: dados completos do cliente e seus animais.
     */
    public ProprietarioAnimaisDTO obterDadosComAnimais(Long id) {
        Proprietario prop = buscarPorId(id);
        ProprietarioAnimaisDTO dto = new ProprietarioAnimaisDTO();
        dto.setId(prop.getId());
        dto.setNome(prop.getNome());
        dto.setTelefone(prop.getTelefone());
        dto.setEmail(prop.getEmail());
        dto.setEndereco(prop.getEndereco());
        dto.setAnimais(prop.getAnimais().stream().map(animal -> {
            AnimalDTO a = new AnimalDTO();
            a.setId(animal.getId());
            a.setNome(animal.getNome());
            a.setEspecie(animal.getEspecie());
            a.setRaca(animal.getRaca());
            a.setIdade(animal.getIdade());
            a.setSexo(animal.getSexo());
            a.setPeso(animal.getPeso());
            a.setFoto(animal.getFoto());
            return a;
        }).collect(Collectors.toList()));
        return dto;
    }
}