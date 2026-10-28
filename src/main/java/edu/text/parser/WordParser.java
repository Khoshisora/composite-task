package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Symbol;
import edu.text.model.TextComponent;
import edu.text.model.Word;


public class WordParser extends AbstractTextParser {

  @Override
  protected TextComponent parseComponent(String source) throws TextProcessingException {
    Word word = new Word();
    int index = 0;
    while (index < source.length()) {
      int codePoint = source.codePointAt(index);
      String symbolValue = new String(Character.toChars(codePoint));
      word.add(parseNext(symbolValue, Symbol.class));
      index += Character.charCount(codePoint);
    }
    return word;
  }
}
