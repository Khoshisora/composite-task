package edu.text.parser;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.model.TextComponent;


public final class TextParserChain {

  private static final TextParserChain INSTANCE = new TextParserChain();

  private final TextParser textParser;

  private TextParserChain() {
    SymbolParser symbolParser = new SymbolParser();
    WordParser wordParser = new WordParser();
    LexemeParser lexemeParser = new LexemeParser();
    SentenceParser sentenceParser = new SentenceParser();
    ParagraphParser paragraphParser = new ParagraphParser();

    textParser = new TextParser();
    textParser
        .setNext(paragraphParser)
        .setNext(sentenceParser)
        .setNext(lexemeParser)
        .setNext(wordParser)
        .setNext(symbolParser);
  }

  public static TextParserChain getInstance() {
    return INSTANCE;
  }

  public Text parse(String source) throws TextProcessingException {
    TextComponent component = textParser.parse(source);
    if (!(component instanceof Text text)) {
      throw new TextProcessingException("Parser chain did not produce a text component");
    }
    return text;
  }
}
