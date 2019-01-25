package com.tracker.api.settings;

import com.tracker.api.ApiLocations;
import com.tracker.domain.settings.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API controller representing methods for working with settings.
 */
@Api(tags = {"Settings"})
@RestController
@RequestMapping(
        path = ApiLocations.SETTINGS,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class SettingsController {

    private final SettingService settingService;
    private final ReadSettingsDtoMapper dtoMapper;

    /**
     * Initialize new instance of {@link SettingsController}
     *
     * @param settingService - the setting service instance.
     */
    @Autowired
    public SettingsController(SettingService settingService, ReadSettingsDtoMapper readSettingsDtoMapper) {
        this.settingService = settingService;
        this.dtoMapper = readSettingsDtoMapper;
    }

    @ApiOperation("Get settings of service")
    @RequestMapping(
            path = "",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReadSettingsDto> getSettings() {
        return ResponseEntity.ok(this.dtoMapper.modelToDto(this.settingService.getSettings()));
    }

}
