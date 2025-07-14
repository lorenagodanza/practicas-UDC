namespace java es.udc.ws.app.thrift

struct ThriftPartidoDto{
    1: i64 partidoId;
    2: string equipoVisitante;
    3: string fechaHoraCelebracion;
    4: double precioEntradas;
    5: i32 entradasDisponibles;
    6: i32 entradasVendidas;
}

struct ThriftCompraDto {
    1: i64 compraId;
    2: string emailUsuario;
    3: i64 partidoId;
    4: string ultimosCuatroDigitosTarjeta;
    5: i32 numeroEntradasCompradas;
    6: string fechaHoraCompra;
    7: bool entradasRecogidas;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftoNoSuficientesEntradasException {
    1: i64 partidoId
}

exception ThriftoPartidoEmpezadoException {
    1: i64 partidoId
}

exception ThriftEntradasYaRecogidasException {
    1: i64 saleId
}

exception ThriftTarjetaBancariaNoCoincideException {
    1: i64 saleId
}

service ThriftPartidoService {

   ThriftPartidoDto darAltaPartido(1: ThriftPartidoDto partidoDto) throws (1: ThriftInputValidationException e)

   list<ThriftPartidoDto> buscarPartidosEntreFechas(1:string keywords) throws(1:ThriftInputValidationException e)

   ThriftPartidoDto buscarPartidoPorId(1: i64 partidoId) throws (1: ThriftInstanceNotFoundException e)

   ThriftCompraDto buyEntradas(1: i64 partidoId, 2: string emailUsuario, 3: string numeroTarjetaBancaria, 4: i32 numeroEntradas)
           throws (1: ThriftInstanceNotFoundException e, 2: ThriftInputValidationException ee, 3: ThriftoNoSuficientesEntradasException eee, 4: ThriftoPartidoEmpezadoException eeee)
}