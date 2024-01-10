import java.util.*;
import java.io.*;
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

    public void escribirTrazaEnArchivo(String nombreArchivo) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            // Se escribe la cantidad mínima en la primera línea.

            bw.write(String.format(nombreArchivo));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            // Escribir información del edificio original
            writer.write(nombreArchivo);
            escribirEdificioEnArchivo(writer, edificio);

            // Ejecutar el algoritmo para encontrar el tornillo y escribir la traza
            encontrarTornilloDesdePosicion(posicionActual[0], posicionActual[1]);

            // Escribir resultado del algoritmo
            writer.write("\nResultado del algoritmo:\n");
            escribirEdificioEnArchivo(writer, edificio);

            // Escribir camino desde el tornillo hasta la salida
            writer.write("Camino desde el tornillo hasta la salida:\n");
            for (int[] posicion : camino) {
                writer.write("F(" + posicion[0] + ", " + posicion[1] + ")\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    private void escribirEdificioEnArchivo(BufferedWriter writer, char[][] edificio) throws IOException {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                writer.write(edificio[i][j] + " ");
            }
            writer.write("\n");
        }
    }


    public static void main(String[] args) {
        //Bloque para leer archivo de entrada
        //-----------------------------------------------------------------------------------
        char[][] edificio = {
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'E', 'L', 'E', 'L'},
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'L', 'L', 'E', 'L'},
                {'L', 'E', 'L', 'L', 'T'}
        };
        boolean trazar = false;
        Robot robot = new Robot(edificio);

        Pattern pattern = Pattern.compile("^.+\\.txt$");
        Matcher matcher = pattern.matcher(args[0]);

        char[][] edificioTxt;
        edificioTxt = leerEdificioDesdeArchivo(args[0]);

        for(int i=0; i<args.length; i++){
            if(args[i].equals("-h")){
                ayuda();
                return;
            }else if(args[i].equals("-t")){
                trazar = true;
                imprimirPantalla(edificio, trazar);
                //robot.encontrarTornillo();
            }else if(matcher.find()){
                trazar = false;
                imprimirPantalla(edificioTxt, trazar);
                //robot.encontrarTornillo();
                if(args[i].equals("-t") && matcher.find()){
                    trazar = true;
                    imprimirPantalla(edificioTxt, trazar);
                    //robot.encontrarTornillo();
                }
            }else if(matcher.find() && !args[1].isEmpty()){
                char[][] edificioSalida;
                trazar = false;
                imprimirPantalla(edificioTxt, trazar);
                robot.escribirTrazaEnArchivo(args[1]);
                if(args[i].equals("-t") && matcher.find() && !args[1].isEmpty()){
                    trazar = true;
                    imprimirPantalla(edificioTxt, trazar);
                    robot.escribirTrazaEnArchivo(args[1]);
                }
            }
            trazar = false;
        }

        /*
        char[][] edificioTxt;
        edificioTxt = leerEdificioDesdeArchivo(args[0]);
        Robot robot = new Robot(edificioTxt);

        imprimirPantalla(edificioTxt, true);
        robot.encontrarTornillo();
        */



        /*
        // Verificar si se proporcionó al menos un argumento
        if (args.length < 1) {
            System.out.println("Debes proporcionar al menos un argumento.");
            return;
        }

        // Expresión regular para verificar que el argumento no sea "-t" ni "-h"
        String regex = "^(?!-t$|-h$).*$";

        // Iterar sobre los argumentos
        for (String arg : args) {
            // Verificar si el argumento cumple con la expresión regular
            if (Pattern.matches(regex, arg)) {
                // El argumento es diferente de "-t" y "-h", puedes usarlo como argumento
                edificioTxt = leerEdificioDesdeArchivo(arg);
                // Hacer algo con el edificioTxt
            } else {
                // El argumento es "-t" o "-h"
                System.out.println("Argumento no válido: " + arg);
            }
        }
        //-----------------------------------------------------------------------------------


        char[][] edificio = {
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'E', 'L', 'E', 'L'},
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'L', 'L', 'E', 'L'},
                {'L', 'E', 'L', 'L', 'T'}
        };

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
        */
    }

    public static void imprimirPantalla(char[][] edificio, boolean traza){
        //EDIFICIO ESTATICO
        Robot robot = new Robot(edificio);
        List<int[]> camino = robot.encontrarTornillo();
        if(!traza){
            //no se imprime
        }else{
            robot.trazarAlgoritmo(edificio);
        }
    }
}