import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.BufferedReader;



public class RobotSolver {
    private static char[][] edificio;
    private int filas, columnas;
    private int[] posicionActual;
    public List<int[]> camino;

    public List<int[]> getCamino() {
        return camino;
    }

    public RobotSolver(char[][] edificio) {
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
            this.camino.add(new int[]{fila, columna});
            return true;
        }

        if (edificio[fila][columna] == 'E' || edificio[fila][columna] == 'V') {
            return false;
        }

        edificio[fila][columna] = 'V';
        this.camino.add(new int[]{fila, columna});

        if (encontrarTornilloDesdePosicion(fila - 1, columna) ||
                encontrarTornilloDesdePosicion(fila + 1, columna) ||
                encontrarTornilloDesdePosicion(fila, columna - 1) ||
                encontrarTornilloDesdePosicion(fila, columna + 1)) {
            return true;
        }

        //camino.remove(camino.size() - 1);
        return false;
    }

    private static void ayuda() {
        System.out.println("SINTAXIS: java RobotSolver [-t][-h] [fichero entrada] [fichero salida]");
        System.out.println("-t Traza el algoritmo");
        System.out.println("-h Muestra esta ayuda");
        System.out.println("[fichero entrada] Nombre del fichero de entrada");
        System.out.println("[fichero salida] Nombre del fichero de salida");
    }

    private void trazarAlgoritmo(char[][] edificio) {
        char[][] copiaEdificio = new char[filas][columnas];

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
        if (this.camino.size() == 0) {
            System.out.println("Vacio");
        } else {
            for (int[] posicion : getCamino()) {
                System.out.println("F(" + posicion[0] + ", " + posicion[1] + ")");
            }
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
        Random random = new Random();
        char[] caracteresPosibles = {'L', 'E', 'T'};
        char[][] edificio = new char[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int indiceCaracter = random.nextInt(caracteresPosibles.length);
                edificio[i][j] = caracteresPosibles[indiceCaracter];
            }
        }

        int filaTornillo = random.nextInt(filas);
        int columnaTornillo = random.nextInt(columnas);
        edificio[filaTornillo][columnaTornillo] = 'T';

        return edificio;
    }

    private static char[][] leerEdificioDesdeArchivo(String fileName) {
        Pattern pattern = Pattern.compile("^.+\\.txt$");
        Matcher matcher = pattern.matcher(fileName);

        if (!matcher.matches()) {
            System.err.println("El nombre del archivo debe tener la extensión .txt");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int filas = Integer.parseInt(br.readLine());
            int columnas = Integer.parseInt(br.readLine());

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
            bw.write(String.format(nombreArchivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void guardarResultadoEnArchivo(RobotSolver robot,String nombreArchivoSalida, char[][] edificio) {
        try {
            File archivo = new File(nombreArchivoSalida);

            // Verifica si el archivo ya existe
            if (archivo.exists()) {
                System.out.println("El archivo " + archivo.getName() + " ya existe. No se creará.");
            } else {
                // Crea el archivo
                if (archivo.createNewFile() && !archivo.exists()) {
                    System.out.println("Archivo creado: " + archivo.getName());
                }
            }

            try (FileWriter fw = new FileWriter(archivo)) {
                if (robot.getCamino().isEmpty()) {
                    fw.write("No se encontró un camino hasta el tornillo.\n");
                } else {
                    for (int[] x : robot.getCamino()) {
                        String linea = String.format("(%d, %d)\n", x[0], x[1]);
                        System.out.print(linea);
                        fw.write(linea);
                    }
                }
            }

            System.out.println("Solución escrita en: " + archivo.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    private void imprimirEdificioEnArchivo(BufferedWriter writer, char[][] edificio) throws IOException {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                writer.write(edificio[i][j] + " ");
            }
            writer.write("\n");
        }
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
        char[][] edificioInicial = {
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'E', 'L', 'E', 'L'},
                {'L', 'L', 'E', 'L', 'L'},
                {'L', 'L', 'L', 'E', 'L'},
                {'L', 'E', 'L', 'L', 'T'}
        };

        boolean trazar = false;
        String regexTxt = "^.+\\.txt$";
        Pattern pattern = Pattern.compile(regexTxt);
        String archivoEntrada = null;
        String archivoSalida = null;
        RobotSolver robot1 = null;//Creacion del robot
        for (int i = 0; i < args.length; i++) {
            Matcher matcher = pattern.matcher(args[i]);

            if (args[i].equals("-t")) {
                trazar = true;
            }

            if (args[i].equals("-h")) {
                ayuda();
                return;
            }

            if (matcher.matches()) {
                if (archivoEntrada == null) {
                    archivoEntrada = args[i];
                    char[][] edificioEntrada = leerEdificioDesdeArchivo(archivoEntrada);
                    robot1 = new RobotSolver(edificioEntrada);
                    robot1.imprimirEdificio();
                    imprimirPantalla(robot1,edificioEntrada, trazar);
                } else if (archivoSalida == null) {
                    archivoSalida = args[i];
                    robot1.guardarResultadoEnArchivo(robot1,archivoSalida, edificio);
                    System.out.println("Traza guardada en  " + archivoSalida);
                }
            }
        }
    }

    public static void imprimirPantalla(RobotSolver robot,char[][] edificio, boolean traza) {

        List<int[]> camino = robot.encontrarTornillo();
        if (!traza) {
            // No se imprime
        } else {
            robot.trazarAlgoritmo(edificio);
        }
    }
}
