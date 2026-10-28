package edu.text.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.io.TextFileReader;
import edu.text.model.Text;
import edu.text.parser.TextParserChain;
import org.junit.jupiter.api.Test;

class TextServiceTest {

  private final TextFileReader reader = new TextFileReader();
  private final TextService textService =
      new TextService(reader, TextParserChain.getInstance());

  @Test
  void readResourceShouldReadAndParseBundledSample() throws TextProcessingException {
    // when
    Text text = textService.readResource("text/sample.txt");
    // then
    assertThat(text.getChildren()).hasSize(4);
    assertThat(text.getContent()).contains("It has survived");
  }

  @Test
  void parseShouldRejectBlankText() {
    assertThatThrownBy(() -> textService.parse("  \n "))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void parseShouldRejectNullText() {
    assertThatThrownBy(() -> textService.parse(null))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void constructorShouldRejectNullArguments() {
    assertThatThrownBy(() -> new TextService(null, TextParserChain.getInstance()))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new TextService(reader, null))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
