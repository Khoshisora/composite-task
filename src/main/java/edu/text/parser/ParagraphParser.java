package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Paragraph;
import edu.text.model.Sentence;
import edu.text.model.TextComponent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParagraphParser extends AbstractTextParser {

  private static final String SENTENCE_END_REGEX = "[.!?…]+[\"'»”’)\\]]*(?=\\s|$)";
  private static final Pattern SENTENCE_END_PATTERN = Pattern.compile(SENTENCE_END_REGEX);

  @Override
  protected TextComponent parseComponent(String source) throws TextProcessingException {
    Paragraph paragraph = new Paragraph();
    Matcher matcher = SENTENCE_END_PATTERN.matcher(source);
    int sentenceStart = 0;
    while (matcher.find()) {
      addSentence(paragraph, source.substring(sentenceStart, matcher.end()));
      sentenceStart = matcher.end();
    }
    addSentence(paragraph, source.substring(sentenceStart));
    return paragraph;
  }

  private void addSentence(Paragraph paragraph, String sentenceSource)
      throws TextProcessingException {
    if (!sentenceSource.isBlank()) {
      paragraph.add(parseNext(sentenceSource, Sentence.class));
    }
  }
}
