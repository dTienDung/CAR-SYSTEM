package com.example.CAR_.SYSTEM.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CodeGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    /**
     * Sinh mã khách hàng: KH0001, KH0002...
     */
    public static String generateMaKH(Long sequence) {
        return String.format("KH%04d", sequence);
    }
    
    /**
     * Sinh mã xe: XE0001, XE0002...
     */
    public static String generateMaXe(Long sequence) {
        return String.format("XE%04d", sequence);
    }
    
    /**
     * Sinh mã hợp đồng: HD-YYYYMMDD-XXXX
     */
    public static String generateMaHD(Long sequence) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        return String.format("HD-%s-%04d", date, sequence);
    }
    
    /**
     * Sinh mã hồ sơ thẩm định: HS-YYYYMMDD-XXXX
     */
    public static String generateMaHS(Long sequence) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        return String.format("HS-%s-%04d", date, sequence);
    }
    
    /**
     * Sinh mã thanh toán: TT-YYYYMMDD-XXXX
     */
    public static String generateMaTT(Long sequence) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        return String.format("TT-%s-%04d", date, sequence);
    }
    
    /**
     * Sinh mã gói bảo hiểm: GBH001, GBH002...
     */
    public static String generateMaGoi(Long sequence) {
        return String.format("GBH%03d", sequence);
    }
    
    /**
     * Sinh mã tiêu chí: TC001, TC002...
     */
    public static String generateMaTieuChi(Long sequence) {
        return String.format("TC%03d", sequence);
    }
}

