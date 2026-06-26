package petshop.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("HOSPEDAGEM")
public class ServicoHospedagem extends Servico {

    @Override
    public BigDecimal calcularValor(Animal animal) {
        // preco já é diário; nenhum acréscimo extra
        return getPreco() != null ? getPreco() : BigDecimal.valueOf(80);
    }
}