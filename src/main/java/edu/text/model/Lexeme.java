package edu.text.model;


public class Lexeme extends AbstractTextComposite<TextComponent> {

  private static final String PART_SEPARATOR = "";

  @Override
  protected String getSeparator() {
    return PART_SEPARATOR;
  }
}
