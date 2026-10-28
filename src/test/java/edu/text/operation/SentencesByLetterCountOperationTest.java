package edu.text.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.parser.TextParserChain;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SentencesByLetterCountOperationTest {

  private final TextParserChain chain = TextParserChain.getInstance();
  private final SentencesByLetterCountOperation operation =
      new SentencesByLetterCountOperation();

  @Test
  void shouldOrderSentencesByAscendingLetterCount() throws TextProcessingException {
    // given
    Text text = chain.parse("Apple. Banana. Cherry.");
    // when
    String result = operation.execute(text, Optional.of("a"));
    // then
    assertThat(result.indexOf("Cherry.")).isLessThan(result.indexOf("Apple."));
    assertThat(result.indexOf("Apple.")).isLessThan(result.indexOf("Banana."));
    assertThat(result).contains("[0] Cherry.", "[1] Apple.", "[3] Banana.");
  }

  @Test
  void shouldCountLetterIgnoringCase() throws TextProcessingException {
    // given
    Text text = chain.parse("Aaa. aaa.");
    // when
    String result = operation.execute(text, Optional.of("A"));
    // then
    assertThat(result).contains("[3] Aaa.", "[3] aaa.");
  }

  @Test
  void shouldKeepOriginalOrderForEqualCounts() throws TextProcessingException {
    // given
    Text text = chain.parse("B. A. C.");
    // when
    String result = operation.execute(text, Optional.of("z"));
    // then
    assertThat(result.indexOf("B.")).isLessThan(result.indexOf("A."));
    assertThat(result.indexOf("A.")).isLessThan(result.indexOf("C."));
  }

  @Test
  void shouldHandleEmptyText() throws TextProcessingException {
    // given
    Text text = new Text();
    // when
    String result = operation.execute(text, Optional.of("a"));
    // then
    assertThat(result).isEqualTo("The text does not contain any sentences.");
  }

  @Test
  void shouldRejectMissingParameter() {
    Text text = new Text();
    assertThatThrownBy(() -> operation.execute(text, Optional.empty()))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void shouldRejectParameterWithSeveralCharacters() {
    Text text = new Text();
    assertThatThrownBy(() -> operation.execute(text, Optional.of("ab")))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void shouldRejectNonLetterParameter() {
    Text text = new Text();
    assertThatThrownBy(() -> operation.execute(text, Optional.of("1")))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void shouldRejectBlankParameter() {
    Text text = new Text();
    assertThatThrownBy(() -> operation.execute(text, Optional.of(" ")))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void shouldRejectNullText() {
    assertThatThrownBy(() -> operation.execute(null, Optional.of("a")))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
