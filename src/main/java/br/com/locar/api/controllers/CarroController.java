package br.com.locar.api.controllers;

import br.com.locar.api.models.CarroModel;
import br.com.locar.api.services.CarroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("carros")
public class CarroController {

    private final CarroService carroService;

    @PostMapping
    public ResponseEntity<CarroModel> salvar(@Valid @RequestBody CarroModel carro){
        CarroModel carroModel = carroService.salvar(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body(carroModel);
    }

    @PatchMapping
    public ResponseEntity<CarroModel> atualizar(@Valid @RequestBody CarroModel carro){
        CarroModel carroModel = carroService.atualizar(carro);
        return ResponseEntity.ok(carroModel);
    }

    @DeleteMapping
    public ResponseEntity<String> remover(@Valid @RequestParam("placa") String placa){
        carroService.deletarCarroPorPlaca(placa);
        return ResponseEntity.ok("Carro removido com sucesso!");
    }

    @GetMapping("/")
    public ResponseEntity<Page<CarroModel>> listarTodos(@PageableDefault(value = 10, page = 0) Pageable pageable){
        Page<CarroModel> carros = carroService.listarTodos(pageable);
        return ResponseEntity.ok(carros);
    }

    @GetMapping
    public ResponseEntity<CarroModel> buscarPorPlaca(@RequestParam("placa") String placa){
        CarroModel carro = carroService.buscarPorPlaca(placa);
        return ResponseEntity.ok(carro);
    }

    @GetMapping("seminovos")
    public ResponseEntity<Page<CarroModel>> listarTodosSeminovos(@PageableDefault(value = 10, page = 0) Pageable pageable){
        Page<CarroModel> carros = carroService.buscarTodosSeminovos(pageable);
        return ResponseEntity.ok(carros);
    }
}
