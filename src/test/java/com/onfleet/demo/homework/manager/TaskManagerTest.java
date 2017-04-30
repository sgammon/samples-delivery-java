package com.onfleet.demo.homework.manager;


import com.onfleet.demo.homework.TaskAssigner;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * Test the {@link TaskManager} and {@link BlindTaskManager} object.
 */
@SuppressWarnings("unused")
public final class TaskManagerTest extends TaskAssignerTest {
  @Test
  public void testConstructViaPublicAPI() {
    final TaskAssigner manager = TaskManager.setupWithDataset(this.getSampleDataset());
    assertNotNull("TaskManager should not be null when factoried", manager);
  }

  @Test
  public void testResolutionConsistency() {
    this.testResolutionForConsistency(TaskManager.setupWithDataset(this.getSampleDataset()));
  }

  @Test
  public void testAssignTaskToPreviouslyUnknownDriver() {
    this.testAssignTaskToPreviouslyUnknownDriver(TaskManager.setupWithDataset(this.getSampleDataset()));
  }

  @Test
  public void testAssignMultipleTasks() {
    this.testAssignMultipleTasks(TaskManager.setupWithDataset(this.getSampleDataset()));
  }
}
