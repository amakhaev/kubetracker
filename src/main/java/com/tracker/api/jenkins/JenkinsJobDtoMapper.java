package com.tracker.api.jenkins;

import com.tracker.api.DtoDataMapper;
import com.tracker.domain.jenkins.JenkinsJobModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting Pod Dto to Pod model object(s) and vice versa.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JenkinsJobDtoMapper extends DtoDataMapper<JenkinsJobDto, JenkinsJobModel> {
}
