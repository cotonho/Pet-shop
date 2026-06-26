package petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RelatorioDiarioServicoDTO {
    private LocalDate data;
    private String servico;
    private BigDecimal total;
}