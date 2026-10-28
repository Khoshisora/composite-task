package edu.text.operation;

import edu.text.model.Lexeme;
import edu.text.model.Paragraph;
import edu.text.model.Sentence;
import edu.text.model.Text;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SwapFirstAndLastLexemeOperation implements TextOperation {

  private static final Logger LOGGER =
      LogManager.getLogger(SwapFirstAndLastLexemeOperation.class);

  @Override
  public String getTitle() {
    return "Swap the first and the last lexeme in every sentence";
  }

  @Override
  public String execute(Text text, Optional<String> parameter) {
    if (text == null) {
      throw new IllegalArgumentException("Text must not be null");
    }
    Text swappedText = new Text();
    int swappedSentences = 0;
    for (Paragraph paragraph : text.getChildren()) {
      Paragraph swappedParagraph = new Paragraph();
      for (Sentence sentence : paragraph.getChildren()) {
        if (sentence.getChildren().size() > 1) {
          swappedSentences++;
        }
        swappedParagraph.add(swapLexemes(sentence));
      }
      swappedText.add(swappedParagraph);
    }
    LOGGER.debug("Swapped lexemes in {} sentences", swappedSentences);
    return swappedText.getContent();
  }

  private Sentence swapLexemes(Sentence sentence) {
    List<Lexeme> lexemes = sentence.getChildren();
    Sentence swappedSentence = new Sentence();
    if (lexemes.size() < 2) {
      for (Lexeme lexeme : lexemes) {
        swappedSentence.add(lexeme);
      }
      return swappedSentence;
    }
    swappedSentence.add(lexemes.get(lexemes.size() - 1));
    for (int index = 1; index < lexemes.size() - 1; index++) {
      swappedSentence.add(lexemes.get(index));
    }
    swappedSentence.add(lexemes.get(0));
    return swappedSentence;
  }
}
