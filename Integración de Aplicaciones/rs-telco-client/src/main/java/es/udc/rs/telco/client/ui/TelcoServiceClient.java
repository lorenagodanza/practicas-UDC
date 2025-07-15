package es.udc.rs.telco.client.ui;

import es.udc.rs.telco.client.service.ClientTelcoService;
import es.udc.rs.telco.client.service.ClientTelcoServiceFactory;
import es.udc.rs.telco.client.service.clientDto.ClientCustomerDto;
import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallsBtwDatesDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public class TelcoServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientTelcoService clientTelcoService = ClientTelcoServiceFactory.getService();
		if ("-addCustomer".equalsIgnoreCase(args[0])) {
			validateArgs(args, 5, new int[] {});

			// [-addCustomer] TelcoServiceClient -addCustomer <name> ...

			try {
				ClientCustomerDto customerId = clientTelcoService.addCustomer(args[1], args[2], args[3], args[4]);
				System.out.println("Customer " + customerId.getCustomerId() + " " + "created sucessfully");
			}catch (InputValidationException ex){
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		}else if ("-addPhoneCall".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[]{});

			try {
				ClientPhoneCallDto added = clientTelcoService.addPhoneCall(Long.parseLong(args[1]), args[2], Long.parseLong(args[3]),
						args[4], args[5]);
				System.out.println("PhoneCall " + added.getPhoneCallId() + " " + "created sucessfully");
			} catch (InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		}else if ("-findCalls".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 1 });

			try {
				List<ClientPhoneCallsBtwDatesDto> phonecalls = clientTelcoService.findCallsBetweenDates(Long.parseLong(args[1]),args[2],args[3],args[4],Integer.parseInt(args[5]),Integer.parseInt(args[6]));
				System.out.println("Found " + phonecalls.size() + " phone calls between dates: " + args[2] + " and " + args[3]);
				for (ClientPhoneCallsBtwDatesDto phonecall : phonecalls){
					System.out.println("Start Date: " + phonecall.getStartDate() + ", duration: "
					+ phonecall.getDuration() + ", destinationnumber: " + phonecall.getDestinationNumber());
				}
			}
			catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-deleteCustomer".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[]{});
			try {
				clientTelcoService.deleteCustomer(Long.parseLong(args[1]));
				System.out.println("Customer " + args[1] + " has been removed sucessfully");
			} catch (InstanceNotFoundException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if("-changeState".equalsIgnoreCase(args[0])){
			validateArgs(args, 5, new int[]{});
			try {
				clientTelcoService.changeStatus(Long.parseLong(args[1]), args[2],args[3],args[4]);
				System.out.println("PhoneCall " + args[1] + " status changed to " + args[4]);
			} catch (InstanceNotFoundException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
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

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println(
				"Usage:\n" + "    [-addCustomer]    TelcoServiceClient -addCustomer <name> <dni> <address> <phonenumber>\n" +
		                     "    [-findCalls]   TelcoServiceClient -findCalls <customerId> <Inicio> <Fin> <tipo> <indice> <cantidad> \n" +
						     "    [-changeState]   TelcoServiceClient -changeStatus <phoneCallId> <mes> <ano> <newStatus>\n" +
						     "    [-addPhoneCall] TelcoServiceClient -addPhoneCall <customerId> <startDate> <duration> <destinationNumber> <phoneCallType> <phoneCallStatus>\n" +
							 "    [-deleteCustomer]   TelcoServiceClient -deleteCustomer <customerId>\n");
	}

}
