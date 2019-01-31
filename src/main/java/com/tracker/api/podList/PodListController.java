package com.tracker.api.podList;

import com.tracker.api.ApiLocations;
import com.tracker.domain.pods.PodListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller representing methods for working with podList.
 */
@Api(tags = {"Pod list"})
@RestController
@RequestMapping(
        path = ApiLocations.POD_LIST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class PodListController {

    private final PodListService podListService;
    private final ReadPodDtoMapper readPodDtoMapper;

    /**
     * Initialize new instance of {@link PodListController}
     */
    @Autowired
    public PodListController(PodListService podListService, ReadPodDtoMapper readPodDtoMapper) {
        this.podListService = podListService;
        this.readPodDtoMapper = readPodDtoMapper;
    }

    /**
     * Gets the list of read model of pod
     *
     * @return ResponseEntity<List<ReadPodDto>> object
     */
    @ApiOperation("Get pod list by namespace")
    @RequestMapping(
            path = "",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ReadPodDto>> getPods(
            @ApiParam(value = "Namespace of pods in cluster") @RequestParam(name = "namespace", required = true) String namespace,
            @ApiParam(value = "Is need to apply filters") @RequestParam(name = "applyFilters", defaultValue = "false", required = true) boolean applyFilters
    ) {
        return ResponseEntity.ok(this.readPodDtoMapper.modelsToDtos(this.podListService.getPods(namespace, applyFilters)));
    }
}
