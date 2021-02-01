package calculator.data;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import calculator.calc.Calculator;
import calculator.calc.InvalidExpressionException;
import calculator.data.entities.CalcResult;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private CalcResultRepository calcResultRepository;

    @Autowired
    private Calculator calculator;

    @Override
    public double calculateAndSave(String username, String expression) throws InvalidExpressionException {
        double calcResult = calculator.calculate(expression);

        CalcResult toSave = new CalcResult();
        toSave.setExpression(expression);
        toSave.setResult(calcResult);
        toSave.setUsername(username);
        toSave.setDate(LocalDateTime.now());
        calcResultRepository.save(toSave);

        return calcResult;
    }

    @Override
    public Page<CalcResult> getResultsByUsername(String username, Pageable pageable) {
        return calcResultRepository.findAllByUsername(username, pageable);
    }

    @Override
    public Page<CalcResult> getResultsByExpression(String expression, Pageable pageable) {
        return calcResultRepository.findAllByExpression(expression, pageable);
    }

    @Override
    public Page<CalcResult> getResultsBetweenDates(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        return calcResultRepository.findAllByDateBetween(fromDate, toDate, pageable);
    }
}
