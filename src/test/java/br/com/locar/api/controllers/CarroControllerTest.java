package br.com.locar.api.controllers;

import br.com.locar.api.exceptions.NotFoundRegitreException;
import br.com.locar.api.factories.CarroFactory;
import br.com.locar.api.models.CarroModel;
import br.com.locar.api.services.CarroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarroController.class)
class CarroControllerTest {

    @MockBean
    private CarroService service;

    @Autowired
    private MockMvc mockMvc;

    private List<CarroModel> carros;

    private CarroModel carroModel;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(){
        objectMapper=new ObjectMapper();
    }

    @BeforeEach
    void setUpEach() {
        carros=new ArrayList<>();
        carroModel = CarroFactory.createModel();
    }

    @Test
    void deveriaSalvarComSucessoUmNovoCarro() throws Exception {
        carroModel.setPlaca("HIJ-7894");
        String requestBody = objectMapper.writeValueAsString(carroModel);
        carroModel.setId(1);
        when(service.salvar(any(CarroModel.class))).thenReturn(carroModel);

        this.mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void deveriaRetonarUmaExcecaoAoTentarSalvarUmNovoCarroPoisJaExisteCarroRegistradoComEssaPlaca() throws Exception {
        carroModel.setPlaca("HIJ-7894");
        String requestBody = objectMapper.writeValueAsString(carroModel);
        when(service.salvar(any(CarroModel.class))).thenThrow(EntityExistsException.class);

        this.mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaAtualizarComSucessoUmCarroCasoEsteJaEstejaCadastrado() throws Exception {
        carroModel.setPlaca("HIJ-7894");
        String requestBody = objectMapper.writeValueAsString(carroModel);
        carroModel.setId(1);
        when(service.atualizar(any(CarroModel.class))).thenReturn(carroModel);

        this.mockMvc.perform(patch("/carros")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void deveriaRetornarUmaExcecaoAoTentarAtualizarUmCarroCasoEsteNaoEstejaCadastrado() throws Exception {
        carroModel.setPlaca("HIJ-7894");
        String requestBody = objectMapper.writeValueAsString(carroModel);
        when(service.atualizar(any(CarroModel.class))).thenThrow(NotFoundRegitreException.class);

        this.mockMvc.perform(patch("/carros")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaConseguirDeletarUmCarroCadastradoComSucesso() throws Exception {
        doNothing().when(service).deletarCarroPorPlaca(anyString());

        this.mockMvc.perform(delete("/carros")
                        .queryParam("placa", "HIZ-1234"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Carro removido com sucesso!"));
    }

    @Test
    void deveriaRetornarUmaExcecaoAoTentarDeletarUmCarroNaoCadastrado() throws Exception {
        doThrow(NotFoundRegitreException.class).when(service).deletarCarroPorPlaca(anyString());

        this.mockMvc.perform(delete("/carros")
                        .queryParam("placa", "HIZ-1234"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaListarTodosOsCarrosCadastradosComSucesso() throws Exception {
        carroModel.setPlaca("XTP-7888");
        carros.add(carroModel);
        Pageable pageable = PageRequest.of(0,2);
        Page<CarroModel> pagesRetornadas = new PageImpl<>(carros, pageable, 1);

        when(service.listarTodos(any(Pageable.class))).thenReturn(pagesRetornadas);

        this.mockMvc.perform(get("/carros/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void deveriaListarVaziaDeCarrosCadastrados() throws Exception {
        Pageable pageable = PageRequest.of(0,2);
        Page<CarroModel> pagesRetornadas = new PageImpl<>(carros, pageable, 1);

        when(service.listarTodos(any(Pageable.class))).thenReturn(pagesRetornadas);

        this.mockMvc.perform(get("/carros/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", empty()));
    }

    @Test
    void deveriaRetornarOsDadosDoCarroCadastradoNoBancoComDeterminadaPlaca() throws Exception {
        when(service.buscarPorPlaca(anyString())).thenReturn(carroModel);

        this.mockMvc.perform(get("/carros").queryParam("placa", "XTP-7888"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void deveriaRetornarUmaExcecaoAoTentarBuscarOsDadosDoCarroCadastradoNoBancoComDeterminadaPlaca() throws Exception {
        when(service.buscarPorPlaca(anyString())).thenThrow(NotFoundRegitreException.class);

        this.mockMvc.perform(get("/carros").queryParam("placa", "XTP-7888"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaRetornarUmaListaContendoTodosOsCarrosSeminovos() throws Exception {
        carroModel.setPlaca("XTP-7888");
        carroModel.setQuilometragem(service.MAX_QUILOMETRAGEM_PARA_LOCACAO);
        carros.add(carroModel);
        Pageable pageable = PageRequest.of(0,2);
        Page<CarroModel> pagesRetornadas = new PageImpl<>(carros, pageable, 1);

        when(service.listarTodos(any(Pageable.class))).thenReturn(pagesRetornadas);

        this.mockMvc.perform(get("/carros/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void deveriaRetornarUmaListaVaziaDeCarrosSeminovos() throws Exception {
        Pageable pageable = PageRequest.of(0,2);
        Page<CarroModel> pagesRetornadas = new PageImpl<>(carros, pageable, 1);

        when(service.listarTodos(any(Pageable.class))).thenReturn(pagesRetornadas);

        this.mockMvc.perform(get("/carros/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", empty()));
    }
}