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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CarroServiceTest {

    private CarroService carroService;

    private CarroRepository carroRepository;

    private PageableCarroRepository pageableCarroRepository;

    private CarroMapper carroMapper;
    private List<Carro> carros;

    @BeforeEach
    void setUpEach() {
        System.out.println("Antes de cada Execução");
        carroRepository = Mockito.mock(CarroRepository.class);
        pageableCarroRepository = Mockito.mock(PageableCarroRepository.class);
        carroMapper = new CarroMapper();
        carroService = new CarroService(carroRepository,pageableCarroRepository, carroMapper);
        carros=new ArrayList<>();
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
        //given
        CarroModel carroModel = CarroFactory.createModel();
        Carro carroSalvo = CarroFactory.createEntity();
        Mockito.when(carroRepository.findByPlaca(anyString())).thenReturn(Optional.empty());
        Mockito.when(carroRepository.save(any(Carro.class))).thenReturn(carroSalvo);

        //when
        CarroModel carroRetornado = carroService.salvar(carroModel);

        //then
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
        carroService = Mockito.spy(this.carroService);
        CarroModel carroModel = CarroFactory.createModel();
        carroModel.setPlaca("XPTO");
        doThrow(NotFoundRegitreException.class).when(carroService).buscarPorPlaca(anyString());

        Assertions.assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(() -> carroService.atualizar(carroModel));
    }

    @Test
//    @Timeout(value = 2, unit = SECONDS)
    void deveriaRetornarUmaListaContendoUmCarro() {
        Carro carro = CarroFactory.createEntity();
        carros.add(carro);
        Pageable pageable = PageRequest.of(0,2);
        Page<Carro> pagesRetornadas = new PageImpl<>(carros, pageable, 1);
        when(pageableCarroRepository.findAll(any(Pageable.class))).thenReturn(pagesRetornadas);
        Assertions.assertThat(carroService.listarTodos(pageable)).isNotEmpty();
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
        Carro expected = CarroFactory.createEntity(placa);
        Mockito.when(carroRepository.findByPlaca(placa)).thenReturn(Optional.of(expected));
        //when
        CarroModel actual = carroService.buscarPorPlaca(placa);

        //then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getPlaca()).isEqualTo(expected.getPlaca());
    }

    @Test
    void deveriaRetornarUmaExcecaoQueContenhaPlacaInformadaAoTentarEncontrarUmCarroComEstaPlaca() {
        //given
        String placa = "HBJ-5579";
        Mockito.when(carroRepository.findByPlaca(placa)).thenReturn(Optional.empty());

        //then - when
        Assertions.assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(()-> carroService.buscarPorPlaca(placa));
    }

    /*
        Dado que seja buscando os caracterizados como seminovos,
        Quando eu pesquisar no banco de dados
        Então eu quero que retorne um lista contendo os carros que tem mais de 12 meses de fabricação,
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
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getContent()).hasSize(2);
        Assertions.assertThat(actual.getContent().stream().map(CarroModel::getPlaca).toList()).doesNotContain(carro3.getPlaca());
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
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getContent()).hasSize(2);
        Assertions.assertThat(actual.getContent().stream().map(CarroModel::getPlaca).toList()).containsSequence(Arrays.asList(carro2.getPlaca(), carro3.getPlaca()));
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
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getContent()).hasSize(2);
        Assertions.assertThat(actual.getContent().stream()
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
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getContent()).isEmpty();
    }

    /*
    Dado que eu queira apagar um Carro do banco
    Quando eu tentar deletarCarroPorPlaca e exista o carro no banco
    Entao eu não devo apresentar uma exceção
     */
    @Test
    void deveriaConseguirDeletarUmCarroQueExistaNoBancoDeDadosSemExcecoes() {
        //given
        Carro carro = CarroFactory.createEntity("FAS-5461");
        doReturn(Optional.of(carro)).when(carroRepository).findByPlaca(anyString());

        //when-then
        Assertions.assertThatNoException().isThrownBy(()->carroService.deletarCarroPorPlaca(carro.getPlaca()));

    }
    /*
        Dado que eu queira apagar um Carro do banco
        Quando eu tentar deletarCarroPorPlaca e exista o carro no banco
        Entao eu devo apresentar uma exceção
         */
    @Test
    void deveriaRetornarUmaExcecaoAoTentarDeletarUmCarroQueNaoExistaNoBancoDeDados() {
        //given
        Carro carro = CarroFactory.createEntity("FAS-5461");
        doReturn(Optional.empty()).when(carroRepository).findByPlaca(anyString());

        //when-then
        Assertions.assertThatExceptionOfType(NotFoundRegitreException.class)
                .isThrownBy(()->carroService.deletarCarroPorPlaca(carro.getPlaca()));
    }
}