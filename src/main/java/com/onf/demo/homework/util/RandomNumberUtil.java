package com.onf.demo.homework.util;


import org.jetbrains.annotations.NotNull;

import java.util.Random;


/**
 * Centralized access to a shared {@link Random} number generator.
 */
final class RandomNumberUtil {
  /**
   * Generate random numbers for use in name building.
   */
  final @NotNull static Random randomGenerator;

  static {
    // random number generator
    randomGenerator = new Random();  // in prod we would do something better here
  }

  /**
   * Retrieve an instance of the shared {@link Random} number generator.
   *
   * @return {@link Random} instance.
   */
  public static Random getRandomGenerator() {
    return randomGenerator;
  }
}
