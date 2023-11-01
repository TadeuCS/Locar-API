package br.com.locar.api.models;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarroModel {
    @NotNull(message = "Campo obrigatório não informado")
    private String cor;
    @NotNull(message = "Campo obrigatório não informado")
    private String modelo;
    @NotNull(message = "Campo obrigatório não informado")
    private Integer ano;
    @NotNull(message = "Campo obrigatório não informado")
    private String placa;
    private BigDecimal quilometragem=BigDecimal.ZERO;

    public CarroModel setCor(String cor) {
        this.cor = cor;
        return this;
    }

    public CarroModel setModelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    public CarroModel setAno(Integer ano) {
        this.ano = ano;
        return this;
    }

    public CarroModel setPlaca(String placa) {
        this.placa = placa;
        return this;
    }

    public CarroModel setQuilometragem(BigDecimal quilometragem) {
        this.quilometragem = quilometragem;
        return this;
    }
}
