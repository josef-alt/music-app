package media;

import java.io.IOException;
import java.nio.file.PathMatcher;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains everything necessary for managing the music library.
 * 
 * TODO playlists, filtering, genre/artist/album/etc
 */
public class Library {
	private Song[] files;
	private Path directory;
	private LibraryStats stats;
	private ObservableList<Playlist> playlists;

	// only supporting m4a files for now
	private static final PathMatcher matcher = FileSystems.getDefault()
			.getPathMatcher("glob:**.{aif,aiff,mp3,mp4,m4a,wav}");

	public Library(String dir) {
		this(Path.of(dir));
	}

	public Library(Path dir) {
		this.stats = new LibraryStats();
		playlists = FXCollections.observableArrayList();
		setDirectory(dir);
	}

	public void setDirectory(Path dir) {
		this.directory = dir;

		try {
			loadFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.files = new Song[0];
		}
	}

	/**
	 * Attempts to load files from selected directory
	 * Filters out unsupported types
	 */
	private void loadFiles() throws IOException {
		stats.clear();// = new LibraryStats();
		ArrayList<Song> songs = new ArrayList<>();

		if (Files.isDirectory(directory)) {
			for (Path path : Files.newDirectoryStream(directory)) {
				if (matcher.matches(path)) {
					Song newSong = new Song(path.toFile());
					stats.addSong(newSong);
					songs.add(newSong);
				}
			}
		} else if (Files.isRegularFile(directory)) {
			if (matcher.matches(directory)) {
				songs.add(new Song(directory.toFile()));
				stats.addSong(songs.get(0));
			}
		}
		this.files = songs.toArray(Song[]::new);

		// TODO playlist support
		playlists.clear();
		playlists.add(new Playlist("All Songs", songs));
	}

	public int getNumberOfTracks() {
		return files.length;
	}

	public Song getTrack(int trackNumber) {
		return files[trackNumber];
	}

	public LibraryStats getStats() {
		return stats;
	}

	public ObservableList<Playlist> getAllPlaylists() {
		return playlists;
	}

	public String toString() {
		String list = Arrays.stream(files).map(Song::toString).collect(Collectors.joining(", ", "{", "}"));

		return String.format("%s has %d files: %s", directory.toAbsolutePath().toString(), files.length, list);
	}
}
