package br.com.locar.api.factories;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.enums.ECor;
import br.com.locar.api.enums.EModelo;
import br.com.locar.api.models.CarroModel;

import java.time.LocalDate;

public class CarroFactory {
    public static CarroModel createModel() {
        return new CarroModel()
                .setModelo(EModelo.GOL_G8.getDescricao())
                .setCor(ECor.BRANCO.name())
                .setAno(LocalDate.now().getYear());
    }

    public static Carro createEntity() {
        return new Carro()
                .setModelo(EModelo.GOL_G8)
                .setCor(ECor.BRANCO)
                .setAno(LocalDate.now().getYear());
    }
}
