package com.arturjarosz.task.supervision.application.mapper;

import com.arturjarosz.task.supervision.application.dto.SupervisionVisitDto;
import com.arturjarosz.task.supervision.model.SupervisionVisit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupervisionVisitDtoMapper {
    SupervisionVisitDtoMapper INSTANCE = Mappers.getMapper(SupervisionVisitDtoMapper.class);

    SupervisionVisitDto supervisionVisitToSupervisionVisionDto(SupervisionVisit supervisionVisit);

}
