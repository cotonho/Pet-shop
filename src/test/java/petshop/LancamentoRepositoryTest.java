package petshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import petshop.model.*;
import petshop.repository.AnimalRepository;
import petshop.repository.LancamentoRepository;
import petshop.repository.ProprietarioRepository;
import petshop.repository.ServicoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")   // application-test.properties com H2
class LancamentoRepositoryTest {

    @Autowired LancamentoRepository lancamentoRepository;
    @Autowired AnimalRepository animalRepository;
    @Autowired ServicoRepository servicoRepository;
    @Autowired ProprietarioRepository proprietarioRepository;

    @Test
    void deveFiltrarPorAnimalServicoEData() {
        Proprietario p = new Proprietario();
        p.setNome("Maria");
        p.setEmail("maria@test.com");
        p = proprietarioRepository.save(p);

        Animal a = new Animal();
        a.setNome("Rex");
        a.setProprietario(p);
        a.setPeso(10.0);
        a = animalRepository.save(a);

        Servico s = new ServicoBanho();
        s.setNome("Banho");
        s.setPreco(BigDecimal.valueOf(50));
        s = servicoRepository.save(s);

        Lancamento l = new Lancamento();
        l.setAnimal(a);
        l.setServico(s);
        l.setData(LocalDate.of(2025, 1, 15));
        l.setValor(BigDecimal.valueOf(60));
        lancamentoRepository.save(l);

        List<Lancamento> result = lancamentoRepository.findByAnimalIdAndServicoIdAndDataBetween(
                a.getId(), s.getId(), LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));
        assertThat(result).hasSize(1);
    }
}