package com.fallenreaper.createutilities.utils;

public class InteractionHandler {
    public enum State {
        RUNNING,
        VALID,
        PAUSED,
        NONE;

        public boolean isRunning() {
            return this == RUNNING;
        }

        public boolean isPaused() {
            return this == PAUSED;
        }

        public boolean isValid() {
            return this == VALID;
        }

        public boolean isInvalid() {
            return this == NONE;
        }
    }
}
