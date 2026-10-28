package edu.text.model;


public class Paragraph extends AbstractTextComposite<Sentence> {

  private static final String SENTENCE_SEPARATOR = " ";

  @Override
  protected String getSeparator() {
    return SENTENCE_SEPARATOR;
  }
}
