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

    // ReadSettingsDtoMapper INSTANCE = Mappers.getMapper(ReadSettingsDtoMapper.class);

    /**
     * Converts the model to dto.
     *
     * @param entity - the entity to convert.
     * @return the Model instance.
     */
    // ReadSettingsDto modelToReadDto(SettingsModel entity);

    /**
     * Converts the dto to model.
     *
     * @param model - the model to convert.
     * @return the Entity instance.
     */
    // SettingsModel dtoToMode(ReadSettingsDto model);

}
