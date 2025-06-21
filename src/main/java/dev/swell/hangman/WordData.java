package dev.swell.hangman;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WordData{

    private StringProperty word = new SimpleStringProperty();
    private BooleanProperty editable = new SimpleBooleanProperty(false);

    public WordData(String character, boolean disabled){
         word.set(character);
         this.editable.set(disabled);
    }

    public String getWord() {
        return word.get();
    }

    public StringProperty wordProperty() {
        return word;
    }

    public boolean getEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }



}
