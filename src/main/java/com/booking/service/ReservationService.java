package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Membership;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class ReservationService {
    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Service> serviceList = ServiceRepository.getAllService();
    private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void createReservation() {
        // Memasukkan ID Pelanggan
        System.out.print("Masukkan ID Pelanggan: ");
        String customerId = input.nextLine();
        Customer customer = findCustomerById(customerId);

        while (customer == null) {
            System.out.println("Customer dengan ID " + customerId + " tidak tersedia.");
            System.out.print("Masukkan ID Pelanggan yang tersedia: ");
            customerId = input.nextLine();
            customer = findCustomerById(customerId);
        }

        // Memasukkan ID Employee
        System.out.print("Masukkan ID Employee: ");
        String employeeId = input.nextLine();
        Employee employee = findEmployeeById(employeeId);

        while (employee == null) {
            System.out.println("Employee dengan ID " + employeeId + " tidak tersedia.");
            System.out.print("Masukkan ID Employee yang tersedia: ");
            employeeId = input.nextLine();
            employee = findEmployeeById(employeeId);
        }

        List<Service> selectedServices = new ArrayList<>();
        boolean selectingServices = true;

        while (selectingServices) {
            // Menampilkan layanan yang tersedia
            System.out.println("Layanan yang tersedia:");
            for (Service service : serviceList) {
                System.out.println("ID: " + service.getServiceId() + " - Nama Layanan: " + service.getServiceName()
                        + " - Harga: " + service.getPrice());
            }

            System.out.print("Masukkan ID Layanan (atau 0 untuk selesai memilih): ");
            String serviceIdInput = input.nextLine();

            if (serviceIdInput.equals("0")) {
                selectingServices = false;
                continue;
            }

            Service selectedService = findServiceById(serviceIdInput.trim());

            if (selectedService == null) {
                System.out.println("Layanan dengan ID " + serviceIdInput + " tidak tersedia.");
            } else if (selectedServices.contains(selectedService)) {
                System.out.println("Layanan dengan ID " + serviceIdInput + " sudah dipilih.");
            } else {
                selectedServices.add(selectedService);
            }
        }

        // Menghitung total harga reservasi
        double reservationPrice = hitungReservationPrice(customer, selectedServices);

        // Membuat objek Reservation
        Reservation newReservation = Reservation.builder()
                .reservationId(generateReservationId())
                .customer(customer)
                .employee(employee)
                .services(selectedServices)
                .reservationPrice(reservationPrice)
                .workstage("In Process")
                .build();

        // Menambahkan objek Reservation ke reservationList
        reservationList.add(newReservation);

        System.out.println("Reservasi berhasil dibuat.");
    }

    private static Customer findCustomerById(String customerId) {
        for (Person person : personList) {
            if (person instanceof Customer && person.getId().equals(customerId)) {
                return (Customer) person;
            }
        }
        return null;
    }

    private static Employee findEmployeeById(String employeeId) {
        for (Person person : personList) {
            if (person instanceof Employee && person.getId().equals(employeeId)) {
                return (Employee) person;
            }
        }
        return null;
    }

    // Metode untuk mencari Service
    private static Service findServiceById(String serviceId) {
        for (Service service : serviceList) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    // Metode untuk menghitung total harga reservasi
    private static double hitungReservationPrice(Customer customer, List<Service> selectedServices) {
        double total = 0;
        for (Service service : selectedServices) {
            total += service.getPrice();
        }

        // Menghitung diskon berdasarkan jenis member
        Membership member = customer.getMember();
        if (member != null) {
            if (member.getMembershipName().equalsIgnoreCase("silver")) {
                total *= 0.95; // Diskon 5% untuk Silver
            } else if (member.getMembershipName().equalsIgnoreCase("gold")) {
                total *= 0.90; // Diskon 10% untuk Gold
            }
        }

        return total;
    }

    // Metode untuk menghasilkan ID reservasi secara unik
    private static String generateReservationId() {
        // Implementasikan logika untuk menghasilkan ID yang unik, misalnya berdasarkan
        // timestamp atau lainnya
        return "RES" + System.currentTimeMillis();
    }

    // public static void editReservationWorkstage() {

    // }

    // Silahkan tambahkan function lain, dan ubah function diatas sesuai kebutuhan
}
