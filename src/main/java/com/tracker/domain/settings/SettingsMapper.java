package com.tracker.domain.settings;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Provides the settings mapper
 */
@Mapper
public interface SettingsMapper {

    SettingsMapper INSTANCE = Mappers.getMapper(SettingsMapper.class);

    /**
     * Converts the entity to model.
     *
     * @param entity - the entity to convert.
     * @return the Model instance.
     */
    SettingModel entityToModel(SettingsEntity entity);

    /**
     * Converts the model to entity.
     *
     * @param model - the model to convert.
     * @return the Entity instance.
     */
    SettingsEntity modelToEntity(SettingModel model);

}
