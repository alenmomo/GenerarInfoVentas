import java.io.*;
import java.util.*;

/**
 * Módulo: Conceptos Fundamentales de Programación
 * Entrega 2 (Semana 5) - Clase Principal de Procesamiento
 * Esta clase lee los archivos generados, procesa totales y genera reportes CSV.
 * @author Daniela Escobar, Alvaro Enrique moreno
 */
public class main {

    public static void main(String[] args) {
        // Estructuras para almacenar datos en memoria
        Map<Integer, String[]> infoProductos = new HashMap<>(); // ID -> [Nombre, Precio]
        Map<Long, Double> recaudadoPorVendedor = new HashMap<>(); // ID Vendedor -> Total Dinero
        Map<Long, String> nombresVendedores = new HashMap<>(); // ID Vendedor -> Nombre Completo
        Map<Integer, Integer> cantidadesPorProducto = new HashMap<>(); // ID Producto -> Cantidad Total

        try {
            // 1. Cargar información de productos
            cargarProductos(infoProductos, cantidadesPorProducto);

            // 2. Cargar información de vendedores
            cargarVendedores(nombresVendedores, recaudadoPorVendedor);

            // 3. Procesar archivos de ventas
            procesarVentas(recaudadoPorVendedor, infoProductos, cantidadesPorProducto);

            // 4. Generar Reportes
            generarReporteVendedores(nombresVendedores, recaudadoPorVendedor);
            generarReporteProductos(infoProductos, cantidadesPorProducto);

            System.out.println("Finalización exitosa: Reportes generados correctamente.");

        } catch (Exception e) {
            System.err.println("Error en el procesamiento: " + e.getMessage());
        }
    }

    private static void cargarProductos(Map<Integer, String[]> productos, Map<Integer, Integer> cantidades) throws IOException {
        File file = new File("productos_info.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                // datos[0]=ID, datos[1]=Nombre, datos[2]=Precio
                productos.put(Integer.parseInt(datos[0]), new String[]{datos[1], datos[2]});
                cantidades.put(Integer.parseInt(datos[0]), 0); // Inicializar contador de ventas
            }
        }
    }

    private static void cargarVendedores(Map<Long, String> nombres, Map<Long, Double> totales) throws IOException {
        File file = new File("vendedores_info.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                // datos[1]=Documento, datos[2]=Nombre, datos[3]=Apellido
                long id = Long.parseLong(datos[1]);
                nombres.put(id, datos[2] + " " + datos[3]);
                totales.put(id, 0.0); // Inicializar recaudado
            }
        }
    }

    private static void procesarVentas(Map<Long, Double> totales, Map<Integer, String[]> productos, Map<Integer, Integer> cantidades) {
        File carpetaActual = new File(".");
        File[] archivosVentas = carpetaActual.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

        if (archivosVentas == null) return;

        for (File archivo : archivosVentas) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String primeraLinea = br.readLine(); // TipoDoc;ID
                if (primeraLinea == null) continue;
                long idVendedor = Long.parseLong(primeraLinea.split(";")[1]);

                String lineaVenta;
                double totalVentaVendedor = 0;

                while ((lineaVenta = br.readLine()) != null) {
                    String[] datos = lineaVenta.split(";");
                    int idProd = Integer.parseInt(datos[0]);
                    int cant = Integer.parseInt(datos[1]);

                    // Calcular dinero
                    if (productos.containsKey(idProd)) {
                        double precio = Double.parseDouble(productos.get(idProd)[1]);
                        totalVentaVendedor += (precio * cant);
                        // Sumar a cantidad total de productos vendidos
                        cantidades.put(idProd, cantidades.get(idProd) + cant);
                    }
                }
                totales.put(idVendedor, totales.getOrDefault(idVendedor, 0.0) + totalVentaVendedor);
            } catch (IOException e) {
                System.err.println("Error leyendo archivo " + archivo.getName());
            }
        }
    }

    private static void generarReporteVendedores(Map<Long, String> nombres, Map<Long, Double> totales) throws IOException {
        List<Map.Entry<Long, Double>> lista = new ArrayList<>(totales.entrySet());
        // Ordenar de mayor a menor dinero recaudado
        lista.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        try (PrintWriter writer = new PrintWriter(new File("reporte_vendedores.csv"))) {
            for (Map.Entry<Long, Double> entrada : lista) {
                writer.println(nombres.get(entrada.getKey()) + ";" + entrada.getValue());
            }
        }
    }

    private static void generarReporteProductos(Map<Integer, String[]> productos, Map<Integer, Integer> cantidades) throws IOException {
        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(cantidades.entrySet());
        // Ordenar de mayor a menor cantidad vendida
        lista.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        try (PrintWriter writer = new PrintWriter(new File("reporte_productos.csv"))) {
            for (Map.Entry<Integer, Integer> entrada : lista) {
                String nombre = productos.get(entrada.getKey())[0];
                String precio = productos.get(entrada.getKey())[1];
                writer.println(nombre + ";" + precio + ";" + entrada.getValue());
            }
        }
    }
}
