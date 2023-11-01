package br.com.locar.api.entities;


import br.com.locar.api.enums.ECor;
import br.com.locar.api.enums.EModelo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "Carro")
@Data
public class Carro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCARRO")
    private Integer idCarro;
    @Column(name = "COR", nullable = false)
    private ECor cor;
    @Column(name = "MODELO", nullable = false)
    private EModelo modelo;
    @Column(name = "ANO", nullable = false)
    private Integer ano;
    @Column(name = "PLACA", unique = true, nullable = false)
    private String placa;
    @Column(name = "QUILOMETRAGEM", nullable = false)
    private BigDecimal quilometragem;

    public Carro setCor(ECor cor) {
        this.cor = cor;
        return this;
    }

    public Carro setModelo(EModelo modelo) {
        this.modelo = modelo;
        return this;
    }

    public Carro setAno(Integer ano) {
        this.ano = ano;
        return this;
    }

    public Carro setPlaca(String placa) {
        this.placa = placa;
        return this;
    }

    public Carro setQuilometragem(BigDecimal quilometragem) {
        this.quilometragem = quilometragem;
        return this;
    }

    public BigDecimal getQuilometragem() {
        if(quilometragem==null){
            quilometragem=BigDecimal.ZERO;
        }
        return quilometragem;
    }
}
