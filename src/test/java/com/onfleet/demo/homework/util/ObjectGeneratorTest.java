package com.onfleet.demo.homework.util;


import com.onfleet.demo.homework.FixturedTest;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Location;
import com.onfleet.demo.homework.struct.Task;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
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
    final Location newLocation = ObjectGenerator.generateLocation();
    assertNotNull("generated location must not be null", newLocation);
    assertNotNull("generated location should have a non-null UUID", newLocation.getUuid());
    assertNotNull("generated location should have a non-null geopoint", newLocation.getGeopoint());
    assertNotEquals("generated location geopoint should have a nonzero latitude", newLocation.getGeopoint().getLatitude(), 0.0);
    assertNotEquals("generated location geopoint should have a nonzero longitude", newLocation.getGeopoint().getLongitude(), 0.0);
  }

  @Test
  public void testGenerateTask() {
    final Task newTask = ObjectGenerator.generateTask();
    assertNotNull("generated task must not be null", newTask);
    assertNotNull("generated task must have a non-null UUID", newTask.getUuid());
    assertNotNull("generated task must have a non-null location", newTask.getLocation());
  }
}
