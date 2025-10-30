package com.project.speedback.toggles;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum ServiceFeatures implements Feature {
  @Label("Testing toggle feature")
  TESTING_TOGGLE_FEATURE,

  @Label("Remove bookings")
  REMOVE_BOOKINGS;

  public boolean isActive() {
    return FeatureContext.getFeatureManager().isActive(this);
  }
}
