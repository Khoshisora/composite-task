package edu.text.model;


public final class Symbol implements TextComponent {

  private final String value;

  public Symbol(String value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("Symbol value must not be empty");
    }
    if (value.codePointCount(0, value.length()) != 1) {
      throw new IllegalArgumentException("Symbol must contain exactly one code point: " + value);
    }
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String getContent() {
    return value;
  }
}
