package com.tracker.api.filter;

import com.tracker.api.DtoDataMapper;
import com.tracker.domain.filter.FilterModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting Pod Dto to Pod model object(s) and vice versa.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilterDtoMapper extends DtoDataMapper<FilterDto, FilterModel> {
}
