package com.tracker.api.settings;

import com.tracker.api.ApiLocations;
import com.tracker.domain.settings.SettingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final ReadSettingsDtoMapper readDtoMapper;
    private final WriteSettingsDtoMapper writeDtoMapper;

    /**
     * Initialize new instance of {@link SettingsController}
     */
    @Autowired
    public SettingsController(SettingService settingService,
                              ReadSettingsDtoMapper readDtoMapper,
                              WriteSettingsDtoMapper writeDtoMapper) {
        this.settingService = settingService;
        this.readDtoMapper = readDtoMapper;
        this.writeDtoMapper = writeDtoMapper;
    }

    /**
     * Gets the read model of settings
     *
     * @return ResponseEntity<ReadSettingsDto> object
     */
    @ApiOperation("Get settings of service")
    @RequestMapping(
            path = "",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReadSettingsDto> getSettings() {
        return ResponseEntity.ok(this.readDtoMapper.modelToDto(this.settingService.getSettings()));
    }

    /**
     * Updates the settings
     *
     * @return update settings DTO
     */
    @ApiOperation("Update service settings")
    @ApiResponses({
            @ApiResponse(code = 202, message = "The settings successfully updated"),
            @ApiResponse(code = 400, message = "Malformed request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(
            path = "",
            method = RequestMethod.PUT
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ReadSettingsDto> updateSettings(
            @ApiParam(value = "Settings in JSON", required = true) @RequestBody WriteSettingsDto settingsDto
    ) {
        return ResponseEntity
                .accepted()
                .body(
                        this.readDtoMapper.modelToDto(
                                this.settingService.updateSettings(this.writeDtoMapper.dtoToModel(settingsDto))
                        )
                );
    }
}
