package org.example;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private Clip bgMusic;
    private String[] playlist;
    private int currentSongIndex = 0;
    private boolean isHyped = false;

    // Load multiple files
    public void loadPlaylist(String[] filePaths) {
        this.playlist = filePaths;
    }

    public void updateMusicState(boolean shouldPlay) {
        this.isHyped = shouldPlay;

        if (shouldPlay) {
            if (bgMusic == null || !bgMusic.isRunning()) {
                playCurrentSong();
            }
        } else {
            if (bgMusic != null && bgMusic.isRunning()) {
                bgMusic.stop();
            }
        }
    }

    private void playCurrentSong() {
        try {
            // If something is already playing, clear it out
            if (bgMusic != null) {
                bgMusic.close();
            }

            File file = new File(playlist[currentSongIndex]);
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            bgMusic = AudioSystem.getClip();
            bgMusic.open(stream);

            // THE SENSOR: Detect when the song ends
            bgMusic.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && isHyped) {
                    // Only move to next song if the player is still clicking fast!
                    nextSong();
                }
            });

            bgMusic.start();
        } catch (Exception e) {
            System.out.println("Playlist Error: " + e.getMessage());
        }
    }

    private void nextSong() {
        currentSongIndex++;
        // If we reach the end of the list, go back to the first song (0)
        if (currentSongIndex >= playlist.length) {
            currentSongIndex = 0;
        }
        playCurrentSong();
    }
}