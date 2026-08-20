package com.project.speedback.controller;

import com.project.speedback.dto.FeatureFlagDTO;
import com.project.speedback.toggles.ServiceFeatures;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Feature Flags", description = "Exposes current backend feature toggle states")
public class FeatureFlagController {

  @Operation(
      summary = "Get all feature flags",
      description =
          "Returns the current active/inactive state of all backend feature toggles (Togglz)."
              + " The frontend uses this to stay in sync with the backend.")
  @ApiResponse(
      responseCode = "200",
      description = "Feature flags retrieved successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureFlagDTO.class)))
  @GetMapping("/feature-flags")
  public List<FeatureFlagDTO> getFeatureFlags() {
    return Arrays.stream(ServiceFeatures.values())
        .map(feature -> new FeatureFlagDTO(feature.name(), feature.isActive()))
        .toList();
  }
}
