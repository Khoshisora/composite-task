package edu.text.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TextFileReaderTest {

  private final TextFileReader reader = new TextFileReader();

  @TempDir
  Path tempDirectory;

  @Test
  void readShouldReadUtf8File() throws IOException, TextProcessingException {
    // given
    Path file = tempDirectory.resolve("text.txt");
    String content = "Привет, мир!\nSecond line.";
    Files.writeString(file, content, StandardCharsets.UTF_8);
    // when
    String actual = reader.read(file);
    // then
    assertThat(actual).isEqualTo(content);
  }

  @Test
  void readShouldRejectMissingFile() {
    // given
    Path missing = tempDirectory.resolve("missing.txt");
    // when, then
    assertThatThrownBy(() -> reader.read(missing))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void readShouldRejectNullPath() {
    assertThatThrownBy(() -> reader.read(null))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void readResourceShouldReadBundledSample() throws TextProcessingException {
    // when
    String content = reader.readResource("text/sample.txt");
    // then
    assertThat(content).contains("It has survived", "по английски");
  }

  @Test
  void readResourceShouldRejectMissingResource() {
    assertThatThrownBy(() -> reader.readResource("text/missing.txt"))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void readResourceShouldRejectBlankName() {
    assertThatThrownBy(() -> reader.readResource(" "))
        .isInstanceOf(TextProcessingException.class);
  }
}
