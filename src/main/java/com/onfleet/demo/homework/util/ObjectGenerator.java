package com.onfleet.demo.homework.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Geopoint;
import com.onfleet.demo.homework.struct.Location;
import com.onfleet.demo.homework.struct.Task;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility object to generate instances of {@link Driver}, {@link Location} and
 * {@link Task} for testing and execution.
 */
@SuppressWarnings({"WeakerAccess", "UtilityClassCanBeEnum", "UtilityClass", "NullableProblems"})
final public class ObjectGenerator {
  // -- internals -- //
  /**
   * Filename for a dataset of first names.
   */
  final @NotNull static String geoBoundsFile;

  /**
   * Cached boundary {@link Polygon} for generating {@link com.onfleet.demo.homework.struct.Geopoint} objects.
   */
  static @NotNull Polygon boundaryPolygon;

  /**
   * Name of the geo boundary we're enforcing.
   */
  static @NotNull String boundaryName;

  /**
   * Cached array of boundary watermark values, organized clockwise from the top left - i.e.
   * top left, top right, bottom right, and bottom left.
   */
  static @NotNull Watermarks geopointWatermarks;

  static {
    // load geo-bounds from JSON
    geoBoundsFile = System.getProperty("sample.geo-data.boundaries", "bounds.json");
    loadGeoJSONBoundaries();
  }

  // -- embedded classes -- //
  @Immutable
  final static class Watermarks {
    /**
     * Latitude value.
     */
    final double latitudeLowMark;

    /**
     * Longitude value.
     */
    final double latitudeHiMark;

    /**
     * Latitude value.
     */
    final double longitudeLowMark;

    /**
     * Longitude value.
     */
    final double longitudeHiMark;

    /**
     * Seal watermarks in a structure to contain them.
     *
     * @param latitudeLowMark Lowest latitude encountered.
     * @param latitudeHiMark Highest latitude encountered.
     * @param longitudeLowMark Lowest longitude encountered.
     * @param longitudeHiMark Highest longitude encountered.
     */
    public Watermarks(final double latitudeLowMark,
                      final double latitudeHiMark,
                      final double longitudeLowMark,
                      final double longitudeHiMark) {
      this.latitudeLowMark = latitudeLowMark;
      this.latitudeHiMark = latitudeHiMark;
      this.longitudeLowMark = longitudeLowMark;
      this.longitudeHiMark = longitudeHiMark;
    }

    /**
     * @return Retrieve the latitude low mark.
     */
    public double getLatitudeLowMark() {
      return latitudeLowMark;
    }

    /**
     * @return Retrieve the latitude high mark.
     */
    public double getLatitudeHiMark() {
      return latitudeHiMark;
    }

    /**
     * @return Retrieve the longitude low mark.
     */
    public double getLongitudeLowMark() {
      return longitudeLowMark;
    }

    /**
     * @return Retrieve the longitude high mark.
     */
    public double getLongitudeHiMark() {
      return longitudeHiMark;
    }
  }

  /**
   * Generate a random location within a given set of boundaries.
   *
   * @return Random Location.
   */
  static @NotNull Geopoint randomLocationWithinBounds() {
    return new Geopoint(0.0, 0.0);  // @TODO: real code
  }

  // -- object generators -- //
  /**
   * Generate a random {@link Location} in the SF Bay Area.
   *
   * @return Location object, randomly generated.
   */
  public @NotNull static Location generateLocation() {
    final Geopoint randomGeopoint = randomLocationWithinBounds();
    return Location.factory(randomGeopoint);
  }

  /**
   * Load geo boundaries, specified in an embedded GeoJSON file.
   * This is rather quick-and-dirty since it's in a util and is
   * exercised each time the testsuite is run.
   */
  @SuppressWarnings({"unchecked", "TypeMayBeWeakened", "ConstantConditions"})
  static void loadGeoJSONBoundaries() {
    // decode JSON into raw map
    final String geoJSON;
    final Map<String, Object> decodedJSON;
    try {
      geoJSON = new FileUtil().fileContentsFromJARAsString(geoBoundsFile);
      decodedJSON = new ObjectMapper().readerFor(HashMap.class).readValue(geoJSON);
    } catch (final IOException e) {
      throw new IllegalStateException(e);  // known to exist, would never do this in prod
    }

    // label in GeoJSON is specified in "features"->0->properties->name
    // bounds in GeoJSON are specified in "features"->0->geometry->coordinates

    final ArrayList<Map<String, Object>> features = (ArrayList)decodedJSON.get("features");
    final Map<String, Object> defaultFeature = features.get(0);

    final Map<String, Object> featureProperties = (Map<String, Object>)defaultFeature.get("properties");
    final Map<String, Object> geometryProperties = (Map<String, Object>)defaultFeature.get("geometry");

    final String name = (String)featureProperties.get("name");
    final List<List<List<List<Double>>>> coordinatesArrayOuter = (List)geometryProperties.get("coordinates");
    final List<List<Double>> coordinatesArray = coordinatesArrayOuter.get(0).get(0);
    final Polygon polygon = new Polygon();

    // extract each coordinate
    Double latitudeLowBound = 0.0;
    Double longitudeLowBound = 0.0;
    Double latitudeHiBound = 0.0;
    Double longitudeHiBound = 0.0;

    for (List innerCoordinate : coordinatesArray) {
      // extract coordinates
      final Double latitude = (Double)innerCoordinate.get(0);
      final Double longitude = (Double)innerCoordinate.get(1);

      // calculate bounds for latitude
      if (latitudeLowBound.equals(0.0))
        latitudeLowBound = latitude;
      else if (latitudeLowBound > latitude)
        latitudeLowBound = latitude;

      if (latitudeHiBound.equals(0.0))
        latitudeHiBound = latitude;
      else if (latitudeHiBound < latitude)
        latitudeHiBound = latitude;

      // calculate bounds for longitude
      if (longitudeLowBound.equals(0.0))
        longitudeLowBound = longitude;
      else if (longitudeLowBound > longitude)
        longitudeLowBound = longitude;

      if (longitudeHiBound.equals(0.0))
        longitudeHiBound = longitude;
      else if (latitudeHiBound < longitude)
        longitudeHiBound = longitude;

      polygon.addPoint(latitude.intValue(),
                       longitude.intValue());
    }

    // setup static properties
    boundaryName = name;
    boundaryPolygon = polygon;
    geopointWatermarks = new Watermarks(latitudeLowBound, latitudeHiBound,
                                        longitudeLowBound, longitudeHiBound);
  }

  /**
   * Generate a random {@link Task} with a random {@link Location},
   * which is supported by {@link #generateLocation()}.
   *
   * @return Task object, randomly generated.
   */
  public @NotNull static Task generateTask() {
    return Task.factory(generateLocation());
  }

  /**
   * Generate a random {@link Driver}.
   *
   * @return Driver object, randomly generated.
   */
  public @NotNull static Driver generateDriver() {
    return Driver.factory(null);
  }

  /**
   * Generate a random {@link Driver} using {@link NameHelper}.
   *
   * @param helper Name generation helper.
   * @return Driver object, with a randomly-generated name.
   */
  public @NotNull static Driver generateDriver(final NameHelper helper) {
    return Driver.factory(helper.generateName());
  }
}
