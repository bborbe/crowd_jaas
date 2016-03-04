package de.benjaminborbe.jaas.crowd;

import java.io.Serializable;

public class CrowdConfig implements Serializable {

  private static final long serialVersionUID = 5359593994333317481L;

  public String getApplicationName() {
    return applicationName;
  }

  public String getApplicationPassword() {
    return applicationPassword;
  }

  public String getCrowdBaseUrl() {
    return crowdBaseUrl;
  }

  private final String crowdBaseUrl;

  private final String applicationName;

  private final String applicationPassword;

  public CrowdConfig(final String crowdBaseUrl, final String applicationName, final String applicationPassword) {
    this.crowdBaseUrl = crowdBaseUrl;
    this.applicationName = applicationName;
    this.applicationPassword = applicationPassword;
  }
}
