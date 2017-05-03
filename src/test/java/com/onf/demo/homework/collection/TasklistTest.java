package com.onf.demo.homework.collection;


import com.onf.demo.homework.FixturedTest;
import com.onf.demo.homework.manager.TaskManager;
import com.onf.demo.homework.struct.Driver;
import com.onf.demo.homework.struct.Task;
import com.onf.demo.homework.util.ObjectGenerator;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests collection classes used by the {@link TaskManager} object.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class TasklistTest extends FixturedTest {
  @Test
  public void testConstruct() {
    final Driver driver = this.getSampleDataset().getGeneratedDrivers().iterator().next();
    final Tasklist tasklist = new Tasklist(driver);
    assertNotNull("tasklist driver getter should work", tasklist.getDriver());
    assertEquals("tasklist should start with 0 tasks", (Integer)0, tasklist.getTaskCount());
    assertEquals("tasklist should start with 0 known distance", (Double)0.0d, tasklist.getKnownDistance());
    assertEquals("tasklist should start with 0 load estimate", (Double)0.0d, tasklist.getLoadEstimate());
  }

  @Test
  public void testConstructWithTask() {
    final Driver driver = this.getSampleDataset().getGeneratedDrivers().iterator().next();
    final Task task = ObjectGenerator.generateTask();
    final Tasklist tasklist = new Tasklist(driver, Collections.singleton(task));
  }

  @Test
  public void testHashCode() {
    final Driver driver = this.getSampleDataset().getGeneratedDrivers().iterator().next();
    final Task task = ObjectGenerator.generateTask();
    final Tasklist tasklist = new Tasklist(driver, Collections.singleton(task));
    final Tasklist tasklist2 = new Tasklist(driver, Collections.singleton(task));
    assertEquals("tasklist.hashCode should equal driver.hashCode", tasklist.hashCode(), driver.hashCode());
    assertTrue("tasklist.equals should operate consistently", tasklist.equals(tasklist2));
    assertEquals("tasklist.hashCode should operate consistently", tasklist.hashCode(), tasklist2.hashCode());
  }
}
