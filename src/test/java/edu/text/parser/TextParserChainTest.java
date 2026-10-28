package edu.text.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.model.Lexeme;
import edu.text.model.Paragraph;
import edu.text.model.Sentence;
import edu.text.model.Symbol;
import edu.text.model.Text;
import edu.text.model.Word;
import org.junit.jupiter.api.Test;

class TextParserChainTest {

  private final TextParserChain chain = TextParserChain.getInstance();

  @Test
  void parseShouldBuildFullCompositeHierarchy() throws TextProcessingException {
    // given
    String source = "Hello, world! Bye.";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getChildren()).hasSize(1);
    Paragraph paragraph = text.getChildren().get(0);
    assertThat(paragraph.getChildren()).hasSize(2);

    Sentence firstSentence = paragraph.getChildren().get(0);
    assertThat(firstSentence.getChildren()).hasSize(2);
    assertThat(firstSentence.getContent()).isEqualTo("Hello, world!");

    Lexeme firstLexeme = firstSentence.getChildren().get(0);
    assertThat(firstLexeme.getChildren()).hasSize(2);
    assertThat(firstLexeme.getChildren().get(0)).isInstanceOf(Word.class);
    assertThat(firstLexeme.getChildren().get(1)).isInstanceOf(Symbol.class);
    assertThat(firstLexeme.getChildren().get(1).getContent()).isEqualTo(",");

    Word word = (Word) firstLexeme.getChildren().get(0);
    assertThat(word.getChildren()).hasSize(5);
    assertThat(word.getContent()).isEqualTo("Hello");
  }

  @Test
  void parseShouldSplitParagraphsByBlankLines() throws TextProcessingException {
    // given
    String source = "First line\ncontinues here.\n\nSecond paragraph.";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getChildren()).hasSize(2);
    assertThat(text.getChildren().get(0).getChildren().get(0).getContent())
        .isEqualTo("First line continues here.");
    assertThat(text.getChildren().get(1).getChildren().get(0).getContent())
        .isEqualTo("Second paragraph.");
  }

  @Test
  void parseShouldSplitSentenceByTerminalPunctuation() throws TextProcessingException {
    // given
    String source = "One. Two? Three! Four… Five";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getChildren()).hasSize(1);
    assertThat(text.getChildren().get(0).getChildren()).hasSize(5);
  }

  @Test
  void parseShouldTreatInnerApostropheAsPartOfWord() throws TextProcessingException {
    // given
    String source = "Don't stop.";
    // when
    Word word = (Word) chain.parse(source)
        .getChildren().get(0)
        .getChildren().get(0)
        .getChildren().get(0)
        .getChildren().get(0);
    // then
    assertThat(word.getContent()).isEqualTo("Don't");
    assertThat(word.getChildren()).hasSize(5);
  }

  @Test
  void parseShouldKeepLeadingApostropheAsPunctuation() throws TextProcessingException {
    // given
    String source = "'Content here', making";
    // when
    Lexeme firstLexeme = chain.parse(source)
        .getChildren().get(0)
        .getChildren().get(0)
        .getChildren().get(0);
    // then
    assertThat(firstLexeme.getChildren().get(0)).isInstanceOf(Symbol.class);
    assertThat(firstLexeme.getChildren().get(1)).isInstanceOf(Word.class);
    assertThat(firstLexeme.getChildren().get(1).getContent()).isEqualTo("Content");
  }

  @Test
  void parseShouldKeepClosingQuoteWithSentence() throws TextProcessingException {
    // given
    String source = "He said \"Stop.\" Then he left.";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getChildren().get(0).getChildren()).hasSize(2);
    assertThat(text.getChildren().get(0).getChildren().get(0).getContent())
        .isEqualTo("He said \"Stop.\"");
    assertThat(text.getChildren().get(0).getChildren().get(1).getContent())
        .isEqualTo("Then he left.");
  }

  @Test
  void parseShouldNotSplitDecimalNumber() throws TextProcessingException {
    // given
    String source = "Pi is 3.14 exactly.";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getChildren().get(0).getChildren()).hasSize(1);
    assertThat(text.getContent()).isEqualTo("Pi is 3.14 exactly.");
  }

  @Test
  void parseShouldSupportCyrillicText() throws TextProcessingException {
    // given
    String source = "Привет, мир!";
    // when
    Text text = chain.parse(source);
    // then
    assertThat(text.getContent()).isEqualTo("Привет, мир!");
    assertThat(text.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
  }

  @Test
  void parseShouldRejectBlankSource() {
    assertThatThrownBy(() -> chain.parse(" \n\t "))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void parseShouldRejectNullSource() {
    assertThatThrownBy(() -> chain.parse(null))
        .isInstanceOf(TextProcessingException.class);
  }

  @Test
  void chainShouldBeReusedAsSingleton() {
    assertThat(TextParserChain.getInstance()).isSameAs(TextParserChain.getInstance());
  }
}
