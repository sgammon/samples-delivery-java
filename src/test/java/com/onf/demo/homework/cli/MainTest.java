package com.onf.demo.homework.cli;


import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.IOException;


/**
 * Test the {@link Main} class, which backs the <pre>deliverytool</pre>, well, tool.
 */
public final class MainTest {
  @Test
  public void testConstructors() {
    new Main();
    new AppLogger();
    AppLogger._enableLogging();
    AppLogger._enableLogging(true);
    AppLogger.verbose("Test", "Test from a verbose log");
    AppLogger._disableLogging();
  }

  @Test(expected = IllegalStateException.class)
  public void testErrorLogging() {
    AppLogger._enableLogging();
    AppLogger.exit("Fatal error test", true);
    AppLogger._disableLogging();
  }

  @Test
  public void testMainWithNoArgs() throws ParseException, IOException {
    final String[] args = {};
    Main.main(args);
  }

  @Test(expected = ParseException.class)
  public void testMainWithNoInvalidArgs() throws ParseException, IOException {
    final String[] args = {"--invalidArgument"};
    Main.main(args);
  }
}
