package com.powersphere.meter.util;

import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.UUID;

@UtilityClass
public class MeterUtil {

    private static final String BARCODE_PREFIX = "PSM";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Generates a unique meter number based on a counter or UUID.
     * Format: MTR-YYYY-COUNTER
     */
    public static String generateMeterNumber(long counter) {
        String year = String.valueOf(LocalDateTime.now().getYear());
        return String.format("MTR-%s-%05d", year, counter);
    }

    /**
     * Generates a QR code string for the meter.
     * Contains meter details encoded as a JSON-like string.
     */
    public static String generateQrCode(String meterNumber, String serialNumber, UUID meterId) {
        return String.format("PSM:QR:%s:%s:%s:%s",
                meterId.toString(),
                meterNumber,
                serialNumber,
                LocalDateTime.now().format(DATE_FORMAT));
    }

    /**
     * Generates a barcode string for the meter.
     * Format: PSM-YYYYMMDD-HASH
     */
    public static String generateBarcode(String meterNumber, UUID meterId) {
        try {
            String input = meterNumber + "-" + meterId + "-" + LocalDateTime.now().format(DATE_FORMAT);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            String hexHash = HexFormat.of().formatHex(hash).substring(0, 12).toUpperCase();
            return String.format("%s-%s-%s", BARCODE_PREFIX,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    hexHash);
        } catch (NoSuchAlgorithmException e) {
            return String.format("%s-%s-%s", BARCODE_PREFIX,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
    }
}
