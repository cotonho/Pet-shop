package petshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import petshop.model.*;
import petshop.repository.LancamentoRepository;
import petshop.service.AnimalService;
import petshop.service.LancamentoService;
import petshop.service.ServicoService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LancamentoServiceTest {

    @Mock LancamentoRepository lancamentoRepository;
    @Mock AnimalService animalService;
    @Mock ServicoService servicoService;
    @InjectMocks LancamentoService lancamentoService;

    private Animal animal;
    private Servico servicoBanho;

    @BeforeEach
    void setUp() {
        animal = new Animal();
        animal.setId(1L);
        animal.setPeso(15.0);

        servicoBanho = new ServicoBanho();
        servicoBanho.setId(1L);
        servicoBanho.setPreco(BigDecimal.valueOf(60));
    }

    @Test
    void deveCalcularValorAutomatico() {
        Lancamento l = new Lancamento();
        l.setAnimal(animal);
        l.setServico(servicoBanho);
        l.setData(LocalDate.now());

        when(animalService.buscarPorId(1L)).thenReturn(animal);
        when(servicoService.buscarPorId(1L)).thenReturn(servicoBanho);
        when(lancamentoRepository.save(any())).thenReturn(l);

        Lancamento salvo = lancamentoService.criar(l);

        // peso 15 kg -> 20% extra sobre 60 = 72
        assertEquals(0, BigDecimal.valueOf(72).compareTo(salvo.getValor()));
        verify(animalService).buscarPorId(1L);
        verify(servicoService).buscarPorId(1L);
    }
}