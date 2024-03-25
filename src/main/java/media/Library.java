package media;

import java.io.IOException;
import java.nio.file.PathMatcher;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import java.util.Arrays;
import java.util.stream.*;
import java.util.ArrayList;

/**
 * Contains everything necessary for managing the music library.
 * 
 * TODO playlists, filtering, genre/artist/album/etc
 */
public class Library {
	private Song[] files;
	private Path directory;

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
		if (Files.isDirectory(directory)) {
			this.files = Files.list(directory).filter(matcher::matches).map(Path::toFile).map(Song::new)
				.toArray(Song[]::new);
		} else if (Files.isRegularFile(directory)) {
			this.files = Stream.of(directory).filter(matcher::matches).map(Path::toFile).map(Song::new)
					.toArray(Song[]::new);
		}
	}

	public int getNumberOfTracks() {
		return files.length;
	}

	public Song getTrack(int trackNumber) {
		return files[trackNumber];
	}

	public String toString() {
		String list = Arrays.stream(files).map(Song::toString)
				.collect(Collectors.joining(", ", "{", "}"));

		return String.format("%s has %d files: %s", directory.toAbsolutePath().toString(), files.length, list);
	}
}
