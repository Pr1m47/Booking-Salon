package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Reservation;

public class ValidationService {
    // Buatlah function sesuai dengan kebutuhan
    private static List<Reservation> reservationList = new ArrayList<>();
    private static PrintService printService = new PrintService();
    private static Scanner input = new Scanner(System.in);

    public static boolean validateReservationId(String reservationId, List<Reservation> reservationList2) {
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId().equals(reservationId)) {
                if ("In Process".equalsIgnoreCase(reservation.getWorkstage())) {
                    return true;
                } else {
                    System.out.println("Reservation yang dicari sudah selesai.");
                    return false;
                }
            }
        }
        System.out.println("Reservation yang dicari tidak tersedia.");
        return false;
    }

    public static void finishOrCancelReservation() {
        System.out.print("Masukkan Reservation ID: ");
        String reservationIdToFind = input.nextLine();

        // Memvalidasi reservation ID
        if (validateReservationId(reservationIdToFind, reservationList)) {
            Reservation foundReservation = findReservationById(reservationIdToFind);

            // Tampilkan detail reservasi
            printService.printReservationDetails(foundReservation);

            System.out.println("Pilih tindakan:");
            System.out.println("1. Finish Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("0. Kembali ke Menu Sebelumnya");
            System.out.print("Pilihan Anda: ");
            int actionChoice = Integer.valueOf(input.nextLine());

            switch (actionChoice) {
                case 1:
                    // Finish Reservation
                    finishReservation(foundReservation);
                    break;
                case 2:
                    // Cancel Reservation
                    cancelReservation(foundReservation);
                    break;
                case 0:
                    // Kembali ke menu sebelumnya
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    private static Reservation findReservationById(String reservationId) {
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    private static void finishReservation(Reservation reservation) {
        reservation.setWorkstage("Finish");
        System.out.println("Reservation berhasil selesai.");
    }

    private static void cancelReservation(Reservation reservation) {
        reservation.setWorkstage("Canceled");
        System.out.println("Reservation berhasil dibatalkan.");
    }
}
