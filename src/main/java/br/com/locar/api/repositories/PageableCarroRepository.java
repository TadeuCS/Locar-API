package br.com.locar.api.repositories;

import br.com.locar.api.entities.Carro;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageableCarroRepository extends PagingAndSortingRepository<Carro, Integer> {
}
