package com.arturjarosz.task.contract.application.mapper;

import com.arturjarosz.task.contract.application.dto.ContractDto;
import com.arturjarosz.task.contract.model.Contract;
import com.arturjarosz.task.project.application.dto.ProjectCreateDto;
import com.arturjarosz.task.sharedkernel.model.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContractDtoMapper {
    ContractDtoMapper INSTANCE = Mappers.getMapper(ContractDtoMapper.class);

    @Mapping(source = "projectCreateDto.offerValue", target = "offerValue")
    @Mapping(source = "projectCreateDto.deadline", target = "deadline")
    ContractDto projectDtoToContractDto(ProjectCreateDto projectCreateDto);

    @Mapping(source = "signingDate", target = "signingDate")
    @Mapping(source = "deadline", target = "deadline")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "offerValue", source = "offerValue", qualifiedByName = "moneyToDouble")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    ContractDto contractToContractDto(Contract contract);

    @Named("moneyToDouble")
    default Double moneyToDouble(Money value){
        return value.getValue().doubleValue();
    }
}
