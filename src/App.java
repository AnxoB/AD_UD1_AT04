import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static Map<Character, Character> codificar = new HashMap<>();
    public static Map<Character, Character> decodificar = new HashMap<>();

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.mostrarMenu();
    }

    public void almacenarCodex(String ruta){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            String orginal = reader.readLine();
            String codificado = reader.readLine();

            for (int i = 0; i < orginal.length(); i++) {
                codificar.put(orginal.charAt(i), codificado.charAt(i));
                decodificar.put(codificado.charAt(i), orginal.charAt(i));
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo");
        }
    }

    public char valorEncriptado(char caracter, Map<Character, Character> codificar){
        return codificar.get(caracter);
    }

    public char valorDesencriptado(char caracter, Map<Character, Character> decodificar){
        return decodificar.getOrDefault(caracter, caracter);
    }

    public void encriptar(String rutaFichero){
        try {
            String rutaSalida = generarNombreArchivoCodificado(rutaFichero);
            BufferedReader reader = new BufferedReader(new FileReader(rutaFichero));
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida));
            
            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (!primeraLinea) {
                    writer.newLine();
                }
                
                for (int i = 0; i < linea.length(); i++) {
                    char caracter = linea.charAt(i);
                    Character encriptado = codificar.get(caracter);
                    writer.write(encriptado != null ? encriptado : caracter);
                }
                primeraLinea = false;
            }
            
            reader.close();
            writer.close();
            System.out.println("Archivo encriptado: " + rutaSalida);
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Método auxiliar para generar el nombre del archivo codificado
    private String generarNombreArchivoCodificado(String rutaOriginal) {
        int puntoIndex = rutaOriginal.lastIndexOf('.');
        
        if (puntoIndex == -1) {
            // Sin extensión: archivo -> archivo_codificado
            return rutaOriginal + "_codificado";
        } else {
            // Con extensión: archivo.txt -> archivo_codificado.txt
            String nombreSinExtension = rutaOriginal.substring(0, puntoIndex);
            String extension = rutaOriginal.substring(puntoIndex);
            return nombreSinExtension + "_codificado" + extension;
        }
    }

    /**
     * Método que desencripta un archivo completo y genera un nuevo archivo decodificado
     * @param rutaFichero La ruta del archivo a desencriptar
     */
    public void desencriptar(String rutaFichero){
        try {
            String rutaSalida = generarNombreArchivoDecodificado(rutaFichero);
            BufferedReader reader = new BufferedReader(new FileReader(rutaFichero));
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida));
            
            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (!primeraLinea) {
                    writer.newLine();
                }
                
                for (int i = 0; i < linea.length(); i++) {
                    char caracter = linea.charAt(i);
                    char desencriptado = valorDesencriptado(caracter, decodificar);
                    writer.write(desencriptado);
                }
                primeraLinea = false;
            }
            
            reader.close();
            writer.close();
            System.out.println("Archivo desencriptado: " + rutaSalida);
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Método auxiliar para generar el nombre del archivo decodificado
    private String generarNombreArchivoDecodificado(String rutaOriginal) {
        int puntoIndex = rutaOriginal.lastIndexOf('.');
        
        if (puntoIndex == -1) {
            // Sin extensión: archivo -> archivo_decodificado
            return rutaOriginal + "_decodificado";
        } else {
            // Con extensión: archivo.txt -> archivo_decodificado.txt
            String nombreSinExtension = rutaOriginal.substring(0, puntoIndex);
            String extension = rutaOriginal.substring(puntoIndex);
            return nombreSinExtension + "_decodificado" + extension;
        }
    }

    /**
     * Método que muestra el menú principal y gestiona las opciones del usuario
     */
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("SISTEMA DE ENCRIPTACIÓN");
        almacenarCodex("codex.txt");
        System.out.println("Códex cargado: " + codificar.size() + " caracteres");

        while (true) {
            mostrarOpcionesMenu();
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    encriptarCaracter(scanner);
                    break;
                case 2:
                    desencriptarCaracter(scanner);
                    break;
                case 3:
                    encriptarArchivo(scanner);
                    break;
                case 4:
                    desencriptarArchivo(scanner);
                    break;
                case 5:
                    mostrarInformacionCodex();
                    break;
                case 6:
                    System.out.println("Hasta luego");
                    return;
            }
            System.out.println();
        }
    }

    /**
     * Muestra las opciones del menú
     */
    private void mostrarOpcionesMenu() {
        System.out.println("1. Encriptar carácter");
        System.out.println("2. Desencriptar carácter");
        System.out.println("3. Encriptar archivo");
        System.out.println("4. Desencriptar archivo");
        System.out.println("5. Info códex");
        System.out.println("6. Salir");
    }

    /**
     * Gestiona la encriptación de un carácter individual
     */
    private void encriptarCaracter(Scanner scanner) {
        System.out.print("Carácter: ");
        char caracter = scanner.nextLine().charAt(0);
        Character encriptado = codificar.get(caracter);
        System.out.println(caracter + " → " + (encriptado != null ? encriptado : caracter));
    }

    /**
     * Gestiona la desencriptación de un carácter individual
     */
    private void desencriptarCaracter(Scanner scanner) {
        System.out.print("Carácter: ");
        char caracter = scanner.nextLine().charAt(0);
        char desencriptado = valorDesencriptado(caracter, decodificar);
        System.out.println(caracter + " → " + desencriptado);
    }

    /**
     * Gestiona la encriptación de un archivo
     */
    private void encriptarArchivo(Scanner scanner) {
        System.out.print("Ruta archivo: ");
        String rutaArchivo = scanner.nextLine();
        encriptar(rutaArchivo);
    }

    /**
     * Gestiona la desencriptación de un archivo
     */
    private void desencriptarArchivo(Scanner scanner) {
        System.out.print("Ruta archivo: ");
        String rutaArchivo = scanner.nextLine();
        desencriptar(rutaArchivo);
    }

    /**
     * Muestra información sobre el códex cargado
     */
    private void mostrarInformacionCodex() {
        System.out.println("Caracteres: " + codificar.size());
        int contador = 0;
        for (Map.Entry<Character, Character> entry : codificar.entrySet()) {
            if (contador >= 10) break;
            System.out.println(entry.getKey() + " → " + entry.getValue());
            contador++;
        }
    }
}
