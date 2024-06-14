package br.com.locar.api.mappers;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.enums.ECor;
import br.com.locar.api.enums.EModelo;
import br.com.locar.api.models.CarroModel;
import org.springframework.stereotype.Component;

@Component
public class CarroMapper {

    public CarroModel entityToModel(Carro entity) {
        return CarroModel.builder()
                .id(entity.getIdCarro())
                .cor(entity.getCor().name())
                .ano(entity.getAno())
                .placa(entity.getPlaca())
                .modelo(entity.getModelo().getDescricao())
                .quilometragem(entity.getQuilometragem())
                .build();
    }

    public Carro modelToEntity(CarroModel model) {
        return Carro.builder()
                .cor(ECor.valueOf(model.getCor()))
                .ano(model.getAno())
                .placa(model.getPlaca())
                .modelo(EModelo.getModeloByDescricao(model.getModelo()))
                .quilometragem(model.getQuilometragem())
                .build();
    }
}
