package petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ServicoTotalDTO {
    private String servico;
    private BigDecimal total;
}