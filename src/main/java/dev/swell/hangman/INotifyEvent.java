package dev.swell.hangman;

public interface INotifyEvent {
    void Notify(GameEvent gameEvent, Integer position);
}
