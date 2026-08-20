package com.project.speedback.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.project.speedback.exception.GlobalExceptionHandler;
import com.project.speedback.toggles.ServiceFeatures;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

@WebMvcTest(FeatureFlagController.class)
@Import(GlobalExceptionHandler.class)
class FeatureFlagControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldReturnAllFeatureFlagsWithCorrectActiveState() throws Exception {
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    org.mockito.Mockito.when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS))
        .thenReturn(true);

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      mockMvc
          .perform(get("/api/feature-flags"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$.length()").value(ServiceFeatures.values().length))
          .andExpect(jsonPath("$[0].name").value("REMOVE_BOOKINGS"))
          .andExpect(jsonPath("$[0].active").value(true));
    }
  }

  @Test
  void shouldReturnAllFlagsInactiveWhenAllDisabled() throws Exception {
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    org.mockito.Mockito.when(featureManager.isActive(org.mockito.Mockito.any())).thenReturn(false);

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      mockMvc
          .perform(get("/api/feature-flags"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(ServiceFeatures.values().length))
          .andExpect(
              jsonPath(
                  "$[*].active", org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is(false))));
    }
  }
}
