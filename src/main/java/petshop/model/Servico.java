package petshop.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_servico", discriminatorType = DiscriminatorType.STRING)
@Getter @Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "tipo_servico", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ServicoBanho.class, name = "BANHO"),
    @JsonSubTypes.Type(value = ServicoVeterinario.class, name = "VETERINARIO"),
    @JsonSubTypes.Type(value = ServicoHospedagem.class, name = "HOSPEDAGEM")
})
public abstract class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    @Positive(message = "Preço deve ser positivo")
    protected BigDecimal preco;

    /**
     * Calcula o valor final do serviço considerando as características do animal.
     * Cada subclasse implementa sua própria regra de negócio.
     */
    public abstract BigDecimal calcularValor(Animal animal);
}