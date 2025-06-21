package dev.swell.hangman;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.*;
import java.util.stream.IntStream;

public class HelloController implements INotifyEvent {

    private int imageFrameIndex = 0;

    private final List<String> palavras = Arrays.asList(
            "Amigo",
            "Bicho",
            "Dente",
            "Farol",
            "Garfo",
            "Hotel",
            "Jarda",
            "Livro",
            "Massa",
            "Nuvem",
            "Pente",
            "Queda",
            "Rosto",
            "Sinal",
            "Torre",
            "Vidro",
            "Zebra",
            "Caixa",
            "Noite"
    );

    private Image[] imageFrames;

    private final HandgmanWord handgmanWord = new HandgmanWord(this);

    @FXML
    private ImageView imageGame;

    @FXML
    private Text finishText;

    @FXML
    private Text victoryText;

    @FXML
    private TextField wordText1;

    @FXML
    private TextField wordText2;

    @FXML
    private TextField wordText3;

    @FXML
    private TextField wordText4;

    @FXML
    private TextField wordText5;


    @FXML
    void onNewGameButtonAction() {
        imageFrameIndex = 0;
        resetUI();
        Collections.shuffle(palavras);
        handgmanWord.newGame(palavras.get(new Random().nextInt(palavras.size())));
    }

    private void resetUI() {
        imageGame.setVisible(true);
        finishText.setVisible(false);
        victoryText.setVisible(false);
        wordText1.getStyleClass().removeAll("success", "error");
        wordText2.getStyleClass().removeAll("success", "error");
        wordText3.getStyleClass().removeAll("success", "error");
        wordText4.getStyleClass().removeAll("success", "error");
        wordText5.getStyleClass().removeAll("success", "error");
        imageGame.setImage(imageFrames[imageFrameIndex]);
    }


    private void incrementFrameImage() {
        if (imageFrameIndex >= imageFrames.length ) return;
        imageFrameIndex++;
        imageGame.setImage(imageFrames[imageFrameIndex]);
    }

    @FXML
    void initialize() {
        finishText.setVisible(false);
        victoryText.setVisible(false);
        wordText1.editableProperty().bind(handgmanWord.getCharacterList()[0].editableProperty());
        wordText2.editableProperty().bind(handgmanWord.getCharacterList()[1].editableProperty());
        wordText3.editableProperty().bind(handgmanWord.getCharacterList()[2].editableProperty());
        wordText4.editableProperty().bind(handgmanWord.getCharacterList()[3].editableProperty());
        wordText5.editableProperty().bind(handgmanWord.getCharacterList()[4].editableProperty());

        wordText1.textProperty().bindBidirectional(handgmanWord.getCharacterList()[0].wordProperty());
        wordText2.textProperty().bindBidirectional(handgmanWord.getCharacterList()[1].wordProperty());
        wordText3.textProperty().bindBidirectional(handgmanWord.getCharacterList()[2].wordProperty());
        wordText4.textProperty().bindBidirectional(handgmanWord.getCharacterList()[3].wordProperty());
        wordText5.textProperty().bindBidirectional(handgmanWord.getCharacterList()[4].wordProperty());

        limitTextField(wordText1);
        limitTextField(wordText2);
        limitTextField(wordText3);
        limitTextField(wordText4);
        limitTextField(wordText5);

        imageGame.setVisible(false);

        loadResources();
    }

    private void loadResources() {
        imageFrames = IntStream.rangeClosed(1, 7)
                .mapToObj(i -> {
                    var path = "images/frame" + i + ".png";
                    var stream = getClass().getResourceAsStream(path);
                    if (stream == null) {
                        System.err.println("Imagem não encontrada: " + path);
                        return null; // ou imagem padrão
                    }
                    return new Image(stream);
                })
                .toArray(Image[]::new);
    }

    private void limitTextField(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() <= 1 ? change : null));
    }

    @Override
    public void Notify(GameEvent gameEvent, Integer position) {


        if (gameEvent == GameEvent.FINISH || gameEvent == GameEvent.VICTORY) {
            FinishGame(gameEvent);
            return;
        }

        var styleClass = gameEvent == GameEvent.OK ? "success" : "error";

        if (gameEvent == GameEvent.ERROR) {
            incrementFrameImage();
        }

        switch (position) {
            case 0: {
                wordText1.getStyleClass().removeAll("success", "error");
                wordText1.getStyleClass().add(styleClass);
                break;
            }
            case 1: {
                wordText2.getStyleClass().removeAll("success", "error");
                wordText2.getStyleClass().add(styleClass);
                break;
            }
            case 2: {
                wordText3.getStyleClass().removeAll("success", "error");
                wordText3.getStyleClass().add(styleClass);
                break;
            }
            case 3: {
                wordText4.getStyleClass().removeAll("success", "error");
                wordText4.getStyleClass().add(styleClass);
                break;
            }
            case 4: {
                wordText5.getStyleClass().removeAll("success", "error");
                wordText5.getStyleClass().add(styleClass);
            }
        }
    }

    private void FinishGame(GameEvent gameEvent) {
        if (gameEvent == GameEvent.VICTORY) {
            victoryText.setVisible(true);
        } else if (gameEvent == GameEvent.FINISH) {
            incrementFrameImage();
            finishText.setVisible(true);
        }

        wordText1.setText(wordText1.getText().toUpperCase());
        wordText2.setText(wordText2.getText().toUpperCase());
        wordText3.setText(wordText3.getText().toUpperCase());
        wordText4.setText(wordText4.getText().toUpperCase());
        wordText5.setText(wordText5.getText().toUpperCase());

        wordText1.getStyleClass().removeAll("success", "error");
        wordText2.getStyleClass().removeAll("success", "error");
        wordText3.getStyleClass().removeAll("success", "error");
        wordText4.getStyleClass().removeAll("success", "error");
        wordText5.getStyleClass().removeAll("success", "error");

        wordText1.getStyleClass().add(gameEvent == GameEvent.VICTORY ? "success" : "error");
        wordText2.getStyleClass().add(gameEvent == GameEvent.VICTORY ? "success" : "error");
        wordText3.getStyleClass().add(gameEvent == GameEvent.VICTORY ? "success" : "error");
        wordText4.getStyleClass().add(gameEvent == GameEvent.VICTORY ? "success" : "error");
        wordText5.getStyleClass().add(gameEvent == GameEvent.VICTORY ? "success" : "error");

    }
}