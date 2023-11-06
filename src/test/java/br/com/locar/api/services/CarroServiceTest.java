package br.com.locar.api.services;

import br.com.locar.api.entities.Carro;
import br.com.locar.api.exceptions.NotFoundRegitreException;
import br.com.locar.api.factories.CarroFactory;
import br.com.locar.api.mappers.CarroMapper;
import br.com.locar.api.models.CarroModel;
import br.com.locar.api.repositories.CarroRepository;
import br.com.locar.api.repositories.PageableCarroRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PageableCarroRepository pageableCarroRepository;

    private static CarroMapper mapper;

    private CarroService carroService;
    private List<Carro> carros;
    private CarroModel carroModel;
    private Carro carroEntity;

    @BeforeAll
    static void setUp(){
        mapper = new CarroMapper();
    }

    @BeforeEach
    void setUpEach() {
        carros=new ArrayList<>();
        carroModel = CarroFactory.createModel();
        carroEntity = CarroFactory.createEntity();
        carroService = new CarroService(carroRepository, pageableCarroRepository, mapper);
    }

    @AfterEach
    void clearUpEach(){
    }

    @AfterAll
    static void clearUp(){
    }

    @Test
    void deveriaSalvarUmCarro(){
        //given
        carroModel.setPlaca("XPTO");
        when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.empty());
        when(carroRepository.save(any(Carro.class))).thenReturn(carroEntity);

        //when
        CarroModel carroRetornado = carroService.salvar(carroModel);

        //then
        assertThat(carroRetornado).isNotNull();
    }

    @Test
    void deveriaRetonarUmaExcecaoAoSalvarUmCarroComPlacaJaExistente(){
        carroModel.setPlaca("XPTO");

        when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.of(carroEntity));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> carroService.salvar(carroModel));
    }

    @Test
    void deveriaAtualizarOsDadosDoCarroCasoEleJaExista() {
        String placa = "XPTO";
        
        carroModel.setPlaca(placa);
        carroEntity.setPlaca(placa);
        doReturn(Optional.of(carroEntity)).when(carroRepository).findByPlaca(anyString());
        doReturn(carroEntity).when(carroRepository).save(any(Carro.class));

        CarroModel carroRetornado = carroService.atualizar(carroModel);
        assertThat(carroRetornado).isNotNull();
    }

    @Test
    @DisplayName("Neste teste podemos ver que utilizando spy ele nao da erro mesmo nao " +
            "declarando o mock do metodo findByPlaca do carroRepository")
    void deveriaRetornarErroAoTentarAtualizarOsDadosDoCarroCasoEleNaoExista() {
        carroModel.setPlaca("XPTO");
        carroService = spy(this.carroService);
        doThrow(NotFoundRegitreException.class).when(carroService).buscarPorPlaca(anyString());

        assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(() -> carroService.atualizar(carroModel));
    }

    @Test
//    @Timeout(value = 2, unit = SECONDS)
    void deveriaRetornarUmaListaContendoUmCarro() {
        carros.add(carroEntity);
        Pageable pageable = PageRequest.of(0,2);
        Page<Carro> pagesRetornadas = new PageImpl<>(carros, pageable, 1);
        when(pageableCarroRepository.findAll(any(Pageable.class))).thenReturn(pagesRetornadas);
        assertThat(carroService.listarTodos(pageable)).isNotEmpty();
    }

    /*
        Dado que seja informado uma placa que contenha no banco dados,
        Quando eu buscar no banco de dados por esta placa
        Então eu quero que retorne os dados do carro encontrado,
        Ou caso não seja encontrado quero que retorne uma exceção detalhando que não foi encontrado um carro com aquela placa
     */
    @Test
    void deveriaRetornarUmCarroQueContenhaPlacaInformada() {
        //given
        String placa = "HBJ-5579";
        when(carroRepository.findByPlaca(placa)).thenReturn(Optional.of(carroEntity));
        //when
        CarroModel actual = carroService.buscarPorPlaca(placa);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getPlaca()).isEqualTo(carroEntity.getPlaca());
    }

    @Test
    void deveriaRetornarUmaExcecaoQueContenhaPlacaInformadaAoTentarEncontrarUmCarroComEstaPlaca() {
        //given
        String placa = "HBJ-5579";
        when(carroRepository.findByPlaca(placa)).thenReturn(Optional.empty());

        //then - when
        assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(()-> carroService.buscarPorPlaca(placa));
    }

    /*
        Dado que seja buscando os caracterizados como seminovos,
        Quando eu pesquisar no banco de dados
        Então eu quero que retorne uma lista contendo os carros que tenha mais de 12 meses de fabricação,
     */
    @Test
    void deveriaRetornarUmaListaContendoCarrosQueEstaoComMaisDeDozeMesesDeFabricacao() {
        //given
        int anoAtual = LocalDate.now().getYear();
        Carro carro1 = CarroFactory.createEntity("FAS-5461");
        carro1.setAno(anoAtual-2);
        Carro carro2 = CarroFactory.createEntity("FAS-5462");
        carro2.setAno(anoAtual-1);
        Carro carro3 = CarroFactory.createEntity("FAS-5463");
        carro3.setAno(anoAtual);
        carros.addAll(Arrays.asList(carro1, carro2, carro3));

        Pageable pageable = PageRequest.of(0,10);
        Page<Carro> pageCarrosRetornados = new PageImpl<>(carros, pageable, 1);
        doReturn(pageCarrosRetornados).when(pageableCarroRepository).findAll(any(Pageable.class));

        //when
        Page<CarroModel> actual = carroService.buscarTodosSeminovos(pageable);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(2);
        assertThat(actual.getContent().stream().map(CarroModel::getPlaca).toList()).doesNotContain(carro3.getPlaca());
    }

    @Test
    void deveriaRetornarUmaListaContendoCarrosQueEstaoComQuilometragemMaiorOuIgualAoMaximoPermitidoParaLocacao() {
        //given
        Carro carro1 = CarroFactory.createEntity("FAS-5461");
        Carro carro2 = CarroFactory.createEntity("FAS-5462");
        carro2.setQuilometragem(CarroService.MAX_QUILOMETRAGEM_PARA_LOCACAO);
        Carro carro3 = CarroFactory.createEntity("FAS-5463");
        carro3.setQuilometragem(CarroService.MAX_QUILOMETRAGEM_PARA_LOCACAO.add(BigDecimal.ONE));
        Carro carro4 = CarroFactory.createEntity("FAS-5464");

        carros.addAll(Arrays.asList(carro1, carro2, carro3, carro4));

        Pageable pageable = PageRequest.of(0,10);
        Page<Carro> pageCarrosRetornados = new PageImpl<>(carros, pageable, 1);
        doReturn(pageCarrosRetornados).when(pageableCarroRepository).findAll(any(Pageable.class));

        //when
        Page<CarroModel> actual = carroService.buscarTodosSeminovos(pageable, CarroService.MAX_QUILOMETRAGEM_PARA_LOCACAO);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(2);
        assertThat(actual.getContent().stream().map(CarroModel::getPlaca).toList()).containsSequence(Arrays.asList(carro2.getPlaca(), carro3.getPlaca()));
    }

    @Test
    void deveriaRetornarUmaListaContendoCarrosQueEstaoComMaisDeDozeMesesDeFabricacaoOuComQuilometragemAcimaDoPermitidoParaLocacao() {
        //given
        int anoAtual = LocalDate.now().getYear();
        Carro carro1 = CarroFactory.createEntity("FAS-5461");
        carro1.setAno(anoAtual-2);
        Carro carro2 = CarroFactory.createEntity("FAS-5462");
        Carro carro3 = CarroFactory.createEntity("FAS-5463");
        carro3.setQuilometragem(CarroService.MAX_QUILOMETRAGEM_PARA_LOCACAO.add(BigDecimal.ONE));
        Carro carro4 = CarroFactory.createEntity("FAS-5464");
        carros.addAll(Arrays.asList(carro1, carro2, carro3, carro4));

        Pageable pageable = PageRequest.of(0,10);
        Page<Carro> pageCarrosRetornados = new PageImpl<>(carros, pageable, 1);
        doReturn(pageCarrosRetornados).when(pageableCarroRepository).findAll(any(Pageable.class));

        //when
        Page<CarroModel> actual = carroService.buscarTodosSeminovos(pageable);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(2);
        assertThat(actual.getContent().stream()
                        .map(CarroModel::getPlaca).toList())
                .containsSequence(Arrays.asList(carro1.getPlaca(), carro3.getPlaca()));
    }

    @Test
    void deveriaRetornarUmaListaVaziaCarrosPoisNenhumEstaComAnoInferiorAoDeFabricacaoOuComQuilometragemMaiorOuIgualPermitidaParaLocacao() {
        //given
        Carro carro1 = CarroFactory.createEntity("FAS-5461");
        Carro carro2 = CarroFactory.createEntity("FAS-5462");
        Carro carro3 = CarroFactory.createEntity("FAS-5463");
        Carro carro4 = CarroFactory.createEntity("FAS-5464");

        carros.addAll(Arrays.asList(carro1, carro2, carro3, carro4));

        Pageable pageable = PageRequest.of(0,10);
        Page<Carro> pageCarrosRetornados = new PageImpl<>(carros, pageable, 1);
        doReturn(pageCarrosRetornados).when(pageableCarroRepository).findAll(any(Pageable.class));

        //when
        Page<CarroModel> actual = carroService.buscarTodosSeminovos(pageable, CarroService.MAX_QUILOMETRAGEM_PARA_LOCACAO);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).isEmpty();
    }

    /*
    Dado que eu queira apagar um Carro do banco
    Quando eu tentar deletarCarroPorPlaca e exista o carro no banco
    Entao eu não devo apresentar uma exceção
     */
    @Test
    void deveriaConseguirDeletarUmCarroQueExistaNoBancoDeDadosSemExcecoes() {
        //given
        carroEntity.setPlaca("FAS-5461");
        doReturn(Optional.of(carroEntity)).when(carroRepository).findByPlaca(anyString());

        //when-then
        assertThatNoException().isThrownBy(()->carroService.deletarCarroPorPlaca(carroEntity.getPlaca()));

    }
    /*
        Dado que eu queira apagar um Carro do banco
        Quando eu tentar deletarCarroPorPlaca e exista o carro no banco
        Entao eu devo apresentar uma exceção
         */
    @Test
    void deveriaRetornarUmaExcecaoAoTentarDeletarUmCarroQueNaoExistaNoBancoDeDados() {
        //given
        String placa = "FAS-5461";
        doReturn(Optional.empty()).when(carroRepository).findByPlaca(anyString());

        //when-then
        assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(() -> carroService.deletarCarroPorPlaca(placa));
    }
}