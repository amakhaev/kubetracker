package com.tracker.domain.podFilter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Provides the mapper for filter of pod
 */
@Mapper
public interface PodFilterMapper {

    PodFilterMapper INSTANCE = Mappers.getMapper(PodFilterMapper.class);

    /**
     * Converts the entity to model.
     *
     * @param entity - the entity to convert.
     * @return the Model instance.
     */
    PodFilterModel entityToModel(PodFilterEntity entity);

    /**
     * Converts the model to entity.
     *
     * @param model - the model to convert.
     * @return the Entity instance.
     */
    PodFilterEntity modelToEntity(PodFilterModel model);

    /**
     * Converts the entities to models.
     *
     * @param entities - the entities to convert
     * @return the List<Model> instance
     */
    List<PodFilterModel> entitiesToModels(List<PodFilterEntity> entities);

    /**
     * Converts the models to entities.
     *
     * @param models - the models to convert
     * @return the List<Entity> instance
     */
    List<PodFilterEntity> modelsToEntities(List<PodFilterModel> models);
}
