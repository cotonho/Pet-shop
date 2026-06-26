package petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petshop.model.Animal;
import petshop.repository.AnimalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public List<Animal> listarTodos() {
        return animalRepository.findAll();
    }

    public Animal buscarPorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
    }

    public List<Animal> listarPorProprietario(Long proprietarioId) {
        return animalRepository.findByProprietarioId(proprietarioId);
    }

    public Animal salvar(Animal animal) {
        return animalRepository.save(animal);
    }

    public void excluir(Long id) {
        animalRepository.deleteById(id);
    }
}