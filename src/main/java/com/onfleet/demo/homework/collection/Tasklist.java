package com.onfleet.demo.homework.collection;


import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Specifies a task-list for a given {@link Driver}, which contains
 * {@link Task} objects currently assigned to that {@link Driver}.
 */
@SuppressWarnings("WeakerAccess")
public final class Tasklist {
  // -- internals -- //
  /**
   * Assigned {@link Driver} for this tasklist.
   */
  final @NotNull Driver driver;

  /**
   * List of assigned {@link Task} objects.
   */
  final @NotNull Collection<Task> assignedTasks;

  // -- constructor -- //
  /**
   * Construct an empty task-list for a given {@link Driver}.
   *
   * @param driver Driver object for this task-list.
   */
  Tasklist(final @NotNull Driver driver) {
    this.driver = driver;
    this.assignedTasks = new ArrayList<>();
  }

  /**
   * Construct a pre-filled task-list for a given {@link Driver}.
   *
   * @param driver Driver object for this task-list.
   * @param tasks Collection of tasks for this driver.
   */
  Tasklist(final @NotNull Driver driver,
           final @NotNull Collection<Task> tasks) {
    this.driver = driver;

    // note: copied here to avoid mutation of underlying private value -
    // chose not to use `unmodifiableCollection` because we modify tasks as we go
    this.assignedTasks = new ArrayList<>(tasks.size());
    this.assignedTasks.addAll(tasks);
  }

  // -- public API -- //
  /**
   * Assign a @{link Task} to this list and {@link Driver}.
   *
   * @since 1.0
   * @param task Task to assign to the local {@link Driver}.
   */
  public void assignTask(final @NotNull Task task) {
    //noinspection ConstantConditions
    if (task == null)
      throw new NullPointerException("Cannot assign `null` Task record.");
    this.assignedTasks.add(task);
  }

  /**
   * Count the number of tasks assigned to this {@link Driver}.
   *
   * @return Count of the number of tasks assigned.
   */
  public int countTasks() {
    return assignedTasks.size();
  }

  /**
   * Query this {@link Driver}'s task-list about how difficult it would
   * be to assign it a given {@link Task}. <i>"Cost"</i> in this context
   * is defined by the following algorithm: <pre>summedDistanceOfTasks</pre>.
   *
   * @since 1.0
   * @param task Subject {@link Task} to query this task-list for.
   * @return Estimated cost, as a floating point value, or <pre>0.0</pre>
   *         if this would be this {@link Driver}'s first assigned task,
   *         and thus the cost would essentially be the distance of the
   *         task itself.
   */
  public float costToAssignTask(final Task task) {
    return 0;  // @TODO actual algorithm
  }

  // -- getters -- //
  /**
   * @return {@link Driver} object.
   */
  @NotNull
  public Driver getDriver() {
    return driver;
  }

  /**
   * @return Assigned collection of {@link Task} records.
   */
  @NotNull
  public Collection<Task> getAssignedTasks() {
    return assignedTasks;
  }
}
