package br.com.locar.api.mappers;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.enums.ECor;
import br.com.locar.api.enums.EModelo;
import br.com.locar.api.models.CarroModel;
import org.springframework.stereotype.Component;

@Component
public class CarroMapper {

    public CarroModel entityToModel(Carro entity) {
        return new CarroModel()
                .setId(entity.getIdCarro())
                .setCor(entity.getCor().name())
                .setAno(entity.getAno())
                .setPlaca(entity.getPlaca())
                .setModelo(entity.getModelo().getDescricao())
                .setQuilometragem(entity.getQuilometragem());
    }

    public Carro modelToEntity(CarroModel model) {
        return new Carro()
                .setCor(ECor.valueOf(model.getCor()))
                .setAno(model.getAno())
                .setPlaca(model.getPlaca())
                .setModelo(EModelo.getModeloByDescricao(model.getModelo()))
                .setQuilometragem(model.getQuilometragem());
    }
}
