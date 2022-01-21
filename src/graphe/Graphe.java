package graphe;
import java.util.LinkedList;

public class Graphe {
    // tab 2D qui stockera les valeurs constituant la matrice de valeur du graphe
    private int matriceValeur[][];
    // tab 2D qui stockera les valeurs constituant la matrice d'adjacence du graphe
    private int matriceAdjacence[][];
    private int nbSommets;
    // stockage en memoire pour construire un chemin
    // tab 2D qui stockera les poids des arcs entre chaque sommets (copie mat Valeur)
    private int poidsEntreSommets[][];
    // tab 2D qui va nous servir à déterminer l'existence d'un chemin vers le sommet suivant
    private int pathfinder[][];
    // constante nous permettant d'assigner la valeur infinie pour les distances inatteignable
    private static final int INF = 99999;

    // initialisation du graphe
    public Graphe(int nbDeSommets) {
        this.nbSommets = nbDeSommets;
        this.matriceValeur = new int [nbDeSommets][nbDeSommets];
        this.matriceAdjacence = new int[nbDeSommets][nbDeSommets];
        this.poidsEntreSommets = new int[nbDeSommets][nbDeSommets];
        this.pathfinder = new int[nbDeSommets][nbDeSommets];
    }

    // notre tab2D poidsEntreSommets va copier les valeurs de la matrice de valeur du graphe
    public void initialisationChemin(){
        for(int i = 0; i < nbSommets; i++)
        {
            for(int j = 0; j < nbSommets; j++)
            {
                poidsEntreSommets[i][j] = matriceValeur[i][j];

                // Si il n'existe pas d'Arc entre 2 sommets i et j
                // on assigne la valeur -1 a notre tab 2d pathfinder afin qu'il puisse
                // déterminer l'existence d'un chemin vers le sommet suivant
                if (poidsEntreSommets[i][j] == INF)
                    pathfinder[i][j] = -1;
                else
                    pathfinder[i][j] = j;
            }
        }
    }


    // retourne le plus court chemin entre le sommet de départ et le sommet de destination
    public LinkedList<Integer> constructionChemin(int depart, int destination){

        // Si il n'existe pas de chemin entre le sommet de depart et de destination
        // on retourne une liste NULL

        if (pathfinder[depart][destination] == -1)
            return null;

        // Stockage des chemins dans une liste chainée
        LinkedList<Integer> path = new LinkedList<Integer>();
        path.add(depart);

        while (depart != destination) {
            depart = pathfinder[depart][destination];
            path.add(depart);
        }
        return path;
    }

    // Ajout d'un arc
    public void addArc(int source, int destination, int poids) {

        matriceAdjacence[source][destination] = 1;
        matriceValeur[source][destination]=poids;

    }

    // attribution des valeurs INF là ou il faut car
    // lorsqu'on initialise nos matrices, toutes les valeurs qui ne sont pas modifiés sont à 0
    public void ajoutDeINF() {
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (matriceAdjacence[i][j] == 0 && i != j){
                    matriceValeur[i][j] = INF;
                }
            }
        }
    }


    public void floydWarshall(int matriceV[][]){
        int distance[][] = new int[nbSommets][nbSommets];
        int i,j,k;

        // Initialisation d'une copie de notre matrice de valeur
        // sur laquelle on va appliquer les modifications avec l'algorithme de Floyd Warshall

        for (i = 0; i < nbSommets; i++){
            for (j = 0; j < nbSommets; j++){
                distance[i][j] = matriceV[i][j];
            }
        }
        // initialement nos distances les plus court entre deux points du graphe sont calculés directement
        // par rapport aux chemins basique reliant deux points
        // nous allons donc voir si il existe un sommet alternatif pour relier les deux points
        // pour pouvoir trouver le vrais chemins les plus courts

        // k represente le sommet alternatif
        for (k = 0; k < nbSommets; k++){
            // selection des sommets source 1 par 1
            for (i = 0; i < nbSommets; i++){
                // selection de tout les sommets destination pour les sommets sources selectionné
                for (j = 0; j < nbSommets; j++){
                    // Si le sommet k est dans le chemin le plus court entre i et j
                    // alors on mets à jour la valeur de la distance entre i et j en le faisant passer par k
                    if (distance[i][k] != INF && distance[k][j] != INF &&
                            distance[i][k] + distance[k][j] < distance[i][j]){
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }

                    // si le chemin n'existe pas on ne fait rien pour notre copie de la matrice de valeur
                    if (poidsEntreSommets[i][k] == INF || poidsEntreSommets[k][j] == INF){
                        continue;
                    }
                    // si il existe un chemin et que le sommet k se trouve dans le chemin le plus cours entre i et j
                    // alors on mets à jour la valeur de la distance entre i et j de la copie de notre matrice de valeur
                    // en le faisant passer par k
                    // on donne la valeur de la distance entre i et k pour notre matrice de chemin

                    if (poidsEntreSommets[i][j] > poidsEntreSommets[i][k] + poidsEntreSommets[k][j]){
                        poidsEntreSommets[i][j] = poidsEntreSommets[i][k] + poidsEntreSommets[k][j];
                        pathfinder[i][j] = pathfinder[i][k];
                    }

                }
            }
            System.out.println("Iteration "+k + " :");
            // Affichage des matrices de valeurs intermédiaires
            printMatriceValeur(distance);
        }

    }

  public boolean hasCircuitAbsorbant(int graphe[][]) {
        int distance[][] = new int[nbSommets][nbSommets], i, j, k;

      // Initialisation d'une copie de notre matrice de valeur
      // sur laquelle on va appliquer les modifications avec l'algorithme de Floyd Warshall
        for (i = 0; i < nbSommets; i++)
            for (j = 0; j < nbSommets; j++)
                distance[i][j] = graphe[i][j];

      // initialement nos distances les plus court entre deux points du graphe sont calculés directement
      // par rapport aux chemins basique reliant deux points
      // nous allons donc voir si il existe un sommet alternatif pour relier les deux points
      // pour pouvoir trouver le vrais chemins les plus courts

      // k represente le sommet alternatif
        for (k = 0; k < nbSommets; k++){
            // selection de tout les sommets destination pour les sommets sources selectionnés
            for (i = 0; i < nbSommets; i++){
                // Si le sommet k est dans le chemin le plus court entre i et j
                // alors on mets à jour la valeur de la distance entre i et j en le faisant passer par k
                for (j = 0; j < nbSommets; j++){
                    if (distance[i][k] + distance[k][j] < distance[i][j]){
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
        // Si la distance d'un sommet depuis ce même sommet devient negative alors il existe un circuit absorbant
        // On retourne alors true si il existe un absorbant et false si il n'y en a pas
        for (i = 0; i < nbSommets; i++){
            if (distance[i][i] < 0){
                return true;
            }
        }
        return false;
    }


    // Affichage du chemin le plus court
    public void printChemin(LinkedList<Integer> chemin) throws NullPointerException {
        if(chemin == null){
            throw new NullPointerException("Pas de chemin possible");

        }
        int n = chemin.size();
        int debut = 0;
        System.out.println("Le plus court chemin pour : "+chemin.get(debut) + " -> " + chemin.get(n-1)+ " est :");
        for(int i = 0; i < n - 1; i++){

            System.out.print(chemin.get(i) + " -> " +chemin.get(i+1) + " \n");

        }

    }

    // Affichade de la matrice de valeur d'un graphe
    public void printMatriceValeur(int graphe[][]) {
        for (int i=0; i<nbSommets; ++i){
            for (int j=0; j<nbSommets; ++j){
                if (graphe[i][j]== INF)
                    System.out.print("INF ");
                else
                    System.out.print(graphe[i][j]+"   ");
            }
            System.out.println();
        }

    }

    public void printMatriceAdjacence(){
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j <nbSommets ; j++) {

                    System.out.print(matriceAdjacence[i][j]+ "   ");
            }
            System.out.println();
        }

    }

    // GETTERS
    public int getNbSommets() {
        return nbSommets;
    }

    public int[][] getMatriceAdjacence() {
        return matriceAdjacence;
    }

    public int[][] getMatriceValeur() {
        return matriceValeur;
    }

}
