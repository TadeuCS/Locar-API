package br.com.locar.api.repositories;

import br.com.locar.api.entities.Carro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PageableCarroRepository extends PagingAndSortingRepository<Carro, Integer> {
        Page<Carro> findByAnoBeforeOrQuilometragemGreaterThanEqual(Pageable pageable, Integer ano, BigDecimal quilometragem);
}
