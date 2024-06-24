package media;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Summarize the active library.
 * 
 * Use:
 * 	1. Initialize
 * 	2. reset()
 * 	3. addSong()
 * 	4. repeat Step 3 ad infinitum
 * 	5. get()
 * 	6. call getters i.e. getTotalDuration(), getAllAlbums()
 * 	7. repeat steps 2-6 as needed
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

		clearAtomics();
	}

	private ConcurrentMap<String, Set> concurrent_songs_by_artist, concurrent_songs_by_genre,
			concurrent_albums_by_artist;
	private ConcurrentSkipListSet<String> concurrent_arts, concurrent_gens;
	private ConcurrentSkipListSet<Album> concurrent_albs;

	private AtomicInteger atomicTotal, atomicDuration;
	private AtomicReference<Song> atomicLongest, atomicShortest;

	public void get() {
		clearObservables();

		arts.addAll(concurrent_arts);
		albs.addAll(concurrent_albs);
		gens.addAll(concurrent_gens);

		concurrent_songs_by_artist.forEach(songs_by_artist::put);
		concurrent_songs_by_genre.forEach(songs_by_genre::put);
		concurrent_albums_by_artist.forEach(albums_by_artist::put);

		shortestSong = atomicShortest.get();
		longestSong = atomicLongest.get();
		totalDuration = atomicDuration.get();
		totalSongs = atomicTotal.get();
	}

	/**
	 * Clear all collections and prepare for adding songs
	 * Should be called before addSong
	 */
	public void reset() {
		clearAtomics();
		clearObservables();
	}

	/*
	 * Clear function to ensure that the observable collections stay connected
	 */
	private void clearObservables() {
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

	private void clearAtomics() {
		atomicTotal = new AtomicInteger();
		atomicDuration = new AtomicInteger();
		atomicShortest = new AtomicReference<>();
		atomicLongest = new AtomicReference<>();

		concurrent_songs_by_artist = new ConcurrentHashMap<>();
		concurrent_songs_by_genre = new ConcurrentHashMap<>();
		concurrent_albums_by_artist = new ConcurrentHashMap<>();
		concurrent_arts = new ConcurrentSkipListSet<>();
		concurrent_gens = new ConcurrentSkipListSet<>();
		concurrent_albs = new ConcurrentSkipListSet<>();

		concurrent_songs_by_artist.clear();
		concurrent_songs_by_genre.clear();
		concurrent_albums_by_artist.clear();

		concurrent_albs.clear();
		concurrent_arts.clear();
		concurrent_gens.clear();
	}

	/**
	 * Add a new song to the current summary.
	 * Changes not reflected in observables until you call get()
	 */
	public void addSong(Song song) {
		concurrent_gens.add(song.getGenre());
		concurrent_albs.add(song.getAlbum());
		concurrent_arts.add(song.getArtist());

		concurrent_songs_by_artist.putIfAbsent(song.getArtist(), new ConcurrentSkipListSet<>());
		concurrent_songs_by_artist.get(song.getArtist()).add(song);

		concurrent_songs_by_genre.putIfAbsent(song.getGenre(), new ConcurrentSkipListSet<>());
		concurrent_songs_by_genre.get(song.getGenre()).add(song);

		concurrent_albums_by_artist.putIfAbsent(song.getArtist(), new ConcurrentSkipListSet<>());
		concurrent_albums_by_artist.get(song.getArtist()).add(song.getAlbum());

		// compute extrema
		if (atomicShortest.get() == null || song.getDuration() < atomicShortest.get().getDuration()) {
			atomicShortest.set(song);
		}
		if (atomicLongest.get() == null || song.getDuration() > atomicLongest.get().getDuration()) {
			atomicLongest.set(song);
		}

		// TODO assume that a song is never added multiple times?
		atomicTotal.incrementAndGet();
		atomicDuration.addAndGet(song.getDuration());
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
