package br.com.locar.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class CarroModel {
    @JsonIgnore
    private Integer id;
    @NotNull(message = "Campo obrigatório não informado")
    private String cor;
    @NotNull(message = "Campo obrigatório não informado")
    private String modelo;
    @NotNull(message = "Campo obrigatório não informado")
    private Integer ano;
    @NotNull(message = "Campo obrigatório não informado")
    private String placa;
    private BigDecimal quilometragem;
}
