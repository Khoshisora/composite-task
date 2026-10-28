package edu.text.console;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import edu.text.operation.TextOperation;
import edu.text.service.TextStatistics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class ConsoleMenu {

  private static final Logger LOGGER = LogManager.getLogger(ConsoleMenu.class);
  private static final String EXIT_COMMAND = "0";
  private static final String STATISTICS_COMMAND = "s";

  private final BufferedReader input;
  private final PrintStream output;
  private final List<TextOperation> operations;
  private final TextStatistics statistics;

  public ConsoleMenu(BufferedReader input, PrintStream output, List<TextOperation> operations,
      TextStatistics statistics) {
    if (input == null) {
      throw new IllegalArgumentException("Console input must not be null");
    }
    if (output == null) {
      throw new IllegalArgumentException("Console output must not be null");
    }
    if (operations == null) {
      throw new IllegalArgumentException("Operations must not be null");
    }
    if (statistics == null) {
      throw new IllegalArgumentException("Statistics must not be null");
    }
    this.input = input;
    this.output = output;
    this.operations = List.copyOf(operations);
    this.statistics = statistics;
  }

  public void run(Text text) throws TextProcessingException {
    if (text == null) {
      throw new IllegalArgumentException("Text must not be null");
    }
    while (true) {
      printMenu();
      String choice = readLine("Choose an option: ");
      if (choice == null) {
        output.println("Input stream is closed. Exiting.");
        return;
      }
      String normalizedChoice = choice.strip();
      if (EXIT_COMMAND.equals(normalizedChoice)) {
        output.println("Goodbye.");
        return;
      }
      if (STATISTICS_COMMAND.equalsIgnoreCase(normalizedChoice)) {
        showStatistics();
        continue;
      }
      executeOperation(normalizedChoice, text);
    }
  }

  public void showStatistics() {
    output.println();
    output.println("Text statistics:");
    output.printf("  Letters: %d%n", statistics.letterCount());
    output.printf("  Symbols (non-letter, non-whitespace): %d%n", statistics.symbolCount());
    output.printf("  Characters (including whitespace): %d%n", statistics.characterCount());
  }

  private void printMenu() {
    output.println();
    output.println("Available operations:");
    for (int index = 0; index < operations.size(); index++) {
      output.printf("  %d. %s%n", index + 1, operations.get(index).getTitle());
    }
    output.println("  s. Show text statistics");
    output.println("  0. Exit");
  }

  private void executeOperation(String choice, Text text) {
    Optional<TextOperation> operation = findOperation(choice);
    if (operation.isEmpty()) {
      output.println("Unknown option: " + choice);
      return;
    }
    TextOperation selectedOperation = operation.get();
    try {
      Optional<String> parameter = readParameter(selectedOperation);
      String result = selectedOperation.execute(text, parameter);
      output.println();
      output.println(result);
    } catch (TextProcessingException e) {
      LOGGER.warn("Operation '{}' failed: {}", selectedOperation.getTitle(), e.getMessage());
      output.println("Cannot perform the operation: " + e.getMessage());
    }
  }

  private Optional<TextOperation> findOperation(String choice) {
    try {
      int index = Integer.parseInt(choice);
      if (index < 1 || index > operations.size()) {
        return Optional.empty();
      }
      return Optional.of(operations.get(index - 1));
    } catch (NumberFormatException e) {
      LOGGER.debug("Unrecognized menu input: {}", choice);
      return Optional.empty();
    }
  }

  private Optional<String> readParameter(TextOperation operation) throws TextProcessingException {
    Optional<String> prompt = operation.getParameterPrompt();
    if (prompt.isEmpty()) {
      return Optional.empty();
    }
    String value = readLine(prompt.get() + " ");
    if (value == null) {
      throw new TextProcessingException("Console input was closed");
    }
    return Optional.of(value);
  }

  private String readLine(String prompt) throws TextProcessingException {
    output.print(prompt);
    output.flush();
    try {
      return input.readLine();
    } catch (IOException e) {
      throw new TextProcessingException("Cannot read console input", e);
    }
  }
}
