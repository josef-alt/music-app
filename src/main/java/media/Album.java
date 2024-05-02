package media;

import media.Song;

/**
 * Used to ensure that albums with the same name are kept separate
 */
public class Album implements Comparable<Album> {
	private static final String UNKNOWN_ALBUM = "Unknown Album";
	private static final String UNKNOWN_ARTIST = "Unknown Artist";

	private String albumName;
	private String artistName;

	public Album() {
		albumName = UNKNOWN_ALBUM;
		artistName = UNKNOWN_ARTIST;
	}

	public Album(String albumName, String artistName) {
		this.albumName = albumName;
		this.artistName = artistName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumArtist(String artistName) {
		this.artistName = artistName;
	}

	public String getAlbumArtist() {
		return artistName;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;

		Album o = (Album) other;
		if (albumName == null ^ o.albumName == null)
			return false;
		if (artistName == null ^ o.artistName == null)
			return false;
		return albumName.equals(o.albumName) && artistName.equals(o.artistName);
	}

	/**
	 * Comparing albums:
	 * Alphabetically by album name, then artist name, then unknowns.
	 */
	@Override
	public int compareTo(Album other) {
		if (albumName.equals(UNKNOWN_ALBUM) && !other.albumName.equals(UNKNOWN_ALBUM)) {
			return 1;
		}
		if (!albumName.equals(UNKNOWN_ALBUM) && other.albumName.equals(UNKNOWN_ALBUM)) {
			return -1;
		}
		if (!albumName.equals(other.albumName)) {
			return albumName.compareTo(other.albumName);
		}

		if (artistName.equals(UNKNOWN_ARTIST) && !other.albumName.equals(UNKNOWN_ARTIST)) {
			return 1;
		}
		if (!artistName.equals(UNKNOWN_ARTIST) && other.albumName.equals(UNKNOWN_ARTIST)) {
			return -1;
		}
		return albumName.compareTo(other.albumName);
	}
}