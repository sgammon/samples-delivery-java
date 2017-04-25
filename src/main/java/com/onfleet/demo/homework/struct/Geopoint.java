package com.onfleet.demo.homework.struct;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * Specifies an individual geo-point, with a geopoint and longitude.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public final class Geopoint implements Serializable {
  // -- internals -- //
  public final static long serialVersionUID = 1L;

  /**
   * Latitude value.
   */
  private final double latitude;

  /**
   * Longitude value.
   */
  private final double longitude;

  // -- constructor -- //
  /**
   * Construct a new {@link Geopoint} structure.
   *
   * @param latitude Latitude value.
   * @param longitude Longitude value.
   */
  @JsonCreator
  public Geopoint(final @NotNull @JsonProperty("latitude") Double latitude,
                  final @NotNull @JsonProperty("longitude") Double longitude) {
    //noinspection ConstantConditions
    if (latitude == null || longitude == null)
      throw new IllegalArgumentException(latitude == null ? (longitude == null
                                                                 ? "Latitude and Longitude must both not be `null`."
                                                                 : "Latitude may not be `null`.")
                                             : "Longitude may not be `null`.");
    this.latitude = latitude;
    this.longitude = longitude;
  }

  // -- equality + formatting -- //
  /**
   * Override equality check to support checking underlying latitude/longitude.
   *
   * @param otherObject Candidate object to check.
   * @return Whether the two are equal.
   */
  @Override
  public boolean equals(final Object otherObject) {
    if (otherObject instanceof Geopoint) {
      final Geopoint other = (Geopoint)otherObject;
      return other.getLatitude().equals(latitude)
             && other.getLongitude().equals(longitude);
    }
    return false;  // not even a geo-point
  }

  /**
   * Override this object's hash code as the code of the pair of points, as a
   * string.
   *
   * @return Hash code for this {@link Geopoint} object.
   */
  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  /**
   * Format this geo point as a sensible string.
   *
   * @return Formatted string for this geo point.
   */
  @Override
  public String toString() {
    return "Point(" + String.valueOf(latitude) + ", " + String.valueOf(longitude) + ")";
  }

  // -- getters -- //
  /**
   * @return Retrieve the geopoint value for this point.
   */
  @NotNull @JsonProperty("longitude")
  public Double getLatitude() {
    return latitude;
  }

  /**
   * @return Retrieve the longitude value for this point.
   */
  @NotNull @JsonProperty("longitude")
  public Double getLongitude() {
    return longitude;
  }
}