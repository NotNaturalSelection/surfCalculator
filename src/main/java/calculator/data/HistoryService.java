package calculator.data;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import calculator.calc.InvalidExpressionException;
import calculator.data.entities.CalcResult;

public interface HistoryService {
    double calculateAndSave(String username, String expression)
            throws InvalidExpressionException;

    Page<CalcResult> getResultsByUsername(String username, Pageable pageable);

    Page<CalcResult> getResultsByExpression(String expression, Pageable pageable);

    Page<CalcResult> getResultsBetweenDates(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
