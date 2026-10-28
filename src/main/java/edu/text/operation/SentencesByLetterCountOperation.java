package edu.text.operation;

import edu.text.exception.TextProcessingException;
import edu.text.model.Sentence;
import edu.text.model.Text;
import edu.text.service.TextStructure;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SentencesByLetterCountOperation implements TextOperation {

  private static final Logger LOGGER =
      LogManager.getLogger(SentencesByLetterCountOperation.class);
  private static final String PARAMETER_PROMPT = "Enter a letter to count occurrences:";

  @Override
  public String getTitle() {
    return "Print sentences in ascending order of a given letter count";
  }

  @Override
  public Optional<String> getParameterPrompt() {
    return Optional.of(PARAMETER_PROMPT);
  }

  @Override
  public String execute(Text text, Optional<String> parameter) throws TextProcessingException {
    if (text == null) {
      throw new IllegalArgumentException("Text must not be null");
    }
    int letter = parseLetter(parameter);
    List<Sentence> sentences = new ArrayList<>(TextStructure.collectSentences(text));
    if (sentences.isEmpty()) {
      return "The text does not contain any sentences.";
    }
    sentences.sort(Comparator.comparingInt(sentence -> countLetter(sentence, letter)));

    StringBuilder report = new StringBuilder("Sentences ordered by ascending count of letter '")
        .appendCodePoint(letter)
        .append("':")
        .append(System.lineSeparator());
    int position = 1;
    for (Sentence sentence : sentences) {
      report.append(String.format("%2d. [%d] %s%n",
          position++, countLetter(sentence, letter), sentence.getContent()));
    }
    LOGGER.debug("Sentences ordered by letter '{}'", new String(Character.toChars(letter)));
    return report.toString().stripTrailing();
  }

  private int parseLetter(Optional<String> parameter) throws TextProcessingException {
    String raw = parameter.orElseThrow(
        () -> new TextProcessingException("A letter parameter is required"));
    String value = raw.strip();
    if (value.isEmpty() || value.codePointCount(0, value.length()) != 1) {
      throw new TextProcessingException("Exactly one letter is expected but got: " + raw);
    }
    int codePoint = value.codePointAt(0);
    if (!Character.isLetter(codePoint)) {
      throw new TextProcessingException("The parameter must be a letter but got: " + raw);
    }
    return codePoint;
  }

  private int countLetter(Sentence sentence, int letter) {
    int lowerCaseLetter = Character.toLowerCase(letter);
    return (int) sentence.getContent().codePoints()
        .filter(codePoint -> Character.toLowerCase(codePoint) == lowerCaseLetter)
        .count();
  }
}
