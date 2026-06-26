package petshop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RelatorioCompletoDTO {
    private BigDecimal totalGeral;
    private List<ServicoTotalDTO> totaisPorServico;
    private List<RelatorioDiarioServicoDTO> diarioPorServico;
}