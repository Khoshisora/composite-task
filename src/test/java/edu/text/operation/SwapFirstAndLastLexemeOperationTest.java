package edu.text.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.parser.TextParserChain;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SwapFirstAndLastLexemeOperationTest {

  private final TextParserChain chain = TextParserChain.getInstance();
  private final SwapFirstAndLastLexemeOperation operation =
      new SwapFirstAndLastLexemeOperation();

  @Test
  void shouldSwapFirstAndLastLexeme() throws TextProcessingException {
    // given
    Text text = chain.parse("One two three.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).isEqualTo("three. two One");
  }

  @Test
  void shouldSwapLexemesTogetherWithPunctuation() throws TextProcessingException {
    // given
    Text text = chain.parse("First, middle last!");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).isEqualTo("last! middle First,");
  }

  @Test
  void shouldKeepSingleLexemeSentenceUnchanged() throws TextProcessingException {
    // given
    Text text = chain.parse("Single.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).isEqualTo("Single.");
  }

  @Test
  void shouldProcessEverySentenceAndParagraph() throws TextProcessingException {
    // given
    Text text = chain.parse("One two. Three four.\n\nFive six.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).isEqualTo("two. One four. Three\n\nsix. Five");
  }

  @Test
  void shouldPreserveLengthOfEachSentence() throws TextProcessingException {
    // given
    List<String> sentences = List.of("One two three.", "Four five.", "Six.");
    // when / then
    for (String sentence : sentences) {
      String result = operation.execute(chain.parse(sentence), Optional.empty());
      assertThat(result)
          .as("Length of the sentence '%s' must not change", sentence)
          .hasSameSizeAs(sentence);
    }
  }

  @Test
  void shouldPreserveTotalLengthOfMultilineText() throws TextProcessingException {
    // given
    Text text = chain.parse("One two three. Four five.\n\nSix.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).hasSameSizeAs(text.getContent());
  }

  @Test
  void shouldHandleEmptyText() {
    // given
    Text text = new Text();
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).isEmpty();
  }

  @Test
  void shouldRejectNullText() {
    assertThatThrownBy(() -> operation.execute(null, Optional.empty()))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
