package com.booking.service;

import java.util.ArrayList;
import java.util.List;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Membership;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;

public class PrintService {
    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Reservation> reservationList = new ArrayList<>();

    public static void printMenu(String title, String[] menuArr) {
        int num = 1;
        System.out.println(title);
        for (int i = 0; i < menuArr.length; i++) {
            if (i == (menuArr.length - 1)) {
                num = 0;
            }
            System.out.println(num + ". " + menuArr[i]);
            num++;
        }
    }

    public String printServices(List<Service> serviceList) {
        String result = "";
        // Bisa disesuaikan kembali
        for (Service service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }

    // Function yang dibuat hanya sebgai contoh bisa disesuaikan kembali
    public void showRecentReservation(List<Reservation> reservationList) {
        int num = 1;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
                "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Pegawai", "Workstage");
        System.out
                .println("+========================================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Waiting")
                    || reservation.getWorkstage().equalsIgnoreCase("In process")) {
                System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
                        num, reservation.getReservationId(), reservation.getCustomer().getName(),
                        printServices(reservation.getServices()), reservation.getReservationPrice(),
                        reservation.getEmployee().getName(), reservation.getWorkstage());
                num++;
            }
        }
    }

    public void showAllCustomer() {
        for (Person person : personList) {
            if (person instanceof Customer) {
                showPersonDetails((Customer) person);
            }
        }
    }

    public void showAllEmployee() {
        for (Person person : personList) {
            if (person instanceof Employee) {
                showPersonDetails((Employee) person);
            }
        }
    }

    public void showHistoryReservationAndTotalProfit() {
        double totalProfit = 0;

        for (Reservation reservation : reservationList) {
            if ("Finish".equalsIgnoreCase(reservation.getWorkstage())) {
                printReservationDetails(reservation);
                totalProfit += reservation.getReservationPrice();
            }
        }

        System.out.println("Total Keuntungan: $" + totalProfit);
    }

    // Metode untuk mencetak detail Reservation
    public void printReservationDetails(Reservation reservation) {
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Customer: " + reservation.getCustomer().getName());
        System.out.println("Employee: " + reservation.getEmployee().getName());
        System.out.println("Workstage: " + reservation.getWorkstage());
        System.out.println("Reservation Price: $" + reservation.getReservationPrice());

        System.out.println("Services:");
        for (Service service : reservation.getServices()) {
            System.out.println("- " + service.getServiceName() + ": $" + service.getPrice());
        }

        System.out.println("-------------------------------");
    }

    // Metode untuk mencetak detail Employee atau Customer
    private static void showPersonDetails(Person person) {
        System.out.println("ID: " + person.getId());
        System.out.println("Name: " + person.getName());
        System.out.println("Address: " + person.getAddress());

        if (person instanceof Customer) {
            Customer customer = (Customer) person;
            System.out.println("Wallet: $" + customer.getWallet());

            Membership member = customer.getMember();
            if (member != null) {
                System.out.println("Membership: " + member.getMembershipName());
            }
        } else if (person instanceof Employee) {
            Employee employee = (Employee) person;
            System.out.println("Experience: " + employee.getExperience() + " years");
        }

        System.out.println("-------------------------------");
    }
}
