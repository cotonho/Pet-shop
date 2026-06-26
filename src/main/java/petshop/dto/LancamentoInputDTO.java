package petshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LancamentoInputDTO {
    @NotNull(message = "ID do animal é obrigatório")
    private Long animalId;

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    private String observacoes;
}