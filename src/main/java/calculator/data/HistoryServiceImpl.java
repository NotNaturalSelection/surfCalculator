package calculator.data;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import calculator.calc.CalculationService;
import calculator.calc.Calculator;
import calculator.calc.InvalidExpressionException;
import calculator.data.entities.CalcResult;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private CalcResultRepository resultRepository;

    @Autowired
    private CalculationService calculationService;

    @Override
    public double calculateAndSave(String username, String expression) throws InvalidExpressionException {
        CalcResult toSave = calculationService.calculate(expression);
        toSave.setUsername(username);
        resultRepository.save(toSave);
        return toSave.getResult();
    }

    @Override
    public Page<CalcResult> getResultsByUsername(String username, Pageable pageable) {
        return resultRepository.findAllByUsername(username, pageable);
    }

    @Override
    public Page<CalcResult> getResultsByExpression(String expression, Pageable pageable) {
        return resultRepository.findAllByExpression(expression, pageable);
    }

    @Override
    public Page<CalcResult> getResultsBetweenDates(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        return resultRepository.findAllByDateBetween(fromDate, toDate, pageable);
    }
}
