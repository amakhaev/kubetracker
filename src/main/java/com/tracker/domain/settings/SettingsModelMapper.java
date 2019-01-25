package com.tracker.domain.settings;

import com.tracker.domain.DataMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Provides the settings mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingsModelMapper extends DataMapper<SettingsEntity, SettingsModel> {
}
