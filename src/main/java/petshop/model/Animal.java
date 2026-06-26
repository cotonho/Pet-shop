package petshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String especie;

    private String raca;

    @Min(0)
    private Integer idade;

    private String sexo;

    @Positive
    private Double peso;

    private String foto; // URL ou caminho da imagem (opcional)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_id")
    @JsonIgnore
    @NotNull(message = "Proprietário é obrigatório")
    private Proprietario proprietario;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Lancamento> lancamentos = new ArrayList<>();
}