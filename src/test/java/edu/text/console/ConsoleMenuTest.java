package edu.text.console;

import static org.assertj.core.api.Assertions.assertThat;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.operation.TextOperation;
import edu.text.parser.TextParserChain;
import edu.text.service.TextStatistics;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ConsoleMenuTest {

  private final TextParserChain chain = TextParserChain.getInstance();
  private final TextStatistics statistics = new TextStatistics(1, 2, 3);

  @Test
  void shouldExecuteSelectedOperationAndExit() throws TextProcessingException {
    // given
    TextOperation operation = new StubOperation("Test operation", Optional.empty(), "result");
    MenuResult menuResult = runMenu("1\n0\n", operation);
    // then
    assertThat(menuResult.output())
        .contains("1. Test operation")
        .contains("result")
        .contains("Goodbye.");
  }

  @Test
  void shouldPassParameterToOperation() throws TextProcessingException {
    // given
    TextOperation operation =
        new StubOperation("Parameterized operation", Optional.of("Enter value:"), null);
    MenuResult menuResult = runMenu("1\nabc\n0\n", operation);
    // then
    assertThat(menuResult.output())
        .contains("Enter value:")
        .contains("abc");
  }

  @Test
  void shouldReportUnknownOption() throws TextProcessingException {
    // given
    TextOperation operation = new StubOperation("Test operation", Optional.empty(), "result");
    MenuResult menuResult = runMenu("99\n0\n", operation);
    // then
    assertThat(menuResult.output()).contains("Unknown option: 99");
  }

  @Test
  void shouldShowStatistics() throws TextProcessingException {
    // given
    TextOperation operation = new StubOperation("Test operation", Optional.empty(), "result");
    MenuResult menuResult = runMenu("s\n0\n", operation);
    // then
    assertThat(menuResult.output())
        .contains("Letters: 1")
        .contains("Symbols (non-letter, non-whitespace): 2")
        .contains("Characters (including whitespace): 3");
  }

  @Test
  void shouldExitWhenInputIsClosed() throws TextProcessingException {
    // given
    TextOperation operation = new StubOperation("Test operation", Optional.empty(), "result");
    MenuResult menuResult = runMenu("", operation);
    // then
    assertThat(menuResult.output()).contains("Input stream is closed. Exiting.");
  }

  private MenuResult runMenu(String input, TextOperation operation) throws TextProcessingException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try (PrintStream printStream =
        new PrintStream(output, true, StandardCharsets.UTF_8)) {
      ConsoleMenu menu = new ConsoleMenu(
          new BufferedReader(new StringReader(input)),
          printStream,
          List.of(operation),
          statistics);
      menu.run(chain.parse("Text."));
    }
    return new MenuResult(output.toString(StandardCharsets.UTF_8));
  }

  private record MenuResult(String output) {
  }

  private static final class StubOperation implements TextOperation {

    private final String title;
    private final Optional<String> prompt;
    private final String fixedResult;

    private StubOperation(String title, Optional<String> prompt, String fixedResult) {
      this.title = title;
      this.prompt = prompt;
      this.fixedResult = fixedResult;
    }

    @Override
    public String getTitle() {
      return title;
    }

    @Override
    public Optional<String> getParameterPrompt() {
      return prompt;
    }

    @Override
    public String execute(Text text, Optional<String> parameter) {
      if (fixedResult == null) {
        return parameter.orElse("no parameter");
      }
      return fixedResult;
    }
  }
}
