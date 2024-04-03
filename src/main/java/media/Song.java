package media;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.*;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.*;

import javafx.scene.image.Image;
import javafx.scene.media.*;

/**
 * Contains extracted file information for each song to make Library
 * operations easier.
 */
public class Song {
	private String title;
	private String album;
	private String artist;
	private byte[] cover;
	private File source;
	private int duration;

	private static final Image DEFAULT_ALBUM_COVER = new Image(Song.class.getResourceAsStream("/img/cd.png"));;
	static {
		// https://stackoverflow.com/q/50778442
		Logger[] pin = new Logger[] { Logger.getLogger("org.jaudiotagger") };

		for (Logger l : pin)
			l.setLevel(Level.OFF);
	}

	public Song(File source) {
		this.source = source;
		loadTags();
	}

	/**
	 * Attempts to populate Song fields from source metadata
	 */
	private void loadTags() {
		try {
			AudioFile af = AudioFileIO.read(source);
			Tag tag = af.getTag();

			if (tag.hasField(FieldKey.ALBUM)) {
				album = tag.getFirst(FieldKey.ALBUM);
			}

			if (tag.hasField(FieldKey.COVER_ART)) {
				cover = tag.getFirstArtwork().getBinaryData();
			}

			if (tag.hasField(FieldKey.ARTIST)) {
				artist = tag.getFirst(FieldKey.ARTIST);
			}

			if (tag.hasField(FieldKey.TITLE)) {
				title = tag.getFirst(FieldKey.TITLE);
			} else {
				title = source.getName();
			}

			duration = af.getAudioHeader().getTrackLength();
		}

		// TODO exception handling
		catch (IOException | CannotReadException e) {
			System.err.println("Failed to read file");
			e.printStackTrace();
		} catch (TagException | ReadOnlyFileException e) {
			System.err.println("Failed to read tags");
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			e.printStackTrace();
		}
	}

	public boolean hasTitle() {
		return title != null && !title.isBlank();
	}

	public String getTitle() {
		return title;
	}

	public boolean hasAlbum() {
		return album != null && !album.isBlank();
	}

	public String getAlbum() {
		return album;
	}

	public boolean hasArtist() {
		return artist != null && !artist.isBlank();
	}

	public String getArtist() {
		return artist;
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

	public File getFile() {
		return source;
	}

	public int getDuration() {
		return duration;
	}

	public String toString() {
		return String.format("%s", title);
	}
}
