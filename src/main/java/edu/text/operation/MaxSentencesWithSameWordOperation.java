package edu.text.operation;

import edu.text.model.Sentence;
import edu.text.model.Text;
import edu.text.model.Word;
import edu.text.service.TextStructure;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MaxSentencesWithSameWordOperation implements TextOperation {

  private static final Logger LOGGER = LogManager.getLogger(MaxSentencesWithSameWordOperation.class);

  @Override
  public String getTitle() {
    return "Find the maximum number of sentences that contain the same word";
  }

  @Override
  public String execute(Text text, Optional<String> parameter) {
    if (text == null) {
      throw new IllegalArgumentException("Text must not be null");
    }
    List<Sentence> sentences = TextStructure.collectSentences(text);
    if (sentences.isEmpty()) {
      return "The text does not contain any sentences.";
    }

    Map<String, Set<Sentence>> sentencesByWord = collectSentencesByWord(sentences);
    int maxSentenceCount = sentencesByWord.values().stream()
        .mapToInt(Set::size)
        .max()
        .orElse(0);
    LOGGER.debug("Maximum number of sentences with the same word: {}", maxSentenceCount);

    StringBuilder report = new StringBuilder()
        .append("Maximum number of sentences with the same word: ")
        .append(maxSentenceCount)
        .append(System.lineSeparator());
    if (maxSentenceCount <= 1) {
      return report.append("No word appears in more than one sentence.").toString();
    }

    List<String> words = sentencesByWord.entrySet().stream()
        .filter(entry -> entry.getValue().size() == maxSentenceCount)
        .map(Map.Entry::getKey)
        .sorted()
        .toList();
    for (String word : words) {
      report.append(System.lineSeparator())
          .append("Word \"")
          .append(word)
          .append("\" (")
          .append(maxSentenceCount)
          .append(" sentences):")
          .append(System.lineSeparator());
      int sentenceNumber = 1;
      for (Sentence sentence : sentencesByWord.get(word)) {
        report.append("  ")
            .append(sentenceNumber++)
            .append(". ")
            .append(sentence.getContent())
            .append(System.lineSeparator());
      }
    }
    return report.toString().stripTrailing();
  }

  private Map<String, Set<Sentence>> collectSentencesByWord(List<Sentence> sentences) {
    Map<String, Set<Sentence>> sentencesByWord = new LinkedHashMap<>();
    for (Sentence sentence : sentences) {
      for (Word word : TextStructure.collectWords(sentence)) {
        String key = word.getContent().toLowerCase(Locale.ROOT);
        sentencesByWord.computeIfAbsent(key, ignored -> new LinkedHashSet<>()).add(sentence);
      }
    }
    return sentencesByWord;
  }
}
