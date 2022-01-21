package application;

import graphe.Graphe;

import java.io.File;

import java.util.*;

import static java.lang.Integer.parseInt;

public class Main {
    // attribut de classe, la Collection de String dans laquelle nous stockerons le texte du fichier choisit
    public static List<String> txtFile = new LinkedList<>();

    // methode pour diviser un tableau en n petit tableau de taille x
    public static List<Integer[]> splitArray(Integer[] arrayToSplit, int tailleDuSplit) {
        // instanciation de notre collections de petit tableau qui vont être split
        List<Integer[]> listTabSplitted = new ArrayList<Integer[]>();

        int totalSize = arrayToSplit.length; //on recupere la taille du tableau
        if(totalSize < tailleDuSplit ){
            tailleDuSplit = totalSize;
        }
        int indexDebut = 0;
        int indexFin = tailleDuSplit;

        while(indexDebut < totalSize){
            // Copie des elements originaux de notre tabOriginal aux index voulus
            Integer[] partArray = Arrays.copyOfRange(arrayToSplit, indexDebut, indexFin);
            listTabSplitted.add(partArray); // ajout de la copie à notre collection de petit tableau

            indexDebut+= tailleDuSplit;  // le pas de notre boucle est egale a la tailleDuSplit
            indexFin = indexDebut + tailleDuSplit;
            if(indexFin>totalSize){
                indexFin = totalSize;
            }
        }
        return listTabSplitted;
    }

    public static void main(String[] args) throws Exception {
        // Initialisation du chemin absolue pour accèder à l'enplacement de nos fichiers texte
        // contenant les graphes de test
        String chemin = "D:\\PROJET\\Floyd Warshall\\assets\\";

        Scanner sc = new Scanner(System.in);
        boolean bonChoix = false;
        int choixGraphe=0;
        while(!bonChoix){
            System.out.println("Veuillez entrer le numéro du graphe (entre 1 et 13) que vous voulez sélectionnez pour appliquer " +
                    "l'algorithme de Floyd Warshall : ");
             choixGraphe = sc.nextInt();
             sc.nextLine();
            if(choixGraphe <= 13 && choixGraphe >=1){
                bonChoix= true;
            }else{
                bonChoix = false;
            }
        }


        chemin +=  choixGraphe + ".txt"; // ajout du nom et de l'extension.txt à notre chemin absolue

        System.out.println(chemin);
        System.out.println("Vous avez choisi le Graphe numéro " + choixGraphe);

        File fichierTexte = new File(chemin);   // instanciation de notre fichier Texte en memoire
        Scanner scannerFile = new Scanner(fichierTexte);    // instanciation d'un Scanner qui lira notre fichier

        int i = 0;
        // récupération ligne par ligne du texte de notre fichier et ajout dans notre attribut de classe
        // pour un stockage du texte du fichier par ligne
        // affichage du graphe choisi

        while (scannerFile.hasNextLine()){
            Main.txtFile.add(scannerFile.nextLine());
            System.out.println(txtFile.get(i));
            i++;
        }

        int nbSommets = parseInt(txtFile.get(0)); // récupération du nombre de sommet du graphe

        // Initialisation d'un objet Graphe
        Graphe graphe = new Graphe(nbSommets);

        // decomposition des lignes du graphes pour les convertir en entier

        LinkedList<String[]>strLignes = new LinkedList<String[]>();
            for(int a = 2; a <= txtFile.size()-1; a++ ){
                strLignes.add(txtFile.get(a).split(" "));
            }

        int index = 0;
        int tailleTabLigneTxt = 0;

        for (String[] s : strLignes) {
            tailleTabLigneTxt+=s.length;
        }
        // on stockera toute les chaines de caractères qui ont été découpé dans un tableau d'entier
        Integer[] valeurInteger = new Integer[tailleTabLigneTxt];
        // conversion de chaque chaine de caractère en entier et ajout dans notre tableau d'entier
        for(String[] s : strLignes){
            if(s.length > 0){
                for(String s1 : s){
                    valeurInteger[index] = Integer.parseInt(s1);
                    index++;
                }
            }
        }

        System.out.println(Arrays.toString(valeurInteger));

        // decoupage de notre tableau stockaant toutes les valeurs entières en petit tableau de taille 3
        // stockage dans une liste de tableau d'entier
        List<Integer[]>ligneEnTabInt = splitArray(valeurInteger,3);

        int source = 0;
        int destination = 1;
        int poids = 2;
        // Ajouts des arcs du graohe du fichier dans notre objet Graphe
        // en accedant a chaque petit tableau contenu dans notre liste de petit tableau d'entier
        for(Integer[] arc: ligneEnTabInt){
            graphe.addArc(arc[source],arc[destination],arc[poids]);
        }

        graphe.ajoutDeINF();
        graphe.initialisationChemin();
        // Affichage de la matrice de valeur et la matrice d'adjacence du graphe choisi
        System.out.println("\nMatrice de valeur du graphe numéro " + choixGraphe + ":");
        graphe.printMatriceValeur(graphe.getMatriceValeur());
        System.out.println("\nMatrice d'adjacence du graphe numéro " + choixGraphe + ":");
        graphe.printMatriceAdjacence();
        System.out.println("\nApplication de l'algorithme de Floyd Warshall : \n");
        graphe.floydWarshall(graphe.getMatriceValeur());

        System.out.println("Le graphe possède-t-il un circuit absorbant?");
        if(graphe.hasCircuitAbsorbant(graphe.getMatriceValeur())){
            System.out.println("Oui");
            System.out.println("Fin du programme.");

        }else {
            System.out.println("Non");

            boolean choixAffichageChemin = false;
            while(!choixAffichageChemin){
                System.out.println("Voulez vous afficher le plus court chemin entre 2 points ? Y/N");
                String reponse = sc.nextLine();
                if(reponse.trim().equals("Y") || reponse.trim().equals("y")){
                    boolean bonChoixDepartSommet = false;
                    int departChemin = 0;
                    while(!bonChoixDepartSommet){
                        System.out.println("Veuillez choisir le sommet de départ du chemin entre 0 et " + (nbSommets - 1) + " :");
                        departChemin = sc.nextInt();
                        sc.nextLine();
                        if(departChemin <= nbSommets && departChemin >= 0 ){
                            bonChoixDepartSommet = true;
                        }else {
                            bonChoixDepartSommet = false;
                        }
                    }

                    boolean bonChoixDestinationSommet = false;
                    int destinationChemin = 0;
                    while (!bonChoixDestinationSommet){
                        System.out.println("Veuillez choisir le sommet de destination du chemin entre 0 et " + (nbSommets -1)+ " :");
                        destinationChemin = sc.nextInt();
                        sc.nextLine();
                        if(destinationChemin <= (nbSommets -1) && destinationChemin >= 0 ){
                            bonChoixDestinationSommet = true;
                        }else {
                            bonChoixDestinationSommet = false;
                        }
                    }

                    LinkedList<Integer> path;
                    path = graphe.constructionChemin(departChemin, destinationChemin);
                    System.out.println("Chemin le plus court entre : " + departChemin + " et " +destinationChemin+" :");
                    graphe.printChemin(path);

                    System.out.println("Voulez vous afficher un autre chemin le plus court entre 2 sommets? Y/N");
                    String continuer = sc.nextLine();
                    System.out.println("reiteration :");
                    if(continuer.trim().equals("Y")  || continuer.trim().equals("y") ){
                        choixAffichageChemin = false;

                    }
                    if(continuer.trim().equals("N") || continuer.trim().equals("n")){
                        choixAffichageChemin = true;
                        System.out.println("Fin du programme.");
                    }
                }
                if(reponse.trim().equals("N") || reponse.trim().equals("n")){
                    System.out.println("Fin du programme.");
                    choixAffichageChemin = true;
                }
            }
        }
    }
}
