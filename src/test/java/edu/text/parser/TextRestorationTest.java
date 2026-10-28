package edu.text.parser;

import static org.assertj.core.api.Assertions.assertThat;

import edu.text.exception.TextProcessingException;
import edu.text.io.TextFileReader;
import edu.text.model.Sentence;
import edu.text.model.Text;
import org.junit.jupiter.api.Test;

class TextRestorationTest {

  private final TextParserChain chain = TextParserChain.getInstance();

  @Test
  void restoreShouldNormalizeSpacesTabsAndLineWraps() throws TextProcessingException {
    // given
    String source = "  Hello\t\tworld.   It  is\tfine.\n\nNext   line!  ";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getContent()).isEqualTo("Hello world. It is fine.\n\nNext line!");
  }

  @Test
  void restoreShouldReproduceBundledSample() throws TextProcessingException {
    // given
    String sample = new TextFileReader().readResource("text/sample.txt");
    // when
    Text text = chain.parse(sample);
    // then
    String expected = sample.strip().replace("\r\n", "\n").replace('\r', '\n');
    assertThat(text.getContent()).isEqualTo(expected);
  }

  @Test
  void restoreShouldPreservePunctuationAndCyrillic() throws TextProcessingException {
    // given
    String source = "Привет, мир! It's more-or-less fine: 'quoted text'.";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getContent()).isEqualTo(source);
  }

  @Test
  void restoreShouldNotContainTabs() throws TextProcessingException {
    // given
    String source = "First\tsecond.\n\nThird\tfourth.";
    // when
    String restored = chain.parse(source).getContent();
    // then
    assertThat(restored)
        .doesNotContain("\t")
        .isEqualTo("First second.\n\nThird fourth.");
  }

  @Test
  void sentenceContentShouldBeRestoredWithoutTrailingSpaces() throws TextProcessingException {
    // given
    String source = "One.   Two.  ";
    // when
    Sentence lastSentence = chain.parse(source)
        .getChildren().get(0)
        .getChildren().get(1);
    // then
    assertThat(lastSentence.getContent()).isEqualTo("Two.");
  }
}
