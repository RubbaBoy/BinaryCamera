package com.uddernetworks.bcam.output;

/**
 * An object to store the duration of how long the light was on. This may contain more information in the future, so it
 * is not just a long.
 */
public class ToggleState {
    private final long timeOn;

    public ToggleState(long timeOn) {
        this.timeOn = timeOn;
    }

    /**
     * Gets the time the light was on in milliseconds.
     *
     * @return The time in milliseconds
     */
    public long getTimeOn() {
        return timeOn;
    }
}
