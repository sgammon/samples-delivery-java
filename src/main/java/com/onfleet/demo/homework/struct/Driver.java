package com.onfleet.demo.homework.struct;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.UUID;


/**
 * Specifies a Driver that may be assigned {@link Task}s. Each
 * driver is uniquely addressable by a randomly-generated UUID
 * which is assigned upon construction.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public final class Driver implements Serializable {
  // -- internals -- //
  public final static long serialVersionUID = 1L;

  /**
   * UUID for the driver.
   */
  final @NotNull String uuid;

  /**
   * Name for the driver.
   */
  final @Nullable String name;

  // -- constructor -- //
  /**
   * Construct a new Driver object from scratch.
   *
   * @param uuid Driver UUID.
   * @param name Generated name for the driver.
   */
  @JsonCreator
  public Driver(final @JsonProperty("uuid") @NotNull String uuid,
                final @JsonProperty("name") @Nullable String name) {
    this.uuid = uuid;
    this.name = name;
  }

  // -- public API -- //
  /**
   * Overridden <pre>equals</pre> method for two {@link Driver} objects.
   *
   * @param other Other object to compare.
   * @return Whether the two should be considered equal or not.
   */
  @Override
  public boolean equals(final Object other) {
    return other instanceof Driver && this.hashCode() == other.hashCode();
  }

  /**
   * Return the hash code for the underlying {@link Driver}'s UUID.
   *
   * @return Hash code for this {@link Driver}'s UUID.
   */
  @Override
  public int hashCode() {
    return this.uuid.hashCode();
  }

  /**
   * Use the driver's name as their string value.
   *
   * @return Driver's name.
   */
  @Override
  public String toString() {
    return this.name;
  }


  // -- static API -- //
  /**
   * Construct a new Driver object, with a random UUID specifying
   * the unique identity of the object.
   *
   * @param name Name of the driver.
   * @return Generated {@link Driver} object.
   */
  @NotNull
  public static Driver factory(final @Nullable String name) {
    return new Driver(UUID.randomUUID().toString(), name);
  }

  // -- getters -- //
  /**
   * @return Driver UUID.
   */
  @NotNull @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  /**
   * @return Driver name.
   */
  @Nullable @JsonProperty("name")
  public String getName() {
    return name;
  }
}
