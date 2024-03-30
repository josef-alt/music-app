package media;

import java.io.IOException;
import java.nio.file.PathMatcher;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Contains everything necessary for managing the music library.
 * 
 * TODO playlists, filtering, genre/artist/album/etc
 */
public class Library {
	private Song[] files;
	private Path directory;
	private LibraryStats stats;

	// only supporting m4a files for now
	private static final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.{m4a,mp3,wav}");

	public Library(String dir) {
		this(Path.of(dir));
	}

	public Library(Path dir) {
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
		stats = new LibraryStats();
		if (Files.isDirectory(directory)) {
			ArrayList<Song> songs = new ArrayList<>();
			for (Path path : Files.newDirectoryStream(directory)) {
				if (matcher.matches(path)) {
					Song newSong = new Song(path.toFile());
					stats.addSong(newSong);
					songs.add(newSong);
				}
			}
			this.files = songs.toArray(Song[]::new);
		} else if (Files.isRegularFile(directory)) {
			if (matcher.matches(directory)) {
				this.files = new Song[] { new Song(directory.toFile()) };
			} else {
				this.files = new Song[0];
			}
		}
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

	public String toString() {
		String list = Arrays.stream(files).map(Song::toString)
				.collect(Collectors.joining(", ", "{", "}"));

		return String.format("%s has %d files: %s", directory.toAbsolutePath().toString(), files.length, list);
	}
}
