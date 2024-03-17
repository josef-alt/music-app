package media;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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
public class Song {
	private String title;
	private String album;
	private String artist;
	private Image cover;
	private File source;
	
	private final Image DEFAULT_ALBUM_COVER;

	public Song(File source) {
		DEFAULT_ALBUM_COVER = new Image(getClass().getResourceAsStream("/img/cd.png"));
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
				cover = new Image(new ByteArrayInputStream(tag.getFirstArtwork().getBinaryData()), 250, 250, false,
						false);
			}

			if (tag.hasField(FieldKey.ARTIST)) {
				artist = tag.getFirst(FieldKey.ARTIST);
			}

			if (tag.hasField(FieldKey.TITLE)) {
				title = tag.getFirst(FieldKey.TITLE);
			}
		}

		// TODO exception handling
		catch(IOException | CannotReadException e) {
			System.err.println("Failed to read file");
			e.printStackTrace();
		}
		catch (TagException | ReadOnlyFileException e) {
			System.err.println("Failed to read tags");
			e.printStackTrace();
		}
		catch (InvalidAudioFrameException e) {
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
		return hasCover() ? cover : DEFAULT_ALBUM_COVER;
	}
}
