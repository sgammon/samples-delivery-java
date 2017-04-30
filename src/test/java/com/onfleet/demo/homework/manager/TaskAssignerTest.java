package com.onfleet.demo.homework.manager;


import com.onfleet.demo.homework.FixturedTest;
import com.onfleet.demo.homework.TaskAssigner;
import com.onfleet.demo.homework.collection.Tasklist;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import com.onfleet.demo.homework.util.ObjectGenerator;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Supplies test methods for classes that comply with {@link com.onfleet.demo.homework.TaskAssigner}.
 */
abstract class TaskAssignerTest extends FixturedTest {
  /**
   * Test that resolving the lowest-cost-assignment happens in an entirely consistent
   * way - i.e. that the same result is returned, given identical state, for the same
   * candidate task.
   *
   * @param assigner Assigning manager implementation.
   */
  void testResolutionForConsistency(final TaskAssigner assigner) {
    final Task generatedTask = ObjectGenerator.generateTask();
    final Driver lowestCost1 = assigner.resolveLowestCostAssignment(generatedTask);
    final Driver lowestCost2 = assigner.resolveLowestCostAssignment(generatedTask);

    Assert.assertEquals("lowest-cost-assignment algorithm for implementation '"
                            + assigner.getClass().getSimpleName()
                            + "' needs to be consistent", lowestCost1, lowestCost2);
  }

  /**
   * Test assigning an arbitrary task to a specific {@link Driver}.
   *
   * @param assigner Assigning manager implementation.
   */
  void testAssignTaskToPreviouslyUnknownDriver(final TaskAssigner assigner) {
    final Driver generatedDriver = ObjectGenerator.generateDriver();
    final Task generatedTask = ObjectGenerator.generateTask();
    assigner.assignToDriver(generatedDriver, generatedTask);
    final Tasklist tasklistForDriver = assigner.tasklistForDriver(generatedDriver);

    Assert.assertNotNull("tasklist for known driver should not return null",
                         tasklistForDriver);
    Assert.assertTrue("tasklist for known driver should contain task just assigned to it",
                      tasklistForDriver.getAssignedTasks().contains(generatedTask));

    final int assignedTasksBefore = tasklistForDriver.getTaskCount();
    final double knownDistanceBefore = tasklistForDriver.getKnownDistance();
    final double loadEstimateBefore = tasklistForDriver.getLoadEstimate();

    final Task generatedTask2 = ObjectGenerator.generateTask();
    assigner.assignToDriver(generatedDriver, generatedTask2);

    final Tasklist tasklistForDriver2 = assigner.tasklistForDriver(generatedDriver);

    Assert.assertNotNull("tasklist for known driver should not return null after second op",
                         tasklistForDriver2);
    Assert.assertTrue("tasklist for known driver should contain second task just assigned to it",
                      tasklistForDriver2.getAssignedTasks().contains(generatedTask2));
    Assert.assertTrue("known distance should change when a new task is added",
                      !tasklistForDriver2.getKnownDistance().equals(knownDistanceBefore));
    Assert.assertTrue("task count should change when a new task is added",
                      !tasklistForDriver2.getTaskCount().equals(assignedTasksBefore));
    Assert.assertTrue("load estimate should change when a new task is added",
                      !tasklistForDriver2.getLoadEstimate().equals(loadEstimateBefore));
  }

  /**
   * Test assigning multiple tasks at once.
   *
   * @param assigner Assigning manager implementation.
   */
  void testAssignMultipleTasks(final TaskAssigner assigner) {
    final Driver generatedDriver = ObjectGenerator.generateDriver();
    final Task generatedTask = ObjectGenerator.generateTask();
    final Task generatedTask2 = ObjectGenerator.generateTask();
    final Task generatedTask3 = ObjectGenerator.generateTask();

    final Collection<Task> tasklist = new ArrayList<>(3);
    tasklist.add(generatedTask);
    tasklist.add(generatedTask2);
    tasklist.add(generatedTask3);
    assigner.assignToDriver(generatedDriver, tasklist);
  }
}
