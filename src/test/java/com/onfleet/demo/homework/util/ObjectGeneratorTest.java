package com.onfleet.demo.homework.util;


import com.onfleet.demo.homework.FixturedTest;
import com.onfleet.demo.homework.struct.Driver;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Test the {@link ObjectGenerator}, which is used to build sample/mock
 * data for this demo.
 */
public class ObjectGeneratorTest extends FixturedTest {
  @Test
  public void testGenerateDriver() {
    final Driver newDriver = ObjectGenerator.generateDriver();
    assertNotNull("generated driver must not be null", newDriver);
    assertNotNull("generated driver should have a UUID", newDriver.getUuid());
    assertNull("generated driver without a name should report a null name", newDriver.getName());
  }

  @Test
  public void testGenerateDriverWithRandomName() {
    final SampleDataset dataset = this.getSampleDataset();
    final NameHelper nameHelper = dataset.getNames();
    final Driver newDriver = ObjectGenerator.generateDriver(nameHelper);
    assertNotNull("generated driver must not be null", newDriver);
    assertNotNull("generated driver should have a name", newDriver.getName());
    assertNotNull("generated driver should have a UUID", newDriver.getUuid());
  }

  @Test
  public void testGenerateLocation() {

  }

  @Test
  public void testGenerateTask() {

  }
}
