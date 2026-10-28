package edu.text.operation;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import java.util.Optional;


public interface TextOperation {

  String getTitle();

  default Optional<String> getParameterPrompt() {
    return Optional.empty();
  }

  String execute(Text text, Optional<String> parameter) throws TextProcessingException;
}
