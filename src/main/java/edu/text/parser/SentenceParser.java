package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Lexeme;
import edu.text.model.Sentence;
import edu.text.model.TextComponent;
import java.util.regex.Pattern;


public class SentenceParser extends AbstractTextParser {

  private static final String LEXEME_BOUNDARY_REGEX = "\\s+";
  private static final Pattern LEXEME_BOUNDARY_PATTERN = Pattern.compile(LEXEME_BOUNDARY_REGEX);

  @Override
  protected TextComponent parseComponent(String source) throws TextProcessingException {
    Sentence sentence = new Sentence();
    for (String lexemeSource : LEXEME_BOUNDARY_PATTERN.split(source)) {
      if (!lexemeSource.isBlank()) {
        sentence.add(parseNext(lexemeSource, Lexeme.class));
      }
    }
    return sentence;
  }
}
