package br.com.locar.api.services;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.exceptions.NotFoundRegitreException;
import br.com.locar.api.factories.CarroFactory;
import br.com.locar.api.mappers.CarroMapper;
import br.com.locar.api.models.CarroModel;
import br.com.locar.api.repositories.CarroRepository;
import br.com.locar.api.repositories.PageableCarroRepository;
import jakarta.persistence.EntityExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CarroServiceTest {

    private CarroService carroService;

    private CarroRepository carroRepository;

    private PageableCarroRepository pageableCarroRepository;

    private CarroMapper carroMapper;

    @BeforeEach
    void setUpEach() {
        System.out.println("Antes de cada Execução");
        carroRepository = Mockito.mock(CarroRepository.class);
        pageableCarroRepository = Mockito.mock(PageableCarroRepository.class);
        carroMapper = new CarroMapper();
        carroService = new CarroService(carroRepository,pageableCarroRepository, carroMapper);
    }

    @AfterEach
    void clearUpEach(){
        System.out.println("Após cada Execução");
    }

    @BeforeAll
    static void setUp() {
        System.out.println("Antes de todas execuções");
    }

    @AfterAll
    static void clearUp(){
        System.out.println("Após todas execuções");
    }

    @Test
    void deveriaSalvarUmCarro(){
        CarroModel carroModel = CarroFactory.createModel();
        Carro carroSalvo = CarroFactory.createEntity();
        Mockito.when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.empty());
        Mockito.when(carroRepository.save(any(Carro.class))).thenReturn(carroSalvo);

        CarroModel carroRetornado = carroService.salvar(carroModel);

        Assertions.assertThat(carroRetornado).isNotNull();
    }

    @Test
    void deveriaRetonarUmaExcecaoAoSalvarUmCarroComPlacaJaExistente(){
        CarroModel carroModel = CarroFactory.createModel();
        carroModel.setPlaca("XPTO");
        Carro carro = CarroFactory.createEntity();
        Mockito.when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.of(carro));

        Assertions.assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> carroService.salvar(carroModel));
    }

    @Test
    void deveriaAtualizarOsDadosDoCarroCasoEleJaExista() {
        CarroModel carroModel = CarroFactory.createModel();
        carroModel.setPlaca("XPTO");
        Carro carro = CarroFactory.createEntity();
        Mockito.when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.of(carro));
        Mockito.when(carroRepository.save(any(Carro.class))).thenReturn(carro);

        CarroModel carroRetornado = carroService.atualizar(carroModel);
        Assertions.assertThat(carroRetornado).isNotNull();
    }

    @Test
    @DisplayName("Neste teste podemos ver que utilizando spy ele não da erro mesmo não " +
            "declarando o mock do método findByPlaca do carroRepository")
    void deveriaRetornarErroAoTentarAtualizarOsDadosDoCarroCasoEleNaoExista() {
        carroService = Mockito.spy(new CarroService(carroRepository,pageableCarroRepository, carroMapper));
        CarroModel carroModel = CarroFactory.createModel();
        carroModel.setPlaca("XPTO");
//        Mockito.when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(() -> carroService.atualizar(carroModel));
    }

    @Test
    @Timeout(value = 2, unit = SECONDS)
    void listarTodos() {
        Carro carro = CarroFactory.createEntity();
        Pageable pageable = PageRequest.of(0,2);
        Page<Carro> pagesRetornadas = new PageImpl<>(Arrays.asList(carro), pageable, 1);
        when(pageableCarroRepository.findAll(any(Pageable.class))).thenReturn(pagesRetornadas);
        Assertions.assertThat(carroService.listarTodos(pageable)).isNotEmpty();
    }
//
//    @Test
//    void buscarPorPlaca() {
//    }
//
//    @Test
//    void buscarTodosSeminovos() {
//    }
//
//    @Test
//    void deletarCarroPorPlaca() {
//    }
}