module dev.swell.hangman {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.swell.hangman to javafx.fxml;
    exports dev.swell.hangman;
}