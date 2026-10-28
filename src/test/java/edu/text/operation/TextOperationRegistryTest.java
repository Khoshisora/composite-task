package edu.text.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.text.exception.TextProcessingException;
import edu.text.model.Text;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TextOperationRegistryTest {

  @Test
  void shouldPreserveRegistrationOrder() {
    // given
    TextOperationRegistry registry = new TextOperationRegistry();
    TextOperation first = new StubOperation("first");
    TextOperation second = new StubOperation("second");
    // when
    registry.register(first);
    registry.register(second);
    // then
    assertThat(registry.getOperations()).containsExactly(first, second);
  }

  @Test
  void shouldReturnImmutableSnapshot() {
    // given
    TextOperationRegistry registry = new TextOperationRegistry();
    registry.register(new StubOperation("operation"));
    // when
    List<TextOperation> operations = registry.getOperations();
    // then
    assertThatThrownBy(() -> operations.add(new StubOperation("another")))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void shouldRejectNullOperation() {
    TextOperationRegistry registry = new TextOperationRegistry();
    assertThatThrownBy(() -> registry.register(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldBeEmptyAfterCreation() {
    assertThat(new TextOperationRegistry().getOperations()).isEmpty();
  }

  private static final class StubOperation implements TextOperation {

    private final String title;

    private StubOperation(String title) {
      this.title = title;
    }

    @Override
    public String getTitle() {
      return title;
    }

    @Override
    public String execute(Text text, Optional<String> parameter) throws TextProcessingException {
      return title;
    }
  }
}
