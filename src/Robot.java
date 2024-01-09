import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Robot {
    private char[][] edificio;
    private int filas, columnas;
    private int[] posicionActual;
    private List<int[]> camino;

    public Robot(char[][] edificio) {
        this.edificio = edificio;
        this.filas = edificio.length;
        this.columnas = edificio[0].length;
        this.posicionActual = new int[]{0, 0}; // Iniciar en la casilla (0, 0)
        this.camino = new ArrayList<>();
    }

    public List<int[]> encontrarTornillo() {
        encontrarTornilloDesdePosicion(posicionActual[0], posicionActual[1]);
        return camino;
    }

    private boolean encontrarTornilloDesdePosicion(int fila, int columna) {
        if (fila < 0 || columna < 0 || fila >= filas || columna >= columnas) {
            return false;
        }

        if (edificio[fila][columna] == 'T') {
            System.out.println("Tornillo encontrado en la posicion: (" + fila + ", " + columna + ")!");
            camino.add(new int[]{fila, columna});
            return true;
        }

        if (edificio[fila][columna] == 'E' || edificio[fila][columna] == 'V') {
            return false;
        }

        edificio[fila][columna] = 'V';
        camino.add(new int[]{fila, columna});

        if (encontrarTornilloDesdePosicion(fila - 1, columna) ||
                encontrarTornilloDesdePosicion(fila + 1, columna) ||
                encontrarTornilloDesdePosicion(fila, columna - 1) ||
                encontrarTornilloDesdePosicion(fila, columna + 1)) {
            return true;
        }

        // Si no se encuentra el tornillo desde la posicion actual, retroceder
        camino.remove(camino.size() - 1);
        return false;
    }

    private static void ayuda() {
        System.out.println("|Orientaciones para el tutor");
        System.out.println("UNIVERSIDAD NACIONAL DE EDUCACIÓN A DISTANCIA 3");
        System.out.println("SINTAXIS: java Robot [-t][-h] [fichero entrada] [fichero salida]");
        System.out.println("-t Traza el algoritmo");
        System.out.println("-h Muestra esta ayuda");
        System.out.println("[fichero entrada] Nombre del fichero de entrada");
        System.out.println("[fichero salida] Nombre del fichero de salida");
    }

    private void trazarAlgoritmo(char[][] edificio) {
        char[][] copiaEdificio = new char[filas][columnas];
        //copiaEdificio = edificio;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                copiaEdificio[i][j] = edificio[i][j];
            }
        }

        System.out.println("Edificio original:");
        imprimirEdificio();

        encontrarTornilloDesdePosicion(posicionActual[0], posicionActual[1]);

        System.out.println("\nResultado del algoritmo:");
        imprimirEdificio();
        System.out.println("Camino desde el tornillo hasta la salida:");
        for (int[] posicion : camino) {
            System.out.println("F(" + posicion[0] + ", " + posicion[1] + ")");
        }
    }

    private void imprimirEdificio() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(edificio[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static char[][] generarEdificioAleatorio(int filas, int columnas) {
        // Crear una instancia de Random para generar valores aleatorios
        Random random = new Random();

        // Definir los caracteres posibles en el edificio (L, E, T)
        char[] caracteresPosibles = {'L', 'E', 'T'};
        char[][] edificio = new char[filas][columnas];

        // Inicializar el edificio con caracteres aleatorios
        //edificio = new char[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int indiceCaracter = random.nextInt(caracteresPosibles.length);
                edificio[i][j] = caracteresPosibles[indiceCaracter];
            }
        }

        // Colocar el tornillo en una posición aleatoria
        int filaTornillo = random.nextInt(filas);
        int columnaTornillo = random.nextInt(columnas);
        edificio[filaTornillo][columnaTornillo] = 'T';

        return edificio;
        /*
        // Colocar el robot en la posición inicial (0, 0)
        posicionActual = new int[]{0, 0};

        // Limpiar la lista de camino
        camino.clear();
        */
    }

    private static char[][] leerEdificioDesdeArchivo(String fileName) {
        // Patrón que verifica si el nombre del archivo tiene una extensión .txt
        Pattern pattern = Pattern.compile("^.+\\.txt$");
        Matcher matcher = pattern.matcher(fileName);

        if (!matcher.matches()) {
            System.err.println("El nombre del archivo debe tener la extensión .txt");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Leer filas y columnas
            int filas = Integer.parseInt(br.readLine());
            int columnas = Integer.parseInt(br.readLine());

            // Leer edificio
            char[][] edificio = new char[filas][columnas];
            for (int i = 0; i < filas; i++) {
                String line = br.readLine();
                edificio[i] = line.replaceAll("\\s+", "").toCharArray();
            }

            return edificio;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }


    /*
    public void generarEdificioAleatorio(int filas, int columnas) {
        // Crear una instancia de Random para generar valores aleatorios
        Random random = new Random();

        // Definir los caracteres posibles en el edificio (L, E, T)
        char[] caracteresPosibles = {'L', 'E', 'T'};

        // Inicializar el edificio con caracteres aleatorios
        edificio = new char[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int indiceCaracter = random.nextInt(caracteresPosibles.length);
                edificio[i][j] = caracteresPosibles[indiceCaracter];
            }
        }

        // Colocar el tornillo en una posición aleatoria
        int filaTornillo = random.nextInt(filas);
        int columnaTornillo = random.nextInt(columnas);
        edificio[filaTornillo][columnaTornillo] = 'T';

        // Colocar el robot en la posición inicial (0, 0)
        posicionActual = new int[]{0, 0};

        // Limpiar la lista de camino
        camino.clear();
    }
    */


    public static void main(String[] args) {
        //int filas = 6;
        //int columnas = 5;
        //char[][] edificio = Robot.generarEdificioAleatorio(filas, columnas);



        char[][] edificio = {
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'E', 'L', 'E', 'L'},
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'L', 'L', 'E', 'L'},
                {'L', 'E', 'L', 'L', 'T'}
        };

        //Robot robot = new Robot(edificio);
        //boolean trazar = false;

        Robot robot = new Robot(edificio);
        boolean trazar = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                trazar = true;
            } else if (args[i].equals("-h")) {
                ayuda();
                return;  // Agrega un retorno para evitar ejecutar el resto del programa si se muestra la ayuda
            }
        }

        imprimirPantalla(edificio, trazar);
        robot.encontrarTornillo();

        if (!robot.camino.isEmpty()) {
            System.out.println("Camino desde el tornillo hasta la salida:");
            for (int[] posicion : robot.camino) {
                System.out.println("(" + posicion[0] + ", " + posicion[1] + ")");
            }
        } else {
            System.out.println("El robot no ha encontrado el tornillo.");
        }


        /*
        imprimirPantalla();
        */
        /*
        // Iterar sobre los argumentos
        for (String arg : args) {
            // Comparar usando equals
            if (arg.equals("-h")) {
                ayuda();
            } else if (arg.equals("-t")) {

                System.out.println("Se proporcionó la opción:" + arg);
            }else if(arg.equals("a")){
                imprimirPantalla();

            } else {

                // Otro procesamiento para argumentos no reconocidos
                System.out.println("Argumento no reconocido: " + arg);
            }
        }
        */

        /*
        // FUNCIONA SIN ARGUMENTOS
        char[][] edificio = {
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'E', 'L', 'E', 'L'},
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'L', 'L', 'E', 'L'},
                {'L', 'E', 'L', 'L', 'T'}
        };

        Robot robot = new Robot(edificio);
        List<int[]> camino = robot.encontrarTornillo();

        if (!camino.isEmpty()) {
            System.out.println("Camino desde el tornillo hasta la salida:");
            for (int[] posicion : camino) {
                System.out.println("(" + posicion[0] + ", " + posicion[1] + ")");
            }
        } else {
            System.out.println("El robot no ha encontrado el tornillo.");
        }
        */
    }

    public static void imprimirPantalla(char[][] edificio, boolean traza){
        /* //EDIFICIO ALEATORIO
        int filas = 5;  // Definir el número de filas del edificio
        int columnas = 5;  // Definir el número de columnas del edificio

        Robot robot = new Robot(new char[filas][columnas]);
        robot.generarEdificioAleatorio(filas, columnas);

        // Trazar el algoritmo
        robot.trazarAlgoritmo();
        */
        /*
        char[][] edificio = {
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'E', 'L', 'E', 'L'},
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'L', 'L', 'E', 'L'},
                {'L', 'E', 'L', 'L', 'T'}
        };
        */

        //EDIFICIO ESTATICO
        Robot robot = new Robot(edificio);
        List<int[]> camino = robot.encontrarTornillo();
        if(!traza){

        }else{
            robot.trazarAlgoritmo(edificio);
        }

        // Trazar el algoritmo
        //Robot robot = new Robot(edificio);
        //robot.trazarAlgoritmo();
        /*
        if (!camino.isEmpty()) {
            System.out.println("Camino desde el tornillo hasta la salida:");
            for (int[] posicion : camino) {
                System.out.println("(" + posicion[0] + ", " + posicion[1] + ")");
            }
        } else {
            System.out.println("El robot no ha encontrado el tornillo.");
        }*/
    }
}