package com.onf.demo.homework.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Small utility to quickly load packaged data files.
 */
@SuppressWarnings("WeakerAccess")
public final class FileUtil {
  /**
   * Return the contents of a file embedded in the local JAR, as an array of string lines.
   *
   * @param path Relative path to the file.
   * @return List of string lines in the file.
   * @throws IOException If the underlying file cannot be read.
   */
  public ArrayList<String> fileContentsFromJAR(final String path) throws IOException {
    // allocate array
    final ArrayList<String> lineArray = new ArrayList<>();

    // load embedded file data w/the Java file read dance
    try(final InputStream is = this.getClass().getClassLoader().getResourceAsStream(path)) {
      try(final InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
        try (final BufferedReader br = new BufferedReader(isr)) {
          String line;

          //noinspection NestedAssignment
          while((line = br.readLine()) != null){
            lineArray.add(line);
          }
        }
      }
    }
    return lineArray;
  }

  /**
   * Return the contents of a file embedded in the local JAR, as a String.
   *
   * @param path Relative path to the
   * @return Contents of the specified file, as a {@link String}.
   * @throws IOException If the underlying file cannot be found.
   */
  public String fileContentsFromJARAsString(final String path) throws IOException {
    // get lines
    final ArrayList<String> lineArray = fileContentsFromJAR(path);

    // build string
    final StringBuilder sb = new StringBuilder(lineArray.size());
    for (final String line : lineArray) {
      sb.append(line);
    }

    // stringify
    return sb.toString();
  }
}
