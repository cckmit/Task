package com.arturjarosz.task.finance.application.impl;

import com.arturjarosz.task.finance.application.ProjectFinancialDataService;
import com.arturjarosz.task.finance.infrastructure.FinancialDataRepository;
import com.arturjarosz.task.finance.infrastructure.ProjectFinancialDataRepository;
import com.arturjarosz.task.finance.model.FinancialData;
import com.arturjarosz.task.finance.model.ProjectFinancialData;
import com.arturjarosz.task.finance.query.impl.FinancialDataQueryServiceImpl;
import com.arturjarosz.task.project.application.ProjectValidator;
import com.arturjarosz.task.finance.model.dto.SupervisionRatesDto;
import com.arturjarosz.task.finance.model.dto.SupervisionVisitFinancialDto;
import com.arturjarosz.task.sharedkernel.annotations.ApplicationService;
import com.arturjarosz.task.sharedkernel.model.Money;

import java.math.BigDecimal;
import java.util.List;

@ApplicationService
public class ProjectFinancialDataServiceImpl implements ProjectFinancialDataService {

    private final ProjectFinancialDataRepository projectFinancialDataRepository;
    private final ProjectValidator projectValidator;
    private final FinancialDataQueryServiceImpl financialDataQueryService;
    private final FinancialDataRepository financialDataRepository;

    public ProjectFinancialDataServiceImpl(ProjectFinancialDataRepository projectFinancialDataRepository,
                                           ProjectValidator projectValidator,
                                           FinancialDataQueryServiceImpl financialDataQueryService,
                                           FinancialDataRepository financialDataRepository) {
        this.projectFinancialDataRepository = projectFinancialDataRepository;
        this.projectValidator = projectValidator;
        this.financialDataQueryService = financialDataQueryService;
        this.financialDataRepository = financialDataRepository;
    }

    @Override
    public ProjectFinancialData createProjectFinancialData(Long projectId) {
        this.projectValidator.validateProjectExistence(projectId);
        ProjectFinancialData projectFinancialData = new ProjectFinancialData(projectId);
        projectFinancialData.initiateProjectFinancialData();
        projectFinancialData = this.projectFinancialDataRepository.save(projectFinancialData);
        return projectFinancialData;
    }

    @Override
    public void recalculateSupervision(Long supervisionId, Long supervisionFinancialDataId) {
        SupervisionRatesDto supervisionRatesDto = this.financialDataQueryService.getSupervisionRatesDto(supervisionId);
        List<SupervisionVisitFinancialDto> supervisionVisitFinancialDtos = this.financialDataQueryService.getVisitsFinancialDto(
                supervisionId);
        FinancialData financialData = this.financialDataRepository.load(supervisionFinancialDataId);

        BigDecimal value = new BigDecimal(0);
        value = value.add(BigDecimal.valueOf(supervisionRatesDto.getBaseNetRate().doubleValue()));

        if (supervisionVisitFinancialDtos != null) {
            // Adding hours value and rate per visit
            for (SupervisionVisitFinancialDto supervisionVisit : supervisionVisitFinancialDtos) {
                if (supervisionVisit.isPayable()) {
                    BigDecimal hoursValue = BigDecimal.valueOf(
                            supervisionVisit.getHoursCount() * supervisionRatesDto.getHourlyNetRate().doubleValue());
                    value = value.add(hoursValue);
                    value = value.add(supervisionRatesDto.getVisitNetRate());
                }
            }
        }

        financialData.setValue(new Money(value));
        this.financialDataRepository.save(financialData);
    }

    public void recalculateProject(Long projectId) {
        // get project installments
        // get project costs
        // get project commissions
        // get supervisions
        // recalculate
    }
}