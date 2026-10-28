package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Lexeme;
import edu.text.model.Symbol;
import edu.text.model.TextComponent;
import edu.text.model.Word;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexemeParser extends AbstractTextParser {

  private static final String WORD_REGEX =
      "[\\p{L}\\p{N}]+(?:['\u2019\\-\u2010\u2011][\\p{L}\\p{N}]+)*";
  private static final String LEXEME_TOKEN_REGEX = WORD_REGEX + "|\\S";
  private static final Pattern LEXEME_TOKEN_PATTERN = Pattern.compile(LEXEME_TOKEN_REGEX);
  private static final Pattern WORD_PATTERN = Pattern.compile(WORD_REGEX);

  @Override
  protected TextComponent parseComponent(String source) throws TextProcessingException {
    Lexeme lexeme = new Lexeme();
    Matcher matcher = LEXEME_TOKEN_PATTERN.matcher(source);
    while (matcher.find()) {
      String token = matcher.group();
      if (WORD_PATTERN.matcher(token).matches()) {
        lexeme.add(parseNext(token, Word.class));
      } else {
        lexeme.add(new Symbol(token));
      }
    }
    return lexeme;
  }
}
