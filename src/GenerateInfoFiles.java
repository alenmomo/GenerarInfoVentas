import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Módulo: Conceptos Fundamentales de Programación
 * Entrega 1 - GenerateInfoFiles (Semana 3)
 * Genera archivos planos para el proyecto de ventas.
 */
public class GenerateInfoFiles {

  // 1. Datos para generar información coherente
  private static final String[] NOMBRES = {"Juan", "Maria", "Carlos", "Ana", "Luis", "Elena"};
  private static final String[] APELLIDOS = {"Perez", "Gomez", "Rodriguez", "Lopez", "Garcia", "Sánchez"};
  private static final String[] PRODUCTOS = {"Laptop", "Mouse", "Teclado", "Monitor", "Webcam", "Impresora"};

  // 2. Método Main: El motor que arranca todo
  public static void main(String[] args) {
    try {
      // Generamos 10 productos y 5 vendedores
      createProductsFile(10);
      createSalesManInfoFile(5);

      System.out.println("Finalización exitosa: Los archivos planos han sido generados.");
    } catch (Exception e) {
      System.err.println("Error en la ejecución: " + e.getMessage());
    }
  }

  // 3. Método para crear el archivo de PRODUCTOS
  public static void createProductsFile(int productsCount) {
    try (PrintWriter writer = new PrintWriter(new File("productos_info.txt"))) {
      Random rand = new Random();
      for (int i = 1; i <= productsCount; i++) {
        String nombre = PRODUCTOS[rand.nextInt(PRODUCTOS.length)] + " Mod-" + i;
        int precio = 5000 + rand.nextInt(95000);
        writer.println(i + ";" + nombre + ";" + precio);
      }
    } catch (IOException e) {
      System.err.println("Error al crear productos: " + e.getMessage());
    }
  }

  // 4. Método para crear el archivo de VENDEDORES
  public static void createSalesManInfoFile(int salesmanCount) {
    try (PrintWriter writer = new PrintWriter(new File("vendedores_info.txt"))) {
      Random rand = new Random();
      for (int i = 0; i < salesmanCount; i++) {
        long documento = 1000000000L + rand.nextInt(900000000);
        String nombre = NOMBRES[rand.nextInt(NOMBRES.length)];
        String apellido = APELLIDOS[rand.nextInt(APELLIDOS.length)];

        writer.println("CC;" + documento + ";" + nombre + ";" + apellido);

        // Crea automáticamente el archivo de ventas para este vendedor
        createSalesMenFile(rand.nextInt(5) + 1, "CC", documento);
      }
    } catch (IOException e) {
      System.err.println("Error al crear vendedores: " + e.getMessage());
    }
  }

  // 5. Método para crear los archivos individuales de VENTAS
  public static void createSalesMenFile(int randomSalesCount, String typeId, long id) {
    String fileName = "ventas_" + id + ".txt";
    try (PrintWriter writer = new PrintWriter(new File(fileName))) {
      writer.println(typeId + ";" + id);
      Random rand = new Random();
      for (int i = 0; i < randomSalesCount; i++) {
        int idProd = rand.nextInt(10) + 1;
        int cant = rand.nextInt(20) + 1;
        writer.println(idProd + ";" + cant + ";");
      }
    } catch (IOException e) {
      System.err.println("Error al crear archivo de ventas: " + e.getMessage());
    }
  }
}