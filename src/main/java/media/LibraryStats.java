package media;

import java.util.HashSet;

/**
 * Summarize the active library.
 *
 * TODO album info?
 * activity?
 */
public class LibraryStats {
	private Song longestSong;
	private Song shortestSong;

	private int totalSongs;
	private HashSet<String> albums, artists;

	public LibraryStats() {
		albums = new HashSet<>();
		artists = new HashSet<>();
	}

	/**
	 * Add a new song to the current summary.
	 */
	public void addSong(Song song) {
		if (song.getAlbum() != null) {
			albums.add(song.getAlbum());
		}

		if (song.getArtist() != null) {
			artists.add(song.getArtist());
		}

		if (shortestSong == null || song.getDuration() < shortestSong.getDuration()) {
			shortestSong = song;
		}

		if (longestSong == null || song.getDuration() > longestSong.getDuration()) {
			longestSong = song;
		}

		// TODO assume that a song is never added multiple times?
		totalSongs++;
	}

	public int getTotalSongs() {
		return totalSongs;
	}

	public int getTotalAlbums() {
		return albums.size();
	}

	public int getTotalArtists() {
		return artists.size();
	}

	public Song getLongest() {
		return longestSong;
	}

	public Song getShortest() {
		return shortestSong;
	}
}
