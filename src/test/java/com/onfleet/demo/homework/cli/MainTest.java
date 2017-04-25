package com.onfleet.demo.homework.cli;


import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.IOException;


/**
 * Test the {@link Main} class, which backs the <pre>deliverytool</pre>, well, tool.
 */
public final class MainTest {
  @Test
  public void testConstructLogger() {
    new AppLogger();
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
