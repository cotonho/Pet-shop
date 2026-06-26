package petshop.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BANHO")
public class ServicoBanho extends Servico {

    @Override
    public BigDecimal calcularValor(Animal animal) {
        if (animal == null) return getPreco() != null ? getPreco() : BigDecimal.ZERO;
        double peso = animal.getPeso() != null ? animal.getPeso() : 0;
        BigDecimal base = getPreco() != null ? getPreco() : BigDecimal.valueOf(50);
        // Acréscimo para animais grandes
        if (peso > 20) {
            return base.multiply(BigDecimal.valueOf(1.5)); // 50% extra
        } else if (peso > 10) {
            return base.multiply(BigDecimal.valueOf(1.2));
        }
        return base;
    }
}