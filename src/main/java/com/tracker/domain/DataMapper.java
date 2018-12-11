package com.tracker.domain;

import org.mapstruct.Mapper;

/**
 * Provides common data mapper.
 */
@Mapper
public interface DataMapper<Entity, Model> {

    /**
     * Converts the entity to model.
     *
     * @param entity - the entity to convert.
     * @return the Model instance.
     */
    // Model entityToModel(Entity entity);

    /**
     * Converts the model to entity.
     *
     * @param model - the model to convert.
     * @return the Entity instance.
     */
    // Entity modelToEntity(Model model);

    /**
     * Converts the entities to models.
     *
     * @param entities - the entities to convert
     * @return the List<Model> instance
     */
    // List<Model> entitiesToModels(List<Entity> entities);

    /**
     * Converts the models to entities.
     *
     * @param models - the models to convert
     * @return the List<Entity> instance
     */
    // List<Entity> modelsToEntities(List<Model> models);
}
