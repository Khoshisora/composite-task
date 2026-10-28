package edu.text.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.parser.TextParserChain;
import org.junit.jupiter.api.Test;

class TextStatisticsServiceTest {

  private final TextStatisticsService statisticsService = new TextStatisticsService();
  private final TextParserChain chain = TextParserChain.getInstance();

  @Test
  void calculateShouldCountLettersSymbolsAndCharacters() throws TextProcessingException {
    // given
    Text text = chain.parse("Hi! Bye?");
    // when
    TextStatistics statistics = statisticsService.calculate(text);
    // then
    assertThat(statistics.letterCount()).isEqualTo(5);
    assertThat(statistics.symbolCount()).isEqualTo(2);
    assertThat(statistics.characterCount()).isEqualTo(8);
  }

  @Test
  void calculateShouldCountWhitespaceOnlyAsCharacters() throws TextProcessingException {
    // given
    Text text = chain.parse("a b");
    // when
    TextStatistics statistics = statisticsService.calculate(text);
    // then
    assertThat(statistics.letterCount()).isEqualTo(2);
    assertThat(statistics.symbolCount()).isZero();
    assertThat(statistics.characterCount()).isEqualTo(3);
  }

  @Test
  void calculateShouldSupportCyrillicLetters() throws TextProcessingException {
    // given
    Text text = chain.parse("Привет!");
    // when
    TextStatistics statistics = statisticsService.calculate(text);
    // then
    assertThat(statistics.letterCount()).isEqualTo(6);
    assertThat(statistics.symbolCount()).isEqualTo(1);
    assertThat(statistics.characterCount()).isEqualTo(7);
  }

  @Test
  void calculateShouldCountDigitsAsSymbols() throws TextProcessingException {
    // given
    Text text = chain.parse("2026");
    // when
    TextStatistics statistics = statisticsService.calculate(text);
    // then
    assertThat(statistics.letterCount()).isZero();
    assertThat(statistics.symbolCount()).isEqualTo(4);
    assertThat(statistics.characterCount()).isEqualTo(4);
  }

  @Test
  void calculateShouldReturnZeroesForEmptyText() {
    // given
    Text text = new Text();
    // when
    TextStatistics statistics = statisticsService.calculate(text);
    // then
    assertThat(statistics.letterCount()).isZero();
    assertThat(statistics.symbolCount()).isZero();
    assertThat(statistics.characterCount()).isZero();
  }

  @Test
  void calculateShouldRejectNullText() {
    assertThatThrownBy(() -> statisticsService.calculate(null))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
