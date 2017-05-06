package com.onf.demo.homework.manager;


import com.onf.demo.homework.struct.Driver;
import com.onf.demo.homework.struct.Task;
import com.onf.demo.homework.util.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;


/**
 * Applies {@link TaskAssignerTest}-based logic to {@link BlindTaskManager}.
 */
@SuppressWarnings("unused")
public final class BlindTaskManagerTest extends TaskAssignerTest {
  @Test
  public void testBlindTaskManagerConstructor() {
    final TaskManager manager = TaskManager.setupWithDataset(this.getSampleDataset());
    final BlindTaskManager blind = new BlindTaskManager(manager);
  }

  @Test
  public void testBlindTaskManagerAssignTasks() {
    final TaskManager manager = TaskManager.setupWithDataset(this.getSampleDataset());
    final BlindTaskManager blind = new BlindTaskManager(manager);
    final Task task = ObjectGenerator.generateTask();
    final Driver lowestCost = blind.resolveLowestCostAssignment(task);
    Assert.assertNotNull("lowest cost driver assignment from blind manager should not be null", lowestCost);
    blind.assignToDriver(lowestCost, task);
  }

  @Test
  public void testResolutionConsistency() {
    this.testResolutionForConsistency(new BlindTaskManager(
        TaskManager.setupWithDataset(this.getSampleDataset())));
  }

  @Test
  public void testAssignTaskToPreviouslyUnknownDriver() {
    this.testAssignTaskToPreviouslyUnknownDriver(new BlindTaskManager(
        TaskManager.setupWithDataset(this.getSampleDataset())));
  }

  @Test
  public void testAssignMultipleTasks() {
    this.testAssignMultipleTasks(new BlindTaskManager(
        TaskManager.setupWithDataset(this.getSampleDataset())));
  }
}
