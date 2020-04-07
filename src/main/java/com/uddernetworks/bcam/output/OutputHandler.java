package com.uddernetworks.bcam.output;

/**
 * Handles light output. This could be typing, as with the default implementation, or some other arbitrary task.
 */
public interface OutputHandler {

    /**
     * Called when the light state changes. This means that it will never be called with two identical states in a row.
     *
     * @param lightsOn The state of the light, if it is on.
     */
    void handleToggle(boolean lightsOn);
}
