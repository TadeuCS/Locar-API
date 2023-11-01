package br.com.locar.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Viagem")
@Data
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVIAGEM")
    private Integer idViagem;
    @Column(name = "DHINICIO")
    private LocalDateTime dhInicio;
    @Column(name = "DHFIM")
    private LocalDateTime dhFim;
    @Column(name = "DISTANCIA")
    private BigDecimal distancia;
    @ManyToOne(optional = false)
    @JoinColumn(name = "IDCARRO", nullable = false)
    private Carro idCarro;
}
