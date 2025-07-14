package es.udc.ws.app.restservice.dto;

import java.util.ArrayList;
import java.util.List;
import es.udc.ws.app.model.compra.Sale;

public class CompraToRestCompraDtoConversor {

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

        public static RestCompraDto toRestSaleDto(Sale sale) {
            return new RestCompraDto(sale.getSaleId(),sale.getEmailUsuario(), sale.getPartidoId(), obtenerUltimosCuatroDigitos(sale
                    .getNumeroTarjetaBancaria()), sale.getNumeroEntradasCompradas(), sale.getFechaHoraCompra(), sale.isEntradasRecogidas());
        }

    public static List<RestCompraDto> toRestSaleDtos(List<Sale> compras) {
        List<RestCompraDto> compraDtos = new ArrayList<>(compras.size());
        for (int i = 0; i < compras.size(); i++) {
            Sale compra = compras.get(i);
            compraDtos.add(toRestSaleDto(compra));
        }
        return compraDtos;
    }



}
