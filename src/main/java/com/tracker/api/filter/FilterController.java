package com.tracker.api.filter;

import com.tracker.api.ApiLocations;
import com.tracker.domain.filter.FilterModel;
import com.tracker.domain.filter.FilterService;
import com.tracker.domain.filter.FilterType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller representing methods for working with filters.
 */
@Api(tags = {"Filters"})
@RestController
@RequestMapping(
        path = ApiLocations.FILTERS,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class FilterController {

    private final FilterService filterService;
    private final FilterDtoMapper filterDtoMapper;

    /**
     * Initialize new instance of {@link FilterController}
     */
    public FilterController(FilterService filterService, FilterDtoMapper filterDtoMapper) {
        this.filterService = filterService;
        this.filterDtoMapper = filterDtoMapper;
    }

    /**
     * Gets the list of read model of filter
     *
     * @return ResponseEntity<List<FilterDto>> object
     */
    @ApiOperation("Get the list of filters that available in application")
    @RequestMapping(
            path = "",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FilterDto>> getFilters() {
        return ResponseEntity.ok(this.filterDtoMapper.modelsToDtos(this.filterService.getFilters(FilterType.POD)));
    }

    /**
     * Creates new filter
     *
     * @return ResponseEntity<List<FilterDto>> object
     */
    @ApiOperation("Creates the filter that will be available for all pods")
    @RequestMapping(
            path = "",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<FilterDto>> createFilter(
            @ApiParam(value = "Filter in JSON", required = true) @RequestBody FilterDto filterDto
    ) {
        FilterModel filterModel = this.filterDtoMapper.dtoToModel(filterDto);
        filterModel.setFilterType(FilterType.POD);
        this.filterService.createOrUpdate(filterModel);
        return ResponseEntity.ok(this.filterDtoMapper.modelsToDtos(this.filterService.getFilters(FilterType.POD)));
    }

    /**
     * Deletes filter
     */
    @ApiOperation("Deletes the filter")
    @RequestMapping(
            path = "",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<List<FilterDto>> deleteFilter(
            @ApiParam(value = "Filter id", required = true) @RequestParam(name = "id", required = true) Integer id
    ) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        this.filterService.delete(id);
        return ResponseEntity.ok(this.filterDtoMapper.modelsToDtos(this.filterService.getFilters(FilterType.POD)));
    }

}
