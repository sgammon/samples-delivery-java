package com.onf.demo.homework.util;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onf.demo.homework.cli.AppLogger;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * Utility that loads a dataset of common given- and sur-names, for use when auto-generating names.
 */
@Immutable
@SuppressWarnings({"unused", "WeakerAccess"})
public final class NameHelper implements Serializable, Iterator<String> {
  // -- internals -- //
  public final static long serialVersionUID = 1L;

  /**
   * Filename for a dataset of first names.
   */
  final transient @NotNull static String firstNameFile;

  /**
   * Filename for a dataset of last names.
   */
  final transient @NotNull static String lastNameFile;

  /**
   * Set of first names that we can pull from.
   */
  final @NotNull List<String> firstNames;

  /**
   * Set of last names that we can pull from.
   */
  final @NotNull List<String> lastNames;

  /**
   * Cache the number of available first names.
   */
  final transient int numberOfFirstNames;

  /**
   * Cache the number of available last names.
   */
  final transient int numberOfLastNames;

  static {
    // -- load config details for embedded name data, build random generator
    firstNameFile = System.getProperty("sample.name-data.firstnames", "firstnames.txt");
    lastNameFile = System.getProperty("sample.name-data.lastnames", "surnames.txt");
  }

  // -- constructor -- //
  /**
   * Construct a new name helper, with a loaded set of first and last names.
   *
   * @param firstNames First names to use.
   * @param lastNames Last names to use.
   */
  @JsonCreator
  NameHelper(final @NotNull @JsonProperty("first") List<String> firstNames,
             final @NotNull @JsonProperty("last") List<String> lastNames) {
    this.firstNames = Collections.unmodifiableList(firstNames);
    this.lastNames = Collections.unmodifiableList(lastNames);
    this.numberOfFirstNames = this.firstNames.size();
    this.numberOfLastNames = this.lastNames.size();
  }

  /**
   * Construct a new name helper, with a loaded set of first and last names.
   *
   * @throws IOException If the underlying embedded name data cannot be loaded.
   */
  NameHelper() throws IOException {
    AppLogger.say("NameHelper", "Loading random name data for drivers...");
    this.firstNames = loadBuiltinNamesFromFile(firstNameFile);
    this.lastNames = loadBuiltinNamesFromFile(lastNameFile);
    this.numberOfFirstNames = this.firstNames.size();
    this.numberOfLastNames = this.lastNames.size();
  }

  // -- static API -- //
  /**
   * Load JAR-embedded name data.
   *
   * @param nameFile File to load and split-by-newlines.
   * @return Set of loaded names from the file.
   * @throws IOException If the underlying file cannot be read/found.
   */
  List<String> loadBuiltinNamesFromFile(final String nameFile) throws IOException {
    return new FileUtil().fileContentsFromJAR(nameFile);
  }

  // -- interface compliance: Iterator<String> -- //
  /**
   * Supply a value for <pre>Iterator.hasNext</pre>, which is always <pre>true</pre>, since we
   * can randomly generate names forever.
   *
   * @return Always <pre>true</pre>.
   */
  @Override
  public boolean hasNext() {
    return true;
  }

  /**
   * Generate a random name as part of an iteration routine.
   *
   * @return Next random name.
   */
  @Override
  public String next() {
    return this.generateName();
  }

  /**
   * Remove an item from the iterator. Unsupported.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  // -- public API -- //
  /**
   * Generate a combo of a first and last name from our bag-of-names data.
   *
   * @return Generated name.
   */
  public String generateName() {
    final String firstName = this.firstNames.get(RandomNumberUtil.getRandomGenerator().nextInt(numberOfFirstNames));
    final String lastName = this.lastNames.get(RandomNumberUtil.getRandomGenerator().nextInt(numberOfLastNames));
    return firstName + " " + lastName;
  }

  // -- getters -- //
  /**
   * @return Retrieve the set of first names.
   */
  @NotNull @JsonProperty("first")
  public List<String> getFirstNames() {
    return firstNames;
  }

  /**
   * @return Retrieve the set of last names.
   */
  @NotNull @JsonProperty("last")
  public List<String> getLastNames() {
    return lastNames;
  }
}
