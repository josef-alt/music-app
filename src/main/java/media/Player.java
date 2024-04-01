package media;

import java.io.File;
import java.util.*;
import java.util.stream.*;

import javafx.beans.value.ChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * Handles all operations associated with playing audio files.
 */
public class Player {
	private MediaPlayer mediaPlayer;
	private Library library;
	private Song currentSong;
	private File directory;

	private ArrayList<Integer> sequence;
	private boolean shuffled;

	// only for use during song transitions
	private boolean paused;
	private int songIndex;
	private int libraryLength;

	// TODO decide whether to keep this or not
	public Player(boolean shuffle) {
		this.paused = true;
		setDirectory(new File("."));
		this.shuffled = shuffle;
	}

	public void setDirectory(File dir) {
		this.directory = dir;
		this.library = new Library(dir.toPath());
		this.libraryLength = library.getNumberOfTracks();
		if (libraryLength > 0) {
			resetSequence();
			if (this.shuffled) {
				shuffle();
			}
		}
	}

	private void resetSequence() {
		sequence = IntStream.range(0, libraryLength).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
		songIndex = -1;
		nextSong();
	}

	public void shuffle() {
		this.shuffled = true;
		Collections.shuffle(sequence);
		songIndex = -1;
		nextSong();
	}

	public void inorder() {
		this.shuffled = false;
		resetSequence();
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

		currentSong = library.getTrack(sequence.get(songIndex));
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

		currentSong = library.getTrack(sequence.get(songIndex));
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

	public void seek(double time) {
		mediaPlayer.seek(Duration.seconds(time));
	}

	public void addTimeListener(ChangeListener<? super Duration> r) {
		mediaPlayer.currentTimeProperty().addListener(r);
	}

	private ArrayList<Runnable> listeners = new ArrayList<>();

	public void addListener(Runnable e) {
		listeners.add(e);
	}

	private void notifyListeners() {
		for (Runnable event : listeners) {
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

	public String getDirectory() {
		System.out.println(directory + " " + directory.getPath() + " " + directory.getAbsolutePath());
		return directory.getAbsolutePath();
	}

	public LibraryStats getStats() {
		return library.getStats();
	}

	public boolean getShuffled() {
		return shuffled;
	}
}
