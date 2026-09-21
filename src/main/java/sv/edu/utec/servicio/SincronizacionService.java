package sv.edu.utec.servicio;

import sv.edu.utec.api.ProveedorApi;
import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SincronizacionService {

    private ProveedorApi proveedorApi;
    private ProductoDAO dao;

    public SincronizacionService(ProveedorApi proveedorApi, ProductoDAO dao) {
        this.proveedorApi = proveedorApi;
        this.dao = dao;
    }

    public int[] sincronizar(int limite)
            throws IOException, InterruptedException, SQLException {

        List<Producto> productos = proveedorApi.obtenerProductos(limite);

        int insertados = 0;
        int actualizados = 0;

        for (Producto producto : productos) {

            if (dao.existe(producto.getId())) {

                dao.actualizar(producto);
                actualizados++;

            } else {

                dao.insertar(producto);
                insertados++;
            }
        }

        return new int[]{insertados, actualizados};
    }
}