package edu.text.model;


public class Text extends AbstractTextComposite<Paragraph> {

  private static final String PARAGRAPH_SEPARATOR = "\n\n";

  @Override
  protected String getSeparator() {
    return PARAGRAPH_SEPARATOR;
  }
}
