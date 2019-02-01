package com.tracker.api.jenkins;

import com.tracker.api.ApiLocations;
import com.tracker.domain.jenkins.JenkinsJobEnvironment;
import com.tracker.domain.jenkins.JenkinsJobTestType;
import com.tracker.domain.jenkins.JenkinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @ApiParam(value = "The type of job that was ran") @RequestParam(name = "jobType") JenkinsJobTestType testType
    ) {
        return ResponseEntity.ok(this.dtoMapper.modelToDto(this.jenkinsService.getTestJobStatus(environment, testType)));
    }

}
