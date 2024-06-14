package br.com.locar.api.factories;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.enums.ECor;
import br.com.locar.api.enums.EModelo;
import br.com.locar.api.models.CarroModel;

import java.time.LocalDate;

public class CarroFactory {
    public static CarroModel createModel() {
        return CarroModel.builder()
                .id(1)
                .modelo(EModelo.GOL_G8.getDescricao())
                .cor(ECor.BRANCO.name())
                .ano(LocalDate.now().getYear())
                .build();
    }

    public static Carro createEntity() {
        return createEntity(null);
    }

    public static Carro createEntity(String placa) {
        return Carro.builder()
                .idCarro(1)
                .modelo(EModelo.GOL_G8)
                .cor(ECor.BRANCO)
                .ano(LocalDate.now().getYear())
                .placa(placa)
                .build();
    }
}
