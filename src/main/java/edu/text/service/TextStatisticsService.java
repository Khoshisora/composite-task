package edu.text.service;

import edu.text.model.CompositeTextComponent;
import edu.text.model.Symbol;
import edu.text.model.Text;
import edu.text.model.TextComponent;


public class TextStatisticsService {

  public TextStatistics calculate(Text text) {
    if (text == null) {
      throw new IllegalArgumentException("Text must not be null");
    }
    Accumulator accumulator = new Accumulator();
    collect(text, accumulator);
    String content = text.getContent();
    int characterCount = content.codePointCount(0, content.length());
    return new TextStatistics(accumulator.letters, accumulator.symbols, characterCount);
  }

  private void collect(TextComponent component, Accumulator accumulator) {
    if (component instanceof Symbol symbol) {
      int codePoint = symbol.getValue().codePointAt(0);
      if (Character.isLetter(codePoint)) {
        accumulator.letters++;
      } else if (!Character.isWhitespace(codePoint)) {
        accumulator.symbols++;
      }
      return;
    }
    if (component instanceof CompositeTextComponent composite) {
      for (TextComponent child : composite.getChildren()) {
        collect(child, accumulator);
      }
    }
  }

  private static final class Accumulator {

    private int letters;
    private int symbols;
  }
}
