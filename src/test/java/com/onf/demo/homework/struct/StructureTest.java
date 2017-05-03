package com.onf.demo.homework.struct;


import com.onf.demo.homework.FixturedTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 * Test the object structures used for the homework problem,
 * i.e. {@link Driver}, {@link Location} and {@link Task}.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class StructureTest extends FixturedTest {
  @Test
  public void testDriverObject() {
    final Driver sampleDriver = Driver.factory("John Doe");
    assertEquals("sampleDriver.uuid should match getter", sampleDriver.uuid, sampleDriver.getUuid());
    assertEquals("sampleDriver.name should match getter", sampleDriver.name, sampleDriver.getName());
  }

  @Test
  public void testDriverObjectFromJSON() throws IOException {
    final String jsonSample = "{\"uuid\": \"ed01d72e-02f8-4ba3-be22-d00fe19b3632\", \"name\": \"Bill Withers\"}";
    this.objectMapper().readerFor(Driver.class).readValue(jsonSample);
  }

  @Test
  public void testDriverObjectToString() throws IOException {
    final Driver subject = this.getSampleDataset().getGeneratedDrivers().iterator().next();
    assertEquals("driver.toString() should return a name when it has one",
                 subject.getName(),
                 subject.toString());

    final Driver subject2 = Driver.factory(null);
    assertEquals("driver.toString() should return a UUID when it has no name",
                 subject2.getUuid(),
                 subject2.toString());
  }

  @Test
  public void testDriverObjectComparison() throws IOException {
    final Driver subject = this.getSampleDataset().getGeneratedDrivers().iterator().next();
    final Driver sameSubject = this.getSampleDataset().getGeneratedDrivers().iterator().next();

    Iterator<Driver> driverIterator = this.getSampleDataset().getGeneratedDrivers().iterator();
    driverIterator.next();  // skip first
    final Driver differentSubject = driverIterator.next();

    assertEquals("identical drivers should equal each other",
                 subject,
                 sameSubject);

    assertNotEquals("different drivers should equal each other",
                    subject,
                    differentSubject);
  }

  @Test
  public void testLocationObject() {
    final Location sampleLocation = Location.factory("ONF HQ", new Geopoint(-122.408394, 37.783657));
    assertNotNull("sampleLocation.uuid should not be null", sampleLocation.getUuid());
    assertNotNull("sampleLocation.label should not be null when set", sampleLocation.getLabel());
    assertEquals("sampleLocation.geopoint should match getter", new Geopoint(-122.408394, 37.783657), sampleLocation.getGeopoint());
    assertEquals("sampleLocation.geopoint should match itself", sampleLocation.getGeopoint(), sampleLocation.getGeopoint());
    assertEquals("sampleLocation.latitude should match getter", sampleLocation.getGeopoint().getLatitude(), sampleLocation.getGeopoint().getLatitude());
    assertEquals("sampleLocation.longitude should match getter", sampleLocation.getGeopoint().getLongitude(), sampleLocation.getGeopoint().getLongitude());
  }

  @Test
  public void testLocationObjectFromJSON() throws IOException {
    final String locationSample = "{\"uuid\": \"9f9b2efd-2663-4c69-90e0-006b2dd3c631\", \"label\": \"ONF HQ\", \"geopoint\": {\"latitude\": -122.408394, \"longitude\": 37.783657}}";
    this.objectMapper().readerFor(Location.class).readValue(locationSample);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testLocationObjectNPEBoth() {
    //noinspection ConstantConditions
    final Location sampleLocation = Location.factory("ONF HQ", new Geopoint(null, null));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testLocationObjectNPELongitude() {
    //noinspection ConstantConditions
    final Location sampleLocation = Location.factory("ONF HQ", new Geopoint(null, 37.783657));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testLocationObjectNPELatitude() {
    //noinspection ConstantConditions
    final Location sampleLocation = Location.factory("ONF HQ", new Geopoint(-122.408394, null));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testNullGeopoint() {
    //noinspection ConstantConditions
    final Location sampleLocation = Location.factory("ONF HQ", null);
  }

  @Test
  public void testTaskObject() {
    final Driver sampleDriver = Driver.factory("John Doe");
    final Location sampleLocation = Location.factory("ONF HQ", new Geopoint(-122.408394, 37.783657));
    final Task sampleTask = Task.factory(sampleLocation);
    final Task sampleTask2 = Task.factory(sampleLocation);

    assertEquals("sampleTask.uuid should match getter", sampleTask.uuid, sampleTask.getUuid());
    assertNotEquals("sampleTask.uuid should be unique", sampleTask.uuid, sampleTask2.uuid);
    assertEquals("sampleTask.location should match getter", sampleTask.location, sampleTask.getLocation());
  }

  @Test
  public void testGeopointObject() {
    final Geopoint geopoint1 = new Geopoint(0.0, 1.0);
    final Geopoint geopoint2 = new Geopoint(0.0, 1.0);
    final Geopoint geopoint3 = new Geopoint(1.0, 0.0);

    assertEquals("geopoint.latitude should work sensibly", geopoint1.getLatitude(), geopoint2.getLatitude());
    assertEquals("geopoint.longitude should work sensibly", geopoint1.getLongitude(), geopoint2.getLongitude());
    assertEquals("identical geopoints should match", geopoint1, geopoint2);
    assertNotEquals("non-identical geopoints should not match", geopoint1, geopoint3);
    assertNotEquals("non-identical geopoint objects should match", geopoint1, 1);
    assertTrue("geopoint.toString should start with 'Point'", geopoint1.toString().startsWith("Point"));
    assertNotEquals("geopoint.hashCode should vary between non-identical points", geopoint1.hashCode(), geopoint3.hashCode());
    assertEquals("geopoint.hashCode should not vary between identical points", geopoint1.hashCode(), geopoint2.hashCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGeopointNullCheck() {
    @SuppressWarnings("ConstantConditions") final Geopoint geopoint1 = new Geopoint(null, null);
  }

  @Test
  public void testTaskObjectFromJSON() throws IOException {
    final String taskSample = "{\"uuid\": \"7e7b5490-f942-4781-9158-82ad47814045\", \"location\": {\"uuid\": \"7e7b5490-f942-4781-9158-82ad47814045\", \"label\": \"ONF HQ\", \"geopoint\": {\"latitude\": -122.408394, \"longitude\": 37.783657}}}";
    this.objectMapper().readerFor(Task.class).readValue(taskSample);
  }
}
