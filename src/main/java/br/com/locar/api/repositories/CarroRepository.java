package br.com.locar.api.repositories;

import br.com.locar.api.entities.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Integer> {
    Optional<Carro> findByPlaca(String placa);
}
