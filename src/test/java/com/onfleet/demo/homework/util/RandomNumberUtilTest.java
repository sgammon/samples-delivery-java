package com.onfleet.demo.homework.util;


import org.junit.Assert;
import org.junit.Test;

import java.util.Random;


/**
 * Test the {@link RandomNumberUtil} which exposes a {@link Random} instance for pseudo-random
 * number generation.
 */
public final class RandomNumberUtilTest {
  @Test
  public void testRandomNumberGenerator() {
    new RandomNumberUtil();
    final Random rando = RandomNumberUtil.getRandomGenerator();
    Integer random = rando.nextInt();
    Assert.assertNotNull("random number generator should not be null at runtime", rando);
    Assert.assertNotNull("random number generator should not product `null` random integers", random);
  }
}
