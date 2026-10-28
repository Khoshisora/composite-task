package edu.text.model;


public class Word extends AbstractTextComposite<Symbol> {

  private static final String SYMBOL_SEPARATOR = "";

  @Override
  protected String getSeparator() {
    return SYMBOL_SEPARATOR;
  }
}
