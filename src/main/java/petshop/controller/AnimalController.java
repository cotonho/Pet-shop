package petshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import petshop.model.Animal;
import petshop.service.AnimalService;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:8080}")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    public List<Animal> listar() {
        return animalService.listarTodos();
    }

    @GetMapping("/{id}")
    public Animal buscar(@PathVariable Long id) {
        return animalService.buscarPorId(id);
    }

    @GetMapping("/proprietario/{proprietarioId}")
    public List<Animal> porProprietario(@PathVariable Long proprietarioId) {
        return animalService.listarPorProprietario(proprietarioId);
    }

    @PostMapping
    public Animal criar(@Valid @RequestBody Animal animal) {
        return animalService.salvar(animal);
    }

    @PutMapping("/{id}")
    public Animal atualizar(@PathVariable Long id, @Valid @RequestBody Animal animal) {
        animal.setId(id);
        return animalService.salvar(animal);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        animalService.excluir(id);
    }
}