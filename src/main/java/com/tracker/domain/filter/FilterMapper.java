package com.tracker.domain.filter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Provides the mapper for filter of pod
 */
@Mapper
public interface FilterMapper {

    FilterMapper INSTANCE = Mappers.getMapper(FilterMapper.class);

    /**
     * Converts the entity to model.
     *
     * @param entity - the entity to convert.
     * @return the Model instance.
     */
    FilterModel entityToModel(FilterEntity entity);

    /**
     * Converts the model to entity.
     *
     * @param model - the model to convert.
     * @return the Entity instance.
     */
    FilterEntity modelToEntity(FilterModel model);

    /**
     * Converts the entities to models.
     *
     * @param entities - the entities to convert
     * @return the List<Model> instance
     */
    List<FilterModel> entitiesToModels(List<FilterEntity> entities);

    /**
     * Converts the models to entities.
     *
     * @param models - the models to convert
     * @return the List<Entity> instance
     */
    List<FilterEntity> modelsToEntities(List<FilterModel> models);
}
