package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Paragraph;
import edu.text.model.Text;
import edu.text.model.TextComponent;
import java.util.regex.Pattern;


public class TextParser extends AbstractTextParser {

  private static final String PARAGRAPH_BREAK_REGEX = "\\R\\s*\\R+";
  private static final Pattern PARAGRAPH_BREAK_PATTERN = Pattern.compile(PARAGRAPH_BREAK_REGEX);

  @Override
  protected TextComponent parseComponent(String source) throws TextProcessingException {
    Text text = new Text();
    for (String paragraphSource : PARAGRAPH_BREAK_PATTERN.split(source)) {
      if (!paragraphSource.isBlank()) {
        text.add(parseNext(paragraphSource, Paragraph.class));
      }
    }
    return text;
  }
}
