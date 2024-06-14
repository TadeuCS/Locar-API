package br.com.locar.api.entities;


import br.com.locar.api.enums.ECor;
import br.com.locar.api.enums.EModelo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@Entity
@Table(name = "Carro")
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

    public BigDecimal getQuilometragem() {
        if(quilometragem==null){
            return BigDecimal.ZERO;
        }
        return quilometragem;
    }
}
