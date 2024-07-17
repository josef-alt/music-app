package media;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.*;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import javafx.scene.image.Image;

/**
 * Contains extracted file information for each song to make Library
 * operations easier.
 */
public class Song implements Comparable<Song> {
	private String title;
	private Album album;
	private String artist;
	private String genre;
	private byte[] cover;
	private File source;
	private int duration;
	private boolean successfullyRead;

	private static final Image DEFAULT_ALBUM_COVER = new Image(Song.class.getResourceAsStream("/img/cd.png"));;
	static {
		// https://stackoverflow.com/q/50778442
		System.out.println("disable logging");
		Logger[] pin = new Logger[] { Logger.getLogger("org.jaudiotagger") };

		for (Logger l : pin)
			l.setLevel(Level.OFF);
	}

	public Song(File source) {
		this.source = source;
		successfullyRead = true;
		loadTags();
	}

	/**
	 * Attempts to populate Song fields from source metadata
	 */
	private void loadTags() {
		try {
			AudioFile af = AudioFileIO.read(source);
			Tag tag = af.getTag();

			album = new Album();
			if (tag.hasField(FieldKey.ALBUM)) {
				album.setAlbumName(tag.getFirst(FieldKey.ALBUM));
			}

			if (tag.hasField(FieldKey.COVER_ART)) {
				cover = tag.getFirstArtwork().getBinaryData();
			}

			if (tag.hasField(FieldKey.ARTIST)) {
				artist = tag.getFirst(FieldKey.ARTIST);
				album.setAlbumArtist(artist);
			}

			if (tag.hasField(FieldKey.TITLE)) {
				title = tag.getFirst(FieldKey.TITLE);
			} else {
				title = source.getName();
			}

			if (tag.hasField(FieldKey.GENRE)) {
				genre = tag.getFirst(FieldKey.GENRE);
			}

			duration = af.getAudioHeader().getTrackLength();
		}

		// TODO exception handling
		catch (IOException | CannotReadException e) {
			System.err.println("Failed to read file");
			e.printStackTrace();
			successfullyRead = false;
		} catch (TagException | ReadOnlyFileException e) {
			System.err.println("Failed to read tags");
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			e.printStackTrace();
		}
	}

	public boolean readSuccessfully() {
		return successfullyRead;
	}

	public boolean hasTitle() {
		return title != null && !title.isBlank();
	}

	public String getTitle() {
		return hasTitle() ? title : "Unknown Title";
	}

	public boolean hasAlbum() {
		return album != null && !album.getAlbumName().isBlank();
	}

	public Album getAlbum() {
		return album;
	}

	public boolean hasArtist() {
		return artist != null && !artist.isBlank();
	}

	public String getArtist() {
		return hasArtist() ? artist : "Unknown Artist";
	}

	public boolean hasGenre() {
		return genre != null && !genre.isBlank();
	}

	public String getGenre() {
		return hasGenre() ? genre : "Unknown Genre";
	}

	public boolean hasCover() {
		return cover != null;
	}

	/**
	 * Retrieves the album art or the project's default cover
	 * Should always return a valid image - never null
	 */
	public Image getCover() {
		if (hasCover())
			return new Image(new ByteArrayInputStream(cover));
		return DEFAULT_ALBUM_COVER;
	}

	public static Image getDefaultCover() {
		return DEFAULT_ALBUM_COVER;
	}

	public File getFile() {
		return source;
	}

	public int getDuration() {
		return duration;
	}

	public String toString() {
		return String.format("%s", title);
	}

	@Override
	public int compareTo(Song o) {
		return Comparator.comparing(Song::getTitle).thenComparing(Song::getAlbum)
				.thenComparing(Song::getArtist, (a, b) -> {
					if (a.equals("Unknown Artist")) {
						return 1;
					}
					if (b.equals("Unknown Artist")) {
						return -1;
					}
					return a.compareTo(b);
				})
				.compare(this, o);
	}
}
