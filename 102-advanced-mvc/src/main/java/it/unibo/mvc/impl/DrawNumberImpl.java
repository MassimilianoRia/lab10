package it.unibo.mvc.impl;

import java.util.Random;

import it.unibo.mvc.api.DrawNumber;

/**
 * Implementation of DrawNumber.
 */
public final class DrawNumberImpl implements DrawNumber {

    private int choice;
    private int remainingAttempts;
    private final Random random = new Random();
    private final Configuration config;

    /**
     * Constructor.
     *
     * @throws IllegalStateException if the configuration is not consistent
     */
    public DrawNumberImpl() {
        config = ConfigurationLoader.load();
        this.reset();
    }

    @Override
    public void reset() {
        this.remainingAttempts = this.config.getAttempts();
        this.choice = this.config.getMin() + random.nextInt(this.config.getMax() - this.config.getMin() + 1);
    }

    @Override
    public DrawResult attempt(final int n) {
        if (this.remainingAttempts <= 0) {
            return DrawResult.YOU_LOST;
        }
        if (n < this.config.getMin() || n > this.config.getMax()) {
            throw new IllegalArgumentException("The number is outside boundaries");
        }
        remainingAttempts--;
        if (n > this.choice) {
            return DrawResult.YOURS_HIGH;
        }
        if (n < this.choice) {
            return DrawResult.YOURS_LOW;
        }
        return DrawResult.YOU_WON;
    }

}
