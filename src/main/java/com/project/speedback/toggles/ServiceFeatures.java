package com.project.speedback.toggles;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum ServiceFeatures implements Feature {
  @Label("Remove bookings")
  REMOVE_BOOKINGS;

  @Override
  public boolean isActive() {
    return FeatureContext.getFeatureManager().isActive(this);
  }
}
