package edu.text.model;

import java.util.List;


public interface CompositeTextComponent extends TextComponent {

  List<? extends TextComponent> getChildren();
}
