package edu.text.io;

import edu.text.exception.TextProcessingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TextFileReader {

  private static final Logger LOGGER = LogManager.getLogger(TextFileReader.class);

  public String read(Path path) throws TextProcessingException {
    if (path == null) {
      throw new TextProcessingException("File path must not be null");
    }
    LOGGER.debug("Reading text file: {}", path);
    try {
      String content = Files.readString(path, StandardCharsets.UTF_8);
      LOGGER.debug("Read {} characters from {}", content.length(), path);
      return content;
    } catch (IOException e) {
      throw new TextProcessingException("Cannot read text file: " + path, e);
    }
  }

  public String readResource(String resourceName) throws TextProcessingException {
    if (resourceName == null || resourceName.isBlank()) {
      throw new TextProcessingException("Resource name must not be blank");
    }
    LOGGER.debug("Reading classpath resource: {}", resourceName);
    try (InputStream stream = TextFileReader.class.getClassLoader()
        .getResourceAsStream(resourceName)) {
      if (stream == null) {
        throw new TextProcessingException("Classpath resource not found: " + resourceName);
      }
      String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
      LOGGER.debug("Read {} characters from resource {}", content.length(), resourceName);
      return content;
    } catch (IOException e) {
      throw new TextProcessingException("Cannot read classpath resource: " + resourceName, e);
    }
  }
}
