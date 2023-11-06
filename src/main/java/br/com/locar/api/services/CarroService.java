package br.com.locar.api.services;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.exceptions.NotFoundRegitreException;
import br.com.locar.api.mappers.CarroMapper;
import br.com.locar.api.models.CarroModel;
import br.com.locar.api.repositories.CarroRepository;
import br.com.locar.api.repositories.PageableCarroRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarroService {

    public static final BigDecimal MAX_QUILOMETRAGEM_PARA_LOCACAO = new BigDecimal("40000");

    private final CarroRepository carroRepository;

    private final PageableCarroRepository pageableCarroRepository;

    public final CarroMapper carroMapper;

    public CarroModel salvar(CarroModel model) {
        boolean existeCarroRegistrado = existeCarroRegistradoPorPlaca(model.getPlaca());
        if(existeCarroRegistrado){
            throw new EntityExistsException("Já existe um carro registrado com a placa: "+ model.getPlaca());
        }
        Carro carro = carroMapper.modelToEntity(model);
        Carro carroSalvo = carroRepository.save(carro);
        return carroMapper.entityToModel(carroSalvo);
    }

    public CarroModel atualizar(CarroModel model) {
        CarroModel carroEncontrado = buscarPorPlaca(model.getPlaca());
        carroEncontrado.setModelo(model.getModelo());
        carroEncontrado.setCor(model.getCor());
        carroEncontrado.setAno(model.getAno());
        carroEncontrado.setQuilometragem(model.getQuilometragem());
        Carro carro = carroMapper.modelToEntity(carroEncontrado);
        Carro carroSalvo = carroRepository.save(carro);
        return carroMapper.entityToModel(carroSalvo);
    }

    public Page<CarroModel> listarTodos(Pageable pageable) {
        Page<Carro> pageableResult = pageableCarroRepository.findAll(pageable);
        List<CarroModel> models = pageableResult
                .stream()
                .map(carroMapper::entityToModel)
                .toList();
        return new PageImpl<>(models, pageableResult.getPageable(), pageableResult.getTotalPages());
    }

    public CarroModel buscarPorPlaca(String placa) {
        Optional<Carro> carroSalvo = carroRepository.findByPlaca(placa);
        if(carroSalvo.isEmpty()){
            throw new NotFoundRegitreException("Carro não encontrado com essa placa: "+placa);
        }
        return carroMapper.entityToModel(carroSalvo.get());
    }

    public boolean existeCarroRegistradoPorPlaca(String placa) {
        Optional<Carro> carroSalvo = carroRepository.findByPlaca(placa);
        return carroSalvo.isPresent();
    }

    public Page<CarroModel> buscarTodosSeminovos(Pageable pageable) {
        return buscarTodosSeminovos(pageable, MAX_QUILOMETRAGEM_PARA_LOCACAO);
    }

    public Page<CarroModel> buscarTodosSeminovos(Pageable pageable, BigDecimal maxQuilometragem) {
        Page<Carro> pageableResult = pageableCarroRepository.findAll(pageable);
        List<CarroModel> models = pageableResult
                .stream()
                .filter(carro -> carro.getAno()<LocalDate.now().getYear() || carro.getQuilometragem().compareTo(maxQuilometragem)>=0)
                .map(carroMapper::entityToModel)
                .toList();
        return new PageImpl<>(models, pageableResult.getPageable(), pageableResult.getTotalPages());
    }

    public void deletarCarroPorPlaca(String placa) throws NotFoundRegitreException{
        CarroModel carroModel = buscarPorPlaca(placa);
        carroRepository.deleteById(carroModel.getId());
    }
}
