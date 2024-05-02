package media;

import java.util.*;
import java.util.Map.*;

import javafx.collections.*;

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

	private ObservableMap<String, Set> songs_by_artist, songs_by_genre, albums_by_artist;
	private ObservableList<String> arts, gens;
	private ObservableList<Album> albs;

	public LibraryStats() {
		arts = FXCollections.observableArrayList();
		albs = FXCollections.observableArrayList();
		gens = FXCollections.observableArrayList();

		songs_by_artist = FXCollections.observableHashMap();
		songs_by_genre = FXCollections.observableHashMap();
		albums_by_artist = FXCollections.observableHashMap();

		clear();
	}

	/**
	 * Clear function to ensure that the observable collections stay connected
	 */
	public void clear() {
		longestSong = null;
		shortestSong = null;
		totalSongs = 0;
		totalDuration = 0;

		songs_by_artist.clear();
		songs_by_genre.clear();
		albums_by_artist.clear();

		arts.clear();
		albs.clear();
		gens.clear();
	}

	/**
	 * Add a new song to the current summary.
	 */
	public void addSong(Song song) {
		// ensure sorted list of each attribute also contains the unknown variant
		int index = Collections.binarySearch(gens, song.getGenre());
		if (index < 0) {
			gens.add(~index, song.getGenre());
		}
		index = Collections.binarySearch(albs, song.getAlbum());
		if (index < 0) {
			albs.add(~index, song.getAlbum());
		}
		index = Collections.binarySearch(arts, song.getArtist());
		if (index < 0) {
			arts.add(~index, song.getArtist());
		}

		songs_by_artist.putIfAbsent(song.getArtist(), FXCollections.observableSet());
		songs_by_artist.get(song.getArtist()).add(song);

		songs_by_genre.putIfAbsent(song.getGenre(), FXCollections.observableSet());
		songs_by_genre.get(song.getGenre()).add(song);

		albums_by_artist.putIfAbsent(song.getArtist(), FXCollections.observableSet());
		albums_by_artist.get(song.getArtist()).add(song.getAlbum());

		// compute extrema
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
		return albs.size();
	}

	public int getTotalArtists() {
		return arts.size();
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	/**
	 * Find the key with the largest corresponding set
	 */
	private Optional<Entry<String, Set>> getMax(Map<String, Set> map) {
		return map.entrySet().stream().max(Comparator.comparing(entry -> entry.getValue().size()));
	}

	private String getMaxKey(Map<String, Set> map) {
		Optional<Entry<String, Set>> max = getMax(map);
		if (max.isPresent()) {
			return max.get().getKey();
		} else {
			return "not found";
		}
	}

	private int getMaxSize(Map<String, Set> map) {
		Optional<Entry<String, Set>> max = getMax(map);
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

	public ObservableList<String> getAllArtists() {
		return arts;
	}

	public ObservableList<Album> getAllAlbums() {
		return albs;
	}

	public ObservableList<String> getAllGenres() {
		return gens;
	}
}
