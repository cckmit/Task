package com.arturjarosz.task.finance.query;

import com.arturjarosz.task.finance.model.dto.SupervisionRatesDto;
import com.arturjarosz.task.finance.model.dto.SupervisionVisitFinancialDto;

import java.util.List;

public interface FinancialDataQueryService {
    /**
     *
     * @param supervisionId
     * @return
     */
    SupervisionRatesDto getSupervisionRatesDto(long supervisionId);

    List<SupervisionVisitFinancialDto> getVisitsFinancialDto(Long supervisionId);
}