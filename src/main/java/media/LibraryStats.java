package media;

import java.util.*;
import java.util.Map.*;

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
	private int totalDuration;
	private Map<String, Set<String>> albums_by_artist, songs_by_artist;
	private HashSet<String> artists;

	public LibraryStats() {
		songs_by_artist = new HashMap<>();
		albums_by_artist = new HashMap<>();
		artists = new HashSet<>();
	}

	/**
	 * Add a new song to the current summary.
	 */
	public void addSong(Song song) {
		if (song.getAlbum() != null) {
			if (song.getArtist() != null) {
				albums_by_artist.putIfAbsent(song.getArtist(), new HashSet<>());
				albums_by_artist.get(song.getArtist()).add(song.getAlbum());
			} else {
				albums_by_artist.putIfAbsent("unknown artist", new HashSet<>());
				albums_by_artist.get("unknown artist").add(song.getAlbum());
			}
		}

		if (song.getTitle() != null) {
			if (song.getArtist() != null) {
				songs_by_artist.putIfAbsent(song.getArtist(), new HashSet<>());
				songs_by_artist.get(song.getArtist()).add(song.getTitle());
			} else {
				songs_by_artist.putIfAbsent("unknown artist", new HashSet<>());
				songs_by_artist.get("unknown artist").add(song.getTitle());
			}
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
		totalDuration += song.getDuration();
	}

	public int getTotalSongs() {
		return totalSongs;
	}

	public int getTotalAlbums() {
		return albums_by_artist.entrySet().stream().map(E -> E.getValue().size()).reduce(0, Integer::sum);
	}

	public int getTotalArtists() {
		return artists.size();
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	private Optional<Entry<String, Set<String>>> getMax(Map<String, Set<String>> map) {
		return map.entrySet().stream().max(Comparator.comparing(entry -> entry.getValue().size()));
	}

	private String getMaxKey(Map<String, Set<String>> map) {
		Optional<Entry<String, Set<String>>> max = getMax(map);
		if (max.isPresent()) {
			return max.get().getKey();
		} else {
			return "not found";
		}
	}

	private int getMaxSize(Map<String, Set<String>> map) {
		Optional<Entry<String, Set<String>>> max = getMax(map);
		if (max.isPresent()) {
			return max.get().getValue().size();
		} else {
			return 0;
		}
	}

	public String getArtistWithMostSongs() {
		return getMaxKey(songs_by_artist);
	}

	public int getMostSongsByArtist() {
		return getMaxSize(songs_by_artist);
	}

	public String getArtistWithMostAlbums() {
		return getMaxKey(albums_by_artist);
	}

	public int getMostAlbumsByArtist() {
		return getMaxSize(albums_by_artist);
	}

	public Song getLongest() {
		return longestSong;
	}

	public Song getShortest() {
		return shortestSong;
	}
}
