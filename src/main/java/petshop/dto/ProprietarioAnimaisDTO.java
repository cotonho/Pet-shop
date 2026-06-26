package petshop.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProprietarioAnimaisDTO {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private List<AnimalDTO> animais;
}