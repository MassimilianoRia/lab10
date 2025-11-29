package it.unibo.mvc.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.mvc.api.DrawNumber;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawNumberViewObserver;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Application entry point for DrawNumber game.
 */
public final class DrawNumberApp implements DrawNumberViewObserver {
    private static final int MIN = 0;
    private static final int MAX = 100;
    private static final int ATTEMPTS = 10;

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * Constructor.
     *
     * @param configFile
     *            the configuration file path
     * @param views
     *            the views to attach
     */
    public DrawNumberApp(final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        this.model = new DrawNumberImpl(MIN, MAX, ATTEMPTS);
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (final IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    @SuppressFBWarnings(
        value = "DM_EXIT",
        justification = "Acceptable for exercising purposes."
    )
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

    /**
     * Application entry point.
     *
     * @param args
     *            ignored
     * @throws FileNotFoundException if the configuration file cannot be fetched
     */
    public static void main(final String... args) throws FileNotFoundException {
        new DrawNumberApp(new DrawNumberViewImpl());
    }

}
