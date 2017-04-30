package com.onfleet.demo.homework.manager;


import com.onfleet.demo.homework.FixturedTest;
import com.onfleet.demo.homework.TaskAssigner;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import com.onfleet.demo.homework.util.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;


/**
 * Test the {@link TaskManager} and {@link BlindTaskManager} object.
 */
@SuppressWarnings("unused")
public final class TaskManagerTest extends FixturedTest {
  @Test
  public void testConstruct() {
    new TaskManager(this.getSampleDataset().getGeneratedDrivers());
  }

  @Test
  public void testConstructViaPublicAPI() {
    final TaskAssigner manager = TaskManager.setupWithDataset(this.getSampleDataset());
    assertNotNull("TaskManager should not be null when factoried", manager);
  }

  @Test
  public void testAssignTaskToPreviouslyUnknownDriver() {
    final TaskAssigner manager = new TaskManager(this.getSampleDataset().getGeneratedDrivers());
    final Driver generatedDriver = ObjectGenerator.generateDriver();
    final Task generatedTask = ObjectGenerator.generateTask();
    manager.assignToDriver(generatedDriver, generatedTask);
  }

  @Test
  public void testAssignMultipleTasks() {
    final TaskAssigner manager = new TaskManager(this.getSampleDataset().getGeneratedDrivers());
    final Driver generatedDriver = ObjectGenerator.generateDriver();
    final Task generatedTask = ObjectGenerator.generateTask();
    final Task generatedTask2 = ObjectGenerator.generateTask();
    final Task generatedTask3 = ObjectGenerator.generateTask();

    final Collection<Task> tasklist = new ArrayList<>(3);
    tasklist.add(generatedTask);
    tasklist.add(generatedTask2);
    tasklist.add(generatedTask3);
    manager.assignToDriver(generatedDriver, tasklist);
  }

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
}
