package com.onf.demo.homework.struct;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.UUID;


/**
 * Specifies location information for a {@link Task}, which may be assigned
 * to a {@link Driver}. A location carries with it a pair of geo-coordinates.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public final class Location implements Serializable {
  // -- internals -- //
  public final static long serialVersionUID = 1L;

  /**
   * UUID for the location.
   */
  final @NotNull String uuid;

  /**
   * String name for the location.
   */
  final @Nullable String label;

  /**
   * Latitude/longitude value for this location.
   */
  final @NotNull Geopoint geopoint;

  // -- constructor -- //
  /**
   * Construct a new Location object from scratch.
   *
   * @param uuid Unique UUID for this location.
   * @param label Place name for the location.
   * @param geopoint Latitude/longitude for this location.
   */
  @JsonCreator
  public Location(final @JsonProperty("uuid") @NotNull String uuid,
                  final @JsonProperty("label") @Nullable String label,
                  final @JsonProperty("geopoint") @NotNull Geopoint geopoint) {
    //noinspection ConstantConditions
    if (geopoint == null)
      throw new IllegalArgumentException("`geopoint` was found to be null.");
    this.uuid = uuid;
    this.label = label;
    this.geopoint = geopoint;
  }

  // -- static API -- //
  /**
   * Generate a new {@link Location}, with no name, generating a
   * random {@link UUID} as we go.
   *
   * @param geopoint Latitude/longitude for this location.
   * @return Newly created {@link Location} object.
   */
  public static Location factory(final @NotNull Geopoint geopoint) {
    return new Location(UUID.randomUUID().toString(),
                        null,
                        geopoint);
  }

  /**
   * Generate a new {@link Location}, with a name, generating a
   * random {@link UUID} as we go.
   *
   * @param name Name of the location.
   * @param geopoint Latitude/longitude for this location.
   * @return Newly created {@link Location} object.
   */
  public static Location factory(final @NotNull String name,
                                 final @NotNull Geopoint geopoint) {
    return new Location(UUID.randomUUID().toString(),
                        name,
                        geopoint);
  }

  // -- getters -- //
  /**
   * @return This location's underlying UUID.
   */
  @NotNull @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  /**
   * @return This location's given place name.
   */
  @Nullable @JsonProperty("label")
  public String getLabel() {
    return label;
  }

  /**
   * @return Longitude/latitude value as a {@link Geopoint}.
   */
  @NotNull @JsonProperty("geopoint")
  public Geopoint getGeopoint() {
    return geopoint;
  }
}
