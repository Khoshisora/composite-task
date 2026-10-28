package edu.text.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.parser.TextParserChain;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MaxSentencesWithSameWordOperationTest {

  private final TextParserChain chain = TextParserChain.getInstance();
  private final MaxSentencesWithSameWordOperation operation =
      new MaxSentencesWithSameWordOperation();

  @Test
  void shouldFindMaximumNumberOfSentencesWithTheSameWord() throws TextProcessingException {
    // given
    Text text = chain.parse("Cat sat. Cat ran. Dog ran.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result)
        .contains("Maximum number of sentences with the same word: 2")
        .contains("Word \"cat\" (2 sentences):")
        .contains("Word \"ran\" (2 sentences):");
  }

  @Test
  void shouldCompareWordsIgnoringCase() throws TextProcessingException {
    // given
    Text text = chain.parse("Word word. WORD.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result)
        .contains("Maximum number of sentences with the same word: 2")
        .contains("Word \"word\" (2 sentences):");
  }

  @Test
  void shouldCountWordOncePerSentence() throws TextProcessingException {
    // given
    Text text = chain.parse("Cat cat dog. Cat.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).contains("Maximum number of sentences with the same word: 2");
  }

  @Test
  void shouldIgnorePunctuationWhenComparingWords() throws TextProcessingException {
    // given
    Text text = chain.parse("Cat, dog. Cat!");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result)
        .contains("Maximum number of sentences with the same word: 2")
        .contains("Word \"cat\"");
  }

  @Test
  void shouldReportNoRepeatedWords() throws TextProcessingException {
    // given
    Text text = chain.parse("One two. Three four.");
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result)
        .contains("Maximum number of sentences with the same word: 1")
        .contains("No word appears in more than one sentence.");
  }

  @Test
  void shouldHandleEmptyText() {
    // given
    Text text = new Text();
    // when
    String result = operation.execute(text, Optional.empty());
    // then
    assertThat(result).isEqualTo("The text does not contain any sentences.");
  }

  @Test
  void shouldRejectNullText() {
    assertThatThrownBy(() -> operation.execute(null, Optional.empty()))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
