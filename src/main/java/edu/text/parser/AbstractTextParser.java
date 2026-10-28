package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.TextComponent;


public abstract class AbstractTextParser {

  private AbstractTextParser next;

  public AbstractTextParser setNext(AbstractTextParser next) {
    if (next == null) {
      throw new IllegalArgumentException("Next parser must not be null");
    }
    this.next = next;
    return next;
  }

  public final TextComponent parse(String source) throws TextProcessingException {
    if (source == null || source.isBlank()) {
      throw new TextProcessingException("Cannot parse blank text");
    }
    return parseComponent(source.strip());
  }

  protected abstract TextComponent parseComponent(String source) throws TextProcessingException;

  protected final <T extends TextComponent> T parseNext(String source, Class<T> type)
      throws TextProcessingException {
    if (next == null) {
      throw new TextProcessingException(
          "Parser chain is not configured: no next parser after " + getClass().getSimpleName());
    }
    TextComponent component = next.parse(source);
    if (!type.isInstance(component)) {
      throw new TextProcessingException(
          "Parser chain is misconfigured: expected " + type.getSimpleName()
              + " but got " + component.getClass().getSimpleName());
    }
    return type.cast(component);
  }
}
