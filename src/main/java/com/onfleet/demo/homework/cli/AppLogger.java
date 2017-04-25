package com.onfleet.demo.homework.cli;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;


/**
 * Logging facade.
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "WeakerAccess"})
public final class AppLogger {
  // -- internals -- //
  /**
   * App-wide logger.
   */
  private static final Logger logging = Logger.getAnonymousLogger();

  /**
   * Logging status.
   */
  private static boolean _loggingEnabled = false;

  /**
   * Verbose logging status.
   */
  private static boolean _verboseEnabled = false;

  // -- disable/enable logging -- //
  /**
   * Disable logging app-wide.
   */
  public static void _disableLogging() {
    _loggingEnabled = false;
    _verboseEnabled = false;
  }

  /**
   * Enable logging app-wide.
   */
  public static void _enableLogging() {
    _enableLogging(false);
  }

  /**
   * Enable logging app-wide, optionally enabling verbose messages.
   */
  public static void _enableLogging(final boolean verbose) {
    _loggingEnabled = true;
    _verboseEnabled = verbose;
  }

  // -- public API -- //
  /**
   * Say something from some part of the app, as a log statement.
   *
   * @param phase Where we are during processing.
   * @param message What we want to say.
   */
  public static void say(final @NotNull String phase,
                         final @NotNull String message) {
    if (_loggingEnabled)
      logging.info("[" + phase + "]: " + message);
  }

  /**
   * Say something verbose from some part of the app, as a log statement.
   *
   * @param phase Where we are during processing.
   * @param message What we want to say.
   */
  public static void verbose(final @NotNull String phase,
                             final @NotNull String message) {
    if (_loggingEnabled && _verboseEnabled)
      logging.info("[" + phase + "]: " + message);
  }

  /**
   * Called when a fatal error has occurred. Prints a message and exits
   * with code <pre>1</pre>.
   *
   * @param message Message to exit with.
   */
  public static void exit(final String message) {
    logging.severe(message);
    logging.severe("Exiting.");
    System.exit(1);
  }
}
