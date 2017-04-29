package com.onfleet.demo.homework.collection;


import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Geopoint;
import com.onfleet.demo.homework.struct.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Specifies a task-list for a given {@link Driver}, which contains
 * {@link Task} objects currently assigned to that {@link Driver}.
 */
@SuppressWarnings("WeakerAccess")
public final class Tasklist implements Comparable<Tasklist> {
  // -- algorithm configuration -- //
  final static double TASK_COUNT_WEIGHT = 0.2;

  // -- internals -- //
  /**
   * Assigned {@link Driver} for this tasklist.
   */
  final @NotNull Driver driver;

  /**
   * List of assigned {@link Task} objects.
   */
  final @NotNull Collection<Task> assignedTasks;

  /**
   * Last assigned task for this {@link Driver}.
   */
  @Nullable Task lastAssignedTask;

  /**
   * Current count of assigned tasks, cached so we don't have to keep
   * interrogating the task collection.
   */
  int taskCount;

  /**
   * Current load estimate given the set of {@link Task} records assigned
   * to this {@link Tasklist}.
   */
  double loadEstimate;

  /**
   * Current known distance of assigned tasks to this {@link Tasklist}.
   */
  double knownDistance;

  // -- constructor -- //
  /**
   * Construct an empty task-list for a given {@link Driver}.
   *
   * @param driver Driver object for this task-list.
   */
  public Tasklist(final @NotNull Driver driver) {
    this.driver = driver;
    this.assignedTasks = new ArrayList<>();
    this.loadEstimate = 0.0;
    this.knownDistance = 0.0;
    this.taskCount = 0;
    this.lastAssignedTask = null;
  }

  /**
   * Construct a pre-filled task-list for a given {@link Driver}.
   *
   * @param driver Driver object for this task-list.
   * @param tasks Collection of tasks for this driver.
   */
  public Tasklist(final @NotNull Driver driver,
                  final @NotNull Collection<Task> tasks) {
    this.driver = driver;
    this.knownDistance = 0.0;

    // note: copied here to avoid mutation of underlying private value -
    // chose not to use `unmodifiableCollection` because we modify tasks as we go
    this.assignedTasks = new ArrayList<>(tasks.size());
    this.assignedTasks.addAll(tasks);
    this.taskCount = this.assignedTasks.size();

    Task lastTask = null;
    double currentKnownDistance = this.knownDistance;
    for (final Task task : tasks) {
      currentKnownDistance += calculateDistanceForPoints(lastTask, task);
      lastTask = task;
    }
    this.lastAssignedTask = lastTask;
    this.knownDistance = currentKnownDistance;
    recalculateLoadEstimate();
  }

  // -- static API -- //
  /**
   * Calculate the distance cost between two tasks using their
   * underlying geopoints.
   *
   * @param start Start geopoint.
   * @param finish End geopoint.
   * @return The value to use for the distance.
   */
  public static double calculateDistanceForPoints(final Task start, final Task finish) {
    if (start == null)
      // it's the first task for this driver: the distance is 0
      return 0.0;

    final Geopoint startPoint = start.getLocation().getGeopoint();
    final Geopoint endPoint = finish.getLocation().getGeopoint();

    // get min and max points
    final double largestLatitude = Math.max(startPoint.getLatitude(), endPoint.getLatitude());
    final double largestLongitude =Math.max(startPoint.getLongitude(), endPoint.getLongitude());

    final double smallestLatitude = Math.min(startPoint.getLatitude(), endPoint.getLatitude());
    final double smallestLongitude = Math.min(startPoint.getLongitude(), endPoint.getLongitude());

    // return summed absolute difference of points
    final double difference = (Math.abs(largestLatitude - smallestLatitude)
                               + Math.abs(largestLongitude - smallestLongitude));

    // make sure we're acting sane
    assert difference >= 0.0;
    return difference;
  }

  // -- private API -- //
  /**
   * Recalculate the internal load estimate after mutating the set of assigned
   * tasks for this {@link Driver}.
   */
  private void recalculateLoadEstimate() {
    final double taskCountAsDouble = (double)this.taskCount;
    this.loadEstimate += ((this.knownDistance / taskCountAsDouble) * (taskCountAsDouble * TASK_COUNT_WEIGHT));
  }

  // -- interface compliance: Comparable<Tasklist> -- //
  /**
   * Compare two tasklists, using their estimated current cost to give them an
   * order.
   *
   * @param other Other tasklist.
   * @return Comparison status for these two {@link Tasklist} objects.
   */
  @Override
  public int compareTo(final @NotNull Tasklist other) {
    // equal load estimate: probably no tasks on either side, so, 0.0
    return Double.compare(other.loadEstimate, this.loadEstimate);
  }

  /**
   * Overridden copy of `equals`.
   *
   * @param other Other object.
   * @return Whether the other object is equal to this one.
   */
  @Override
  public boolean equals(final Object other) {
    // defer to hashCode
    return other instanceof Tasklist && other.hashCode() == this.hashCode();
  }

  /**
   * Defer to the underlying {@link Driver}'s UUID for this object's
   * hash code.
   *
   * @return Hash code of the underlying {@link Driver}'s UUID.
   */
  @Override
  public int hashCode() {
    return this.getDriver().hashCode();
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
    this.assignedTasks.add(task);
    this.taskCount += 1;
    this.knownDistance += calculateDistanceForPoints(this.lastAssignedTask, task);
    this.lastAssignedTask = task;
    this.recalculateLoadEstimate();
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
  public double costToAssignTask(final Task task) {
    if (lastAssignedTask == null)
      return 0.0;  // it would be this driver's first task: no cost
    return calculateDistanceForPoints(this.lastAssignedTask, task);
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

  /**
   * @return Boxed {@link Integer} describing the number of tasks this {@link Tasklist} has.
   */
  @NotNull
  public Integer getTaskCount() {
    return taskCount;
  }

  /**
   * @return Boxed {@link Double} describing the estimated load this {@link Tasklist} is assigned.
   */
  @NotNull
  public Double getLoadEstimate() {
    return loadEstimate;
  }

  /**
   * @return Boxed {@link Double} describing the known distance this driver already has.
   */
  @NotNull
  public Double getKnownDistance() {
    return knownDistance;
  }

  /**
   * @return Last assigned task to this {@link Tasklist}.
   */
  @Nullable
  public Task getLastAssignedTask() {
    return lastAssignedTask;
  }
}
