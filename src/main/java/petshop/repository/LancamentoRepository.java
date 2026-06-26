package petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import petshop.model.Lancamento;
import petshop.dto.RelatorioDiarioServicoDTO;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByAnimalId(Long animalId);

    List<Lancamento> findByAnimalIdAndDataBetween(Long animalId, LocalDate inicio, LocalDate fim);

    // NOVO: filtro combinado por animal, serviço e período
    List<Lancamento> findByAnimalIdAndServicoIdAndDataBetween(
            Long animalId, Long servicoId, LocalDate inicio, LocalDate fim);

    // Lançamentos de um proprietário em um período (já existia)
    @Query("SELECT l FROM Lancamento l JOIN l.animal a WHERE a.proprietario.id = :proprietarioId " +
           "AND l.data BETWEEN :inicio AND :fim")
    List<Lancamento> findByProprietarioIdAndPeriodo(
            @Param("proprietarioId") Long proprietarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    // NOVO: agrupamento diário por serviço (para relatório)
    @Query("SELECT new petshop.dto.RelatorioDiarioServicoDTO(l.data, l.servico.nome, SUM(l.valor)) " +
           "FROM Lancamento l JOIN l.animal a " +
           "WHERE a.proprietario.id = :proprietarioId " +
           "AND l.data BETWEEN :inicio AND :fim " +
           "GROUP BY l.data, l.servico.nome " +
           "ORDER BY l.data, l.servico.nome")
    List<RelatorioDiarioServicoDTO> totalDiarioPorServico(
            @Param("proprietarioId") Long proprietarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);
}