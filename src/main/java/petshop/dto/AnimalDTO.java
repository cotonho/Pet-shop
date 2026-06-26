package petshop.dto;

import lombok.Data;

@Data
public class AnimalDTO {
    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private Integer idade;
    private String sexo;
    private Double peso;
    private String foto;
}