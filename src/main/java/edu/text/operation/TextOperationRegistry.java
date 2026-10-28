package edu.text.operation;

import java.util.ArrayList;
import java.util.List;


public final class TextOperationRegistry {

  private final List<TextOperation> operations = new ArrayList<>();

  public void register(TextOperation operation) {
    if (operation == null) {
      throw new IllegalArgumentException("Operation must not be null");
    }
    operations.add(operation);
  }

  public List<TextOperation> getOperations() {
    return List.copyOf(operations);
  }
}
