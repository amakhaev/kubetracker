package com.tracker.api.podList;

import com.tracker.api.DtoDataMapper;
import com.tracker.domain.pods.PodModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting Pod Dto to Pod model object(s) and vice versa.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReadPodDtoMapper extends DtoDataMapper<ReadPodDto, PodModel> {
}
