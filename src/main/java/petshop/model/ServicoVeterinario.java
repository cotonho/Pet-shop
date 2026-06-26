package petshop.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("VETERINARIO")
public class ServicoVeterinario extends Servico {

    @Override
    public BigDecimal calcularValor(Animal animal) {
        BigDecimal base = getPreco() != null ? getPreco() : BigDecimal.valueOf(100);
        return base.add(BigDecimal.valueOf(30)); // taxa de material
    }
}