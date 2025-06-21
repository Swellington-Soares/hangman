package dev.swell.hangman;

import java.util.*;
import java.util.stream.Collectors;

public class HandgmanWord {

    private String completeWord;
    private final int changeMax = 6;

    private int currentChange = 0;

    private final String[] gameWord = new String[5];

    private final INotifyEvent eventCallback;

    private final WordData[] characterList = {
            new WordData(null, false),
            new WordData(null, false),
            new WordData(null, false),
            new WordData(null, false),
            new WordData(null, false),
    };

    public WordData[] getCharacterList() {
        return characterList;
    }

    private boolean gameStarted = false;

    public void newGame(String word) {
        System.out.println("New game started");
        System.out.println("New word: " + word);
        gameStarted = false;
        completeWord = word.toUpperCase();
        currentChange = 0;
        process();
    }

    public HandgmanWord(INotifyEvent notifyEvent) {
        this.eventCallback =  notifyEvent;
        bindAll();
    }

    private boolean isCompleteWordCorrectly() {
        return Arrays.stream(characterList)
                .map(WordData::getWord)
                .map(String::trim)
                .collect(Collectors.joining(""))
                .trim()
                .equalsIgnoreCase(completeWord);
    }

    private void bindAll() {
        for (int i = 0; i < characterList.length; i++) {
            int finalI = i;
            characterList[i].wordProperty().addListener((observable, oldValue, newValue) -> {
                if (!gameStarted || newValue.trim().isEmpty() || (oldValue.trim().equalsIgnoreCase(newValue)) ) return;

                if (isCompleteWordCorrectly()) {
                    for (WordData wordData : characterList) {
                        wordData.editableProperty().set(false);
                    }
                    NotifyEvent(GameEvent.VICTORY, finalI);
                    gameStarted = false;
                    return;
                }

                if (newValue.equalsIgnoreCase(gameWord[finalI])) {
                    NotifyEvent(GameEvent.OK, finalI);
                } else {
                    currentChange++;
                    if (currentChange >= changeMax) {
                        for (WordData wordData : characterList) {
                            wordData.editableProperty().set(false);
                        }
                        NotifyEvent(GameEvent.FINISH, finalI);
                        gameStarted = false;
                    } else {
                       NotifyEvent(GameEvent.ERROR, finalI);
                    }
                }
            });
        }
    }

    private void NotifyEvent(GameEvent event, int finalI) {
        if (eventCallback != null) {
            eventCallback.Notify(event, finalI);
        }
    }

    private String maskWord(String palavra) {
        Random random = new Random();
        int amountToRemove = random.nextInt(4) + 1;
        int tamanho = palavra.length();

        amountToRemove = Math.min(amountToRemove, tamanho);


        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);
        Set<Integer> positionToRemove = new HashSet<>(indexes.subList(0, amountToRemove));


        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            if (positionToRemove.contains(i)) {
                resultado.append(' ');
            } else {
                resultado.append(palavra.charAt(i));
            }
        }

        return resultado.toString();
    }

    private void process() {
        var word = maskWord(completeWord);
        for (int i = 0; i < word.length(); i++) {
            gameWord[i] = completeWord.substring(i, i + 1);
            var chMasked = word.charAt(i);
            var editable = chMasked == ' ';
            characterList[i].wordProperty().set(editable ? "" : String.valueOf(chMasked));
            characterList[i].editableProperty().set(editable);
        }
        gameStarted = true;
    }

}
