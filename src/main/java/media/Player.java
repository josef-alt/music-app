package media;

import java.io.*;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * Handles all operations associated with playing audio files.
 */
public class Player {
	private MediaPlayer mediaPlayer;
	private Library library;
	private Song currentSong;

	// only for use during song transitions
	private boolean paused;
	private int songIndex;
	private int libraryLength;

	// TODO decide whether to keep this or not
	public Player() {
		this.paused = true;
		setDirectory(new File("."));
	}

	public void setDirectory(File dir) {
		this.library = new Library(dir.toPath());
		this.libraryLength = library.getNumberOfTracks();
		if (libraryLength > 0) {
			System.out.println("Successfully loaded:");
			System.out.println(library.toString());

			songIndex = -1;
			nextSong();
		}
	}

	/**
	 * Handle overhead for song transitions.
	 * Stop current player - if active
	 * Set up player for new song
	 * Play if the previous track was playing
	 */
	private void changeSongs() {
		if (mediaPlayer != null)
			mediaPlayer.dispose();

		// recreate player and reconfigure autoplay
		mediaPlayer = new MediaPlayer(new Media(currentSong.getFile().toURI().toString()));
		mediaPlayer.setOnEndOfMedia(() -> nextSong());

		if (!paused) {
			mediaPlayer.play();
		}
		notifyListeners();
	}

	/**
	 * TODO nextSong & prevSong refactoring
	 * 
	 * Move to next song sequentially in library or cycle back to beginning
	 */
	public void nextSong() {
		if (libraryLength == 0) {
			return;
		}
		songIndex = (songIndex + 1) % libraryLength;

		currentSong = library.getTrack(songIndex);
		changeSongs();
	}

	/**
	 * Move to sequentially prior song, looping back around at beginning.
	 */
	public void prevSong() {
		if (libraryLength == 0) {
			return;
		}
		songIndex = (songIndex - 1 + libraryLength) % libraryLength;

		currentSong = library.getTrack(songIndex);
		changeSongs();
	}

	public Song getSong() {
		return currentSong;
	}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * Pause/Resume play and update paused field for inter-song
	 * transitions.
	 * TODO - rename?
	 */
	public boolean pause() {
		if (mediaPlayer == null) {
			System.err.println("Cannot play/pause with no player active");
			return false;
		}
		if (mediaPlayer.getStatus() == Status.READY) {
			mediaPlayer.play();
			paused = false;
		} else if (mediaPlayer.getStatus() == Status.PAUSED) {
			mediaPlayer.play();
			paused = false;
		} else {
			mediaPlayer.pause();
			paused = true;
		}
		return paused;
	}

	public void resume() {
		mediaPlayer.play();
	}

	private ArrayList<Runnable> events = new ArrayList<>();

	public void addEvent(Runnable e) {
		events.add(e);
	}

	private void notifyListeners() {
		for (Runnable event : events) {
			event.run();
		}
	}

	/**
	 * Shuts down the media player, if it is in use.
	 */
	public void quit() {
		if (mediaPlayer != null) {
			if (mediaPlayer.getStatus() == Status.PLAYING) {
				mediaPlayer.stop();
			}
		}
	}
}
