package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.ClientPartidoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDateTime.parse;


public class AppServiceClient {
    public static void main(String[] args) {

            if (args.length == 0) {
                printUsageAndExit();
            }
            ClientPartidoService clientPartidoService =
                    ClientPartidoServiceFactory.getService();
            if ("-addMatch".equalsIgnoreCase(args[0])) {
                validateArgs(args, 5, new int[]{3,4});

                // -addMatch <visitor> <celebrationdate> <price> <maxTickets>

                try {
                    Long partidoId = clientPartidoService.darAltaPartido(new ClientPartidoDto(null,
                            args[1], LocalDateTime.parse(args[2]) , Double.valueOf(args[3]),
                            Integer.valueOf(args[4])));



                    System.out.println("Partido " + partidoId + " created sucessfully");

                } catch (NumberFormatException | InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if ("-buy".equalsIgnoreCase(args[0])) {
                validateArgs(args, 5, new int[]{1, 3});

                //-buy <matchId> <userEmail> <numTickets> <cardNumber>

                Long saleId;
                try {
                    saleId = clientPartidoService.buyEntradas(Long.parseLong(args[1]),
                            args[2], Integer.valueOf(args[3]),args[4]);

                    System.out.println("Partido " + args[1] +
                            " purchased sucessfully with sale number " +
                            saleId);

                } catch (NumberFormatException | InstanceNotFoundException |
                         InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            }else if("-findMatch".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {1});

                //-findMatches <untilDay>

                try {
                    Long partidoId=Long.valueOf(args[1]);
                    ClientPartidoDto partido= clientPartidoService.buscarPartidoPorId(partidoId);
                    System.out.println("Match with id \"" + partidoId + "\" was found.\n");
                    System.out.println(partido.toString());
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            }else if("-findMatches".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {});

                //-findMatches '2024-09-01'

                try {
                    List<ClientPartidoDto> partidos = clientPartidoService.buscarPartidosEntreFechas(args[1]);

                    if (partidos.isEmpty()) {
                        System.out.println("[]");
                    } else {
                        System.out.println("Found " + partidos.size() + " partido(s) with FechaFin '" + args[1] + "'");
                        for (ClientPartidoDto partidoDto : partidos) {
                            System.out.println(partidoDto.toString());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }


            }/*else if("-findPurchases".equalsIgnoreCase(args[0])) {
                validateArgs(args, 2, new int[] {});

                //- findPurchases <userEmail>

                try {
                    List<ClientSaleDto> sales = clientPartidoService.obtenerComprasUsuario((args[1]));
                    System.out.println("Found " + sales.size() +
                            " sale(s) with email '" + args[1] + "'");
                    for (int i = 0; i < sales.size(); i++) {
                        ClientSaleDto saleDto = sales.get(i);
                        System.out.println(saleDto.toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            } else if ("-collect".equalsIgnoreCase(args[0])) {
                validateArgs(args, 3, new int[]{1});

                //-collect <purchaseId> <cardNumber>


                try {
                    clientPartidoService.recogerEntradas(Long.parseLong(args[1]),args[2]);

                    System.out.println("Entradas de la compra " + args[1] +
                            " recogidas con exito ");

                } catch (NumberFormatException | InstanceNotFoundException |
                         InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }*/
        }

        public static void validateArgs (String[]args,int expectedArgs,
        int[] numericArguments){
            if (expectedArgs != args.length) {
                printUsageAndExit();
            }
            for (int i = 0; i < numericArguments.length; i++) {
                int position = numericArguments[i];
                try {
                    Double.parseDouble(args[position]);
                } catch (NumberFormatException n) {
                    printUsageAndExit();
                }
            }
        }

        public static void printUsageAndExit () {
            printUsage();
            System.exit(-1);
        }

        public static void printUsage () {
            System.err.println("Usage:\n" +
                    "    [addMatch]    PartidoServiceClient <visitor> <celebrationdate> <price> <maxTickets>\n"+
                    "    [buy]         PartidoServiceClient <matchId> <userEmail> <numTickets> <cardNumber>\n");
        }

}