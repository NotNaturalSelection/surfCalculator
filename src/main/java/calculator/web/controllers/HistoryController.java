package calculator.web.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import calculator.data.HistoryService;
import calculator.data.entities.CalcResult;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/{username}")
    public Page<CalcResult> getByUsername(
            @PathVariable(name = "username")
                    String username,
            @PageableDefault
                    Pageable pageable
    ) {
        return historyService.getResultsByUsername(username, pageable);
    }

    @PostMapping("/expression")
    public Page<CalcResult> getByExpression(
            @RequestParam
                    String expression,
            @PageableDefault
                    Pageable pageable
    ) {
        return historyService.getResultsByExpression(expression, pageable);
    }

    @GetMapping("/betweenDates")
    public Page<CalcResult> getBetweenDates(
            @RequestParam(name = "fromDate")
                    LocalDateTime fromDate,
            @RequestParam(name = "toDate")
                    LocalDateTime toDate,
            @PageableDefault
                    Pageable pageable
    ) {
        return historyService.getResultsBetweenDates(fromDate, toDate, pageable);
    }

}
