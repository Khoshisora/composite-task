package edu.text;

import edu.text.console.ConsoleMenu;
import edu.text.exception.TextProcessingException;
import edu.text.io.TextFileReader;
import edu.text.model.Text;
import edu.text.operation.MaxSentencesWithSameWordOperation;
import edu.text.operation.SentencesByLetterCountOperation;
import edu.text.operation.SwapFirstAndLastLexemeOperation;
import edu.text.operation.TextOperationRegistry;
import edu.text.parser.TextParserChain;
import edu.text.service.TextService;
import edu.text.service.TextStatistics;
import edu.text.service.TextStatisticsService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class TextAnalyzerApplication {

  private static final Logger LOGGER = LogManager.getLogger(TextAnalyzerApplication.class);
  private static final String DEFAULT_TEXT_RESOURCE = "text/sample.txt";

  private TextAnalyzerApplication() {
  }

  public static void main(String[] args) {
    LOGGER.info("Text analyzer application started");
    try {
      TextService textService = new TextService(new TextFileReader(), TextParserChain.getInstance());
      Text text = readText(textService, args);
      TextStatistics statistics = new TextStatisticsService().calculate(text);

      TextOperationRegistry registry = createOperationRegistry();
      ConsoleMenu menu = new ConsoleMenu(
          new BufferedReader(new InputStreamReader(System.in)),
          System.out,
          registry.getOperations(),
          statistics);
      menu.showStatistics();
      menu.run(text);
      LOGGER.info("Text analyzer application finished");
    } catch (TextProcessingException e) {
      LOGGER.error("Application failed: {}", e.getMessage(), e);
      System.err.println("Application failed: " + e.getMessage());
    }
  }

  private static Text readText(TextService textService, String[] args)
      throws TextProcessingException {
    if (args.length > 0 && !args[0].isBlank()) {
      Path path = toPath(args[0].strip());
      LOGGER.info("Reading text file: {}", path);
      return textService.read(path);
    }
    LOGGER.info("Reading bundled sample text: {}", DEFAULT_TEXT_RESOURCE);
    return textService.readResource(DEFAULT_TEXT_RESOURCE);
  }

  private static Path toPath(String value) throws TextProcessingException {
    try {
      return Path.of(value);
    } catch (InvalidPathException e) {
      throw new TextProcessingException("Invalid file path: " + value, e);
    }
  }

  private static TextOperationRegistry createOperationRegistry() {
    TextOperationRegistry registry = new TextOperationRegistry();
    registry.register(new MaxSentencesWithSameWordOperation());
    registry.register(new SentencesByLetterCountOperation());
    registry.register(new SwapFirstAndLastLexemeOperation());
    return registry;
  }
}
