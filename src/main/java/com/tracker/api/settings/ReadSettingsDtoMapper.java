package com.tracker.api.settings;

import com.tracker.api.DtoDataMapper;
import com.tracker.domain.settings.SettingsModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Common mapper for converting Dto object(s) to model object(s) and vice versa.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReadSettingsDtoMapper extends DtoDataMapper<ReadSettingsDto, SettingsModel> {

}
