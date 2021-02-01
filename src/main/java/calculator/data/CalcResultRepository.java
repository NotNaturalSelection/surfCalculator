package calculator.data;

import java.time.LocalDateTime;

import calculator.data.entities.CalcResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalcResultRepository extends CrudRepository<CalcResult, Integer> {
    Page<CalcResult> findAllByUsername(String username, Pageable pageable);

    Page<CalcResult> findAllByExpression(String expression, Pageable pageable);

    Page<CalcResult> findAllByDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
