package es.udc.ws.app.thriftservice;
import es.udc.ws.app.model.compra.Sale;
import es.udc.ws.app.thrift.ThriftCompraDto;

import java.util.ArrayList;
import java.util.List;

public class CompraToThriftCompraDtoConversor {

    public static String obtenerUltimosCuatroDigitos(String numero) {
        // Verificar si la cadena es nula o tiene menos de cuatro dígitos
        if (numero == null || numero.length() < 4) {
            return "Número no válido";
        }

        // Obtener los últimos cuatro dígitos
        int longitud = numero.length();
        String ultimosCuatroDigitos = numero.substring(longitud - 4);

        return ultimosCuatroDigitos;
    }

    public static ThriftCompraDto toThriftSaleDto(Sale sale) {
        return new ThriftCompraDto(sale.getSaleId(),sale.getEmailUsuario(), sale.getPartidoId(), obtenerUltimosCuatroDigitos(sale
                .getNumeroTarjetaBancaria()), sale.getNumeroEntradasCompradas(), sale.getFechaHoraCompra().toString(), sale.isEntradasRecogidas());
    }

    public static List<ThriftCompraDto> toThriftSaleDtos(List<Sale> compras) {
        List<ThriftCompraDto> compraDtos = new ArrayList<>(compras.size());
        for (int i = 0; i < compras.size(); i++) {
            Sale compra = compras.get(i);
            compraDtos.add(toThriftSaleDto(compra));
        }
        return compraDtos;
    }



}
