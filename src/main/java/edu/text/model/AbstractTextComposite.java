package edu.text.model;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractTextComposite<T extends TextComponent> implements CompositeTextComponent {

  private final List<T> children = new ArrayList<>();

  protected abstract String getSeparator();

  public void add(T child) {
    if (child == null) {
      throw new IllegalArgumentException("Text component must not be null");
    }
    children.add(child);
  }

  @Override
  public List<T> getChildren() {
    return List.copyOf(children);
  }

  @Override
  public String getContent() {
    StringBuilder content = new StringBuilder();
    for (int index = 0; index < children.size(); index++) {
      if (index > 0) {
        content.append(getSeparator());
      }
      content.append(children.get(index).getContent());
    }
    return content.toString();
  }
}
