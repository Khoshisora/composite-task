package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Symbol;
import edu.text.model.TextComponent;


public class SymbolParser extends AbstractTextParser {

  @Override
  protected TextComponent parseComponent(String source) throws TextProcessingException {
    if (source.codePointCount(0, source.length()) != 1) {
      throw new TextProcessingException(
          "A symbol must contain exactly one code point: " + source);
    }
    return new Symbol(source);
  }
}
