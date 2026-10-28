package edu.text.service;

import edu.text.model.Lexeme;
import edu.text.model.Paragraph;
import edu.text.model.Sentence;
import edu.text.model.Text;
import edu.text.model.TextComponent;
import edu.text.model.Word;
import java.util.ArrayList;
import java.util.List;


public final class TextStructure {

  private TextStructure() {
  }

  public static List<Sentence> collectSentences(Text text) {
    if (text == null) {
      throw new IllegalArgumentException("Text must not be null");
    }
    List<Sentence> sentences = new ArrayList<>();
    for (Paragraph paragraph : text.getChildren()) {
      sentences.addAll(paragraph.getChildren());
    }
    return List.copyOf(sentences);
  }

  public static List<Word> collectWords(Sentence sentence) {
    if (sentence == null) {
      throw new IllegalArgumentException("Sentence must not be null");
    }
    List<Word> words = new ArrayList<>();
    for (Lexeme lexeme : sentence.getChildren()) {
      for (TextComponent part : lexeme.getChildren()) {
        if (part instanceof Word word) {
          words.add(word);
        }
      }
    }
    return List.copyOf(words);
  }
}
