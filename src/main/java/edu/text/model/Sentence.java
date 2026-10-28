package edu.text.model;


public class Sentence extends AbstractTextComposite<Lexeme> {

  private static final String LEXEME_SEPARATOR = " ";

  @Override
  protected String getSeparator() {
    return LEXEME_SEPARATOR;
  }
}
