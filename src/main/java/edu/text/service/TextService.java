package edu.text.service;

import edu.text.exception.TextProcessingException;
import edu.text.io.TextFileReader;
import edu.text.model.Text;
import edu.text.parser.TextParserChain;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TextService {

  private static final Logger LOGGER = LogManager.getLogger(TextService.class);

  private final TextFileReader reader;
  private final TextParserChain parserChain;

  public TextService(TextFileReader reader, TextParserChain parserChain) {
    if (reader == null) {
      throw new IllegalArgumentException("Text reader must not be null");
    }
    if (parserChain == null) {
      throw new IllegalArgumentException("Parser chain must not be null");
    }
    this.reader = reader;
    this.parserChain = parserChain;
  }

  public Text read(Path path) throws TextProcessingException {
    return parse(reader.read(path));
  }

  public Text readResource(String resourceName) throws TextProcessingException {
    return parse(reader.readResource(resourceName));
  }

  public Text parse(String source) throws TextProcessingException {
    if (source == null || source.isBlank()) {
      throw new TextProcessingException("Text must not be blank");
    }
    LOGGER.debug("Parsing text of {} characters", source.length());
    Text text = parserChain.parse(source);
    LOGGER.info("Text parsed into {} paragraphs", text.getChildren().size());
    return text;
  }
}
