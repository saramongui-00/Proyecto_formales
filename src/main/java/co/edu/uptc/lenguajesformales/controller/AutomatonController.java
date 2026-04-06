package co.edu.uptc.lenguajesformales.controller;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;
import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.model.Transition;
import co.edu.uptc.lenguajesformales.persistence.AutomatonPersistence;
import co.edu.uptc.lenguajesformales.view.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutomatonController implements ActionListener {
    private Automaton automaton;
    private MainWindow view;
    private AutomatonPersistence persistence;

    // Inicializa el controlador principal junto con el modelo, la vista y la capa
    // de persistencia.

    public AutomatonController() {
        automaton = new Automaton();
        view = new MainWindow(this);
        persistence = new AutomatonPersistence();
    }

    /**
     * Atiende los eventos de la interfaz y redirige cada accion al metodo
     * correspondiente del controlador.
     *
     * @param e evento disparado por un componente de la vista
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "createAutomatonBtn":
                view.changeCreateAutomatonPanel();
                break;
            case "evaluateAutomatonBtn":
                view.changeEvaluateAutomatonPanel();
                break;
            case "saveAutomatonBtn":
                saveAutomaton();
                break;
            case "generateAutomatonBtn":
                generateAutomaton();
                break;
            case "evaluateBtn":
                evaluateInputs();
                break;
            case "traceBtn":
                traceAutomaton();
                break;
            case "epsilonBtn":
                view.addEpsilonInput();
                break;

        }
    }

    // Ejecuta la evaluacion paso a paso para una cadena y muestra la traza
    // detallada en un dialogo.
    public void traceAutomaton() {
        try {
            String inputToTrace = view.getInputToTrace();
            List<String> detailedTrace = automaton.evaluateWithTrace(inputToTrace);
            view.initTraceDialog(detailedTrace.size(), detailedTrace);
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    // Muestra las opciones de guardado y decide si se exporta o importa el automata
    // segun la seleccion del usuario.

    public void saveAutomaton() {
        int option = view.saveAutomatonAlert();
        switch (option) {
            case 0/* Exportar */:
                exportAutomaton();
                break;
            case 1/* Importar */:
                importAutomaton();
                break;
            default:
                break;
        }
    }

    // Exporta el automata actual a un archivo JSON mediante la capa de
    // persistencia.
    public void exportAutomaton() {
        try {
            if (automaton.getStates().isEmpty()) {
                view.showError("Tiene que crear el autómata");
            } else {
                String filePath = view.exportAutomaton();
                persistence.exportToJson(automaton, filePath);
                view.showMessage("Automata exportado exitosamente");
            }
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    // Importa un automata desde un archivo JSON y actualiza la vista con los datos
    // cargados.

    public void importAutomaton() {
        try {
            String filePath = view.importAutomaton();
            automaton = persistence.importFromJson(filePath);
            view.loadAutomaton(new AutomatonDTO(automaton.getStates(), automaton.getAlphabet(),
                    transitionDTOMapper(automaton.getTransitions()), automaton.getInitialState(),
                    automaton.getFinalStates()));
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    // Evalua un conjunto de cadenas sobre el automata actual y muestra el resultado
    // de aceptacion/rechazo para cada una.
    public void evaluateInputs() {
        try {
            if (automaton.getStates().isEmpty()) {
                view.showError("Tiene que crear el autómata");
            } else {
                ArrayList<String> inputs = view.getInputs();
                Map<String, Boolean> results = automaton.evaluateBatchAutomaton(inputs);
                view.showEvaluationResults(results);
            }
        } catch (Exception e) {
            view.showError(e.getMessage());
        }

    }

    // Obtiene la definicion del automata desde la vista, construye una nueva
    // instancia del modelo y la valida como DFA antes de dibujarla.
    public void generateAutomaton() {

        if (!view.generateAutomaton())
            return;

        AutomatonDTO dto = view.getAutomaton();

        Automaton newAutomaton = new Automaton(dto.getStates(), dto.getAlphabet(),
                transitionMapper(dto.getTransitions()),
                dto.getInitialState(), dto.getFinalStates());

        if (newAutomaton.validateDFA()) {
            this.automaton = newAutomaton;
            view.drawAutomaton();
        } else {
            view.showError("DFA no válido");
        }
    }

    /**
     * Convierte una lista de transiciones de DTO a entidades del modelo.
     *
     * @param dtoList lista de transiciones recibidas desde la vista
     * @return lista equivalente de transiciones del dominio
     */
    public List<Transition> transitionMapper(List<TransitionDTO> dtoList) {
        return dtoList.stream()
                .map(dto -> new Transition(
                        dto.getFromState(),
                        dto.getSymbol(),
                        dto.getToState()))
                .toList();
    }

    /**
     * Convierte una lista de transiciones del modelo a DTO para su uso en la
     * vista.
     *
     * @param dtoList lista de transiciones del dominio
     * @return lista equivalente de transiciones en formato DTO
     */
    public List<TransitionDTO> transitionDTOMapper(List<Transition> dtoList) {
        return dtoList.stream()
                .map(dto -> new TransitionDTO(
                        dto.getFromState(),
                        dto.getSymbol(),
                        dto.getToState()))
                .toList();
    }
}
