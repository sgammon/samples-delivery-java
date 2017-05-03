package com.onf.demo.homework.struct;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import javax.annotation.concurrent.Immutable;

import java.io.Serializable;
import java.util.UUID;


/**
 * Specifies an individual task object, which is assignable to
 * a {@link Driver} and further described by a {@link Location}.
 *
 * <p>Each task also has a unique UUID, assigned at initial construction.</p>
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class Task implements Serializable {
  // -- internals -- //
  public final static long serialVersionUID = 1L;

  /**
   * UUID for the task.
   */
  final @NotNull String uuid;

  /**
   * Assigned task location.
   */
  final @NotNull Location location;

  // -- constructor -- //
  /**
   * Construct a new Task object from scratch.
   *
   * @param uuid Unique task UUID.
   * @param location Location description for this task.
   */
  @JsonCreator
  public Task(final @JsonProperty("uuid") @NotNull String uuid,
              final @JsonProperty("location") @NotNull Location location) {
    this.uuid = uuid;
    this.location = location;
  }

  // -- static API -- //
  /**
   * Construct a new Task object, generating a random UUID to use
   * as the unique ID for the object.
   *
   * @param location Location describing the task.
   * @return Prepared Task object.
   */
  public static Task factory(final @NotNull Location location) {
    return new Task(UUID.randomUUID().toString(),
                    location);
  }

  // -- getters -- //
  /**
   * @return UUID value.
   */
  @NotNull @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  /**
   * @return Location object for this task.
   */
  @NotNull @JsonProperty("location")
  public Location getLocation() {
    return location;
  }
}
