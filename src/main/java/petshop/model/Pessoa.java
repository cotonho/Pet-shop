package petshop.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public abstract class Pessoa {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String telefone;

    @Email(message = "Email inválido")
    private String email;

    private String endereco;
}