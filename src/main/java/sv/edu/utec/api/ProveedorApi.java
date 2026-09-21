package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorApi {

    public List<Producto> obtenerProductos(int limite)
            throws IOException, InterruptedException {

        String url = "https://dummyjson.com/products?limit=" +
                limite + "&select=title,stock";

        HttpClient cliente = HttpClient.newHttpClient();

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        if (respuesta.statusCode() != 200) {
            throw new IOException(
                    "Error al consultar la API. Codigo HTTP: " +
                            respuesta.statusCode()
            );
        }

        ObjectMapper mapper = new ObjectMapper();

        RespuestaProductos datos = mapper.readValue(
                respuesta.body(),
                RespuestaProductos.class
        );

        List<Producto> productos = new ArrayList<>();

        for (ProductoApi productoApi : datos.getProducts()) {
            productos.add(productoApi.aProducto());
        }

        return productos;
    }
}