import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoAEstrella {

    // Lista de adyacencia con costos
    private Map<String, List<String>> vertices;
    private Map<String, Integer> costos;
    private Map<String, Integer> heuristica;

    public GrafoAEstrella() {
        vertices = new HashMap<>();
        costos = new HashMap<>();
        heuristica = new HashMap<>();
    }

    public void agregarVertice(String v) {
        vertices.putIfAbsent(v, new ArrayList<>());
    }

    public void agregarArista(String a, String b, int costo) {
        vertices.get(a).add(b);
        vertices.get(b).add(a);
        costos.put(a + b, costo);
        costos.put(b + a, costo);
    }

    public void agregarHeuristica(String v, int h) {
        heuristica.put(v, h);
    }

    public void aEstrella(String inicio, String meta) {

        List<String> abiertos = new ArrayList<>();
        List<String> cerrados = new ArrayList<>();

        Map<String, Integer> g = new HashMap<>();
        Map<String, String> padre = new HashMap<>();

        abiertos.add(inicio);
        g.put(inicio, 0);

        while (!abiertos.isEmpty()) {

            // Elegir el nodo con menor f = g + h
            String actual = abiertos.get(0);
            for (String n : abiertos) {
                int fActual = g.get(actual) + heuristica.get(actual);
                int fN = g.get(n) + heuristica.get(n);
                if (fN < fActual) {
                    actual = n;
                }
            }

            if (actual.equals(meta)) {
                mostrarCamino(padre, inicio, meta);
                System.out.println("Costo total: " + g.get(meta));
                return;
            }

            abiertos.remove(actual);
            cerrados.add(actual);

            for (String vecino : vertices.get(actual)) {

                if (cerrados.contains(vecino)) {
                    continue;
                }

                int nuevoG = g.get(actual) + costos.get(actual + vecino);

                if (!abiertos.contains(vecino)) {
                    abiertos.add(vecino);
                    padre.put(vecino, actual);
                    g.put(vecino, nuevoG);
                }
            }
        }
    }

    private void mostrarCamino(Map<String, String> padre, String inicio, String meta) {
        System.out.println("Camino encontrado:");

        List<String> camino = new ArrayList<>();
        String actual = meta;

        // Se arma el camino al revés (E->...->A)
        while (actual != null) {
            camino.add(actual);
            actual = padre.get(actual);
        }

        // Se imprime al derecho (A->...->E)
        for (int i = camino.size() - 1; i >= 0; i--) {
            System.out.print(camino.get(i));
            if (i != 0) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {

        GrafoAEstrella grafo = new GrafoAEstrella();

        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarVertice("E");

        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("A", "C", 3);
        grafo.agregarArista("B", "D", 1);
        grafo.agregarArista("C", "D", 1);
        grafo.agregarArista("D", "E", 2);

        // Heurística (estimación al destino E)
        grafo.agregarHeuristica("A", 4);
        grafo.agregarHeuristica("B", 3);
        grafo.agregarHeuristica("C", 2);
        grafo.agregarHeuristica("D", 1);
        grafo.agregarHeuristica("E", 0);

        grafo.aEstrella("A", "E");
    }
}
