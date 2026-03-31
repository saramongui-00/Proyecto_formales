package co.edu.uptc.lenguajesformales.persistence;

import co.edu.uptc.lenguajesformales.model.Automaton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class AutomatonPersistence {

	private final Gson gson;

	public AutomatonPersistence() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public void exportToJson(Automaton automaton, String filePath) throws IOException {
		if (automaton == null) {
			throw new IllegalArgumentException("Automaton cannot be null");
		}
		if (filePath == null || filePath.isBlank()) {
			throw new IllegalArgumentException("File path cannot be empty");
		}

		Path path = Path.of(filePath);
		Path parent = path.getParent();
		if (parent != null && !Files.exists(parent)) {
			Files.createDirectories(parent);
		}

		try (Writer writer = Files.newBufferedWriter(path)) {
			gson.toJson(automaton, writer);
		}
	}

	public Automaton importFromJson(String filePath) throws IOException {
		if (filePath == null || filePath.isBlank()) {
			throw new IllegalArgumentException("File path cannot be empty");
		}

		Path path = Path.of(filePath);
		if (!Files.exists(path)) {
			throw new IOException("File does not exist: " + filePath);
		}

		try (Reader reader = Files.newBufferedReader(path)) {
			Automaton automaton = gson.fromJson(reader, Automaton.class);
			return normalize(automaton);
		}
	}

	private Automaton normalize(Automaton automaton) {
		if (automaton == null) {
			throw new IllegalArgumentException("Invalid JSON: automaton is null");
		}

		if (automaton.getStates() == null) {
			automaton.setStates(new ArrayList<>());
		}
		if (automaton.getAlphabet() == null) {
			automaton.setAlphabet(new ArrayList<>());
		}
		if (automaton.getTransitions() == null) {
			automaton.setTransitions(new ArrayList<>());
		}
		if (automaton.getFinalStates() == null) {
			automaton.setFinalStates(new ArrayList<>());
		}

		return automaton;
	}
}
