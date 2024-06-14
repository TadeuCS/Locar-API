package br.com.locar.api.controllers;

import br.com.locar.api.models.CarroModel;
import br.com.locar.api.services.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping(path = "/carros")
@Tag(name = "Carros", description = "Endpoints relacioandos a carros")
public class CarroController {

    private final CarroService carroService;

    @Operation(summary = "Registrar um carro")
    @PostMapping
    public ResponseEntity<CarroModel> salvar(@Valid @RequestBody CarroModel carro){
        CarroModel carroModel = carroService.salvar(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body(carroModel);
    }

    @Operation(summary = "Atualizar um carro")
    @PatchMapping
    public ResponseEntity<CarroModel> atualizar(@Valid @RequestBody CarroModel carro){
        CarroModel carroModel = carroService.atualizar(carro);
        return ResponseEntity.ok(carroModel);
    }

    @Operation(summary = "Apagar um carro")
    @DeleteMapping
    public ResponseEntity<String> remover(@Valid @RequestParam("placa") String placa){
        carroService.deletarCarroPorPlaca(placa);
        return ResponseEntity.ok("Carro removido com sucesso!");
    }

    @Operation(summary = "Listar todos os carros")
    @GetMapping("/")
    public ResponseEntity<Page<CarroModel>> listarTodos(@PageableDefault(value = 10, page = 0) Pageable pageable){
        Page<CarroModel> carros = carroService.listarTodos(pageable);
        return ResponseEntity.ok(carros);
    }

    @Operation(summary = "Buscar carro por placa")
    @GetMapping
    public ResponseEntity<CarroModel> buscarPorPlaca(@RequestParam("placa") String placa){
        CarroModel carro = carroService.buscarPorPlaca(placa);
        return ResponseEntity.ok(carro);
    }

    @Operation(summary = "Listar carros seminovos")
    @GetMapping("seminovos")
    public ResponseEntity<Page<CarroModel>> listarTodosSeminovos(@PageableDefault(value = 10, page = 0) Pageable pageable){
        Page<CarroModel> carros = carroService.buscarTodosSeminovos(pageable);
        return ResponseEntity.ok(carros);
    }
}
