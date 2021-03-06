package com.tracker.api.jenkins;

import com.tracker.api.ApiLocations;
import com.tracker.domain.jenkins.JenkinsJobEnvironment;
import com.tracker.domain.jenkins.JenkinsJobTestSuite;
import com.tracker.domain.jenkins.JenkinsService;
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
 * REST API controller representing methods for working with settings.
 */
@Api(tags = {"Jenkins"})
@RestController
@RequestMapping(
        path = ApiLocations.JENKINS,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class JenkinsController {

    private final JenkinsService jenkinsService;
    private final JenkinsJobDtoMapper dtoMapper;

    /**
     * Initialize new instance of {@link JenkinsController}
     */
    @Autowired
    public JenkinsController(JenkinsService jenkinsService, JenkinsJobDtoMapper dtoMapper) {
        this.jenkinsService = jenkinsService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Gets the jenkins job model related to test result
     *
     * @return ResponseEntity<JenkinsJobDto> object
     */
    @ApiOperation("Get status of UI tests execution")
    @RequestMapping(
            path = "ui_test",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JenkinsJobDto> getUiTestJob(
            @ApiParam(value = "Environment where tests job where run") @RequestParam(name = "environment") JenkinsJobEnvironment environment,
            @ApiParam(value = "The suite of tests that were ran") @RequestParam(name = "suite") JenkinsJobTestSuite suite
    ) {
        return ResponseEntity.ok(this.dtoMapper.modelToDto(this.jenkinsService.getTestJobStatus(environment, suite)));
    }

    /**
     * Gets the list of jobs that built in current moment
     *
     * @return ResponseEntity<JenkinsJobDto> object
     */
    @ApiOperation("Gets the list of jobs that built in present")
    @RequestMapping(
            path = "active_builds",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JenkinsJobDto>> getActiveBuilds() {
        return ResponseEntity.ok(this.dtoMapper.modelsToDtos(this.jenkinsService.getActiveBuilds()));
    }

    /**
     * Gets the list of jobs that built in current moment
     *
     * @return ResponseEntity<JenkinsJobDto> object
     */
    @ApiOperation("Gets the list of last jobs that built")
    @RequestMapping(
            path = "completed_builds",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JenkinsJobDto>> getCompletedBuilds(
            @ApiParam(value = "The count of built that should be return") @RequestParam(name = "count", defaultValue = "10") int count
    ) {
        return ResponseEntity.ok(this.dtoMapper.modelsToDtos(this.jenkinsService.getLastCompletedBuilds(count)));
    }

}
