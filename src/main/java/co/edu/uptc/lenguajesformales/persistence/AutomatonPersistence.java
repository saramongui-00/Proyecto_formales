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

    /**
     * Inicializa el componente de persistencia con una instancia de Gson
     * configurada para generar JSON legible (pretty printing).
     */
    public AutomatonPersistence() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Exporta un automata a un archivo JSON.
     * Valida los parametros de entrada y crea los directorios faltantes
     * antes de escribir el contenido en disco.
     *
     * @param automaton automata a serializar
     * @param filePath ruta de destino del archivo JSON
     * @throws IOException si ocurre un error durante la escritura
     */
    public void exportToJson(Automaton automaton, String filePath) throws IOException {
        if (automaton == null) {
            throw new IllegalArgumentException("El automata no puede ser nulo.");
        }
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacia");
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

    /**
     * Importa un automata desde un archivo JSON.
     * Tras deserializarlo, normaliza sus colecciones para evitar valores nulos.
     *
     * @param filePath ruta del archivo JSON de entrada
     * @return automata cargado y normalizado
     * @throws IOException si el archivo no existe o hay errores de lectura
     */
    public Automaton importFromJson(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be empty");
        }

        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new IOException("El archivo no existe: " + filePath);
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            Automaton automaton = gson.fromJson(reader, Automaton.class);
            return normalize(automaton);
        }
    }

    /**
     * Garantiza que el automata deserializado tenga todas sus listas inicializadas.
     * Si alguna coleccion llega como null desde el JSON, se reemplaza por una
     * lista vacia para evitar errores posteriores.
     *
     * @param automaton automata deserializado
     * @return automata con estructura interna consistente
     */
    private Automaton normalize(Automaton automaton) {
        if (automaton == null) {
            throw new IllegalArgumentException("El archivo JSON no contiene un automata valido.");
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