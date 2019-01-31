package com.tracker.api;

import java.util.List;

/**
 * Common mapper for converting Dto object(s) to model object(s) and vice versa.
 */
public interface DtoDataMapper<DtoType, ModelType> {
    /**
     * Converts Dto object to model object.
     *
     * @param Dto - The Dto object.
     * @return The model object.
     */
    ModelType dtoToModel(DtoType Dto);

    /**
     * Converts model object to Dto object.
     *
     * @param model - The model object.
     * @return The Dto object.
     */
    DtoType modelToDto(ModelType model);

    /**
     * Converts a list of model objects to a list of Dto objects.
     *
     * @param models - The list of model objects.
     * @return The of list of Dto objects.
     */
    List<DtoType> modelsToDtos(List<ModelType> models);

    /**
     * Converts a list of Dto objects to a list of model objects.
     *
     * @param Dtos - The list of Dto objects.
     * @return The of list of model objects.
     */
    List<ModelType> dtosToModels(List<DtoType> Dtos);
}
