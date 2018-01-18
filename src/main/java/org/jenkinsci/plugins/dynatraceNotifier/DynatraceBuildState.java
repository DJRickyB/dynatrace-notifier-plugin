package org.jenkinsci.plugins.dynatraceNotifier;

/**
 * States communicated to the Dynatrace server.
 */
public enum DynatraceBuildState {
    SUCCESSFUL,
    FAILED,
    INPROGRESS,
}
