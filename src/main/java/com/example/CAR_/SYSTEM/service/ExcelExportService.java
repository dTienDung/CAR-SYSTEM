package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.model.HopDong;
import com.example.CAR_.SYSTEM.model.HoSoThamDinh;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public byte[] exportHoSoThamDinh(List<HoSoThamDinh> hoSoList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Hồ sơ Thẩm định");
            
            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "STT", "Mã HS", "Khách hàng", "Số điện thoại", "Biển số xe", 
                "Hãng xe", "Dòng xe", "Gói bảo hiểm", "Risk Score", "Risk Level", 
                "Trạng thái", "Phí bảo hiểm", "Ngày tạo", "Ghi chú"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (HoSoThamDinh hs : hoSoList) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                
                // STT
                createCell(row, colNum++, rowNum - 1, numberStyle);
                // Mã HS
                createCell(row, colNum++, hs.getMaHS(), dataStyle);
                // Khách hàng
                createCell(row, colNum++, hs.getKhachHang() != null ? hs.getKhachHang().getHoTen() : "", dataStyle);
                // Số điện thoại
                createCell(row, colNum++, hs.getKhachHang() != null ? hs.getKhachHang().getSoDienThoai() : "", dataStyle);
                // Biển số xe
                createCell(row, colNum++, hs.getXe() != null ? hs.getXe().getBienSo() : "", dataStyle);
                // Hãng xe
                createCell(row, colNum++, hs.getXe() != null ? hs.getXe().getHangXe() : "", dataStyle);
                // Dòng xe
                createCell(row, colNum++, hs.getXe() != null ? hs.getXe().getDongXe() : "", dataStyle);
                // Gói bảo hiểm
                createCell(row, colNum++, hs.getGoiBaoHiem() != null ? hs.getGoiBaoHiem().getTenGoi() : "", dataStyle);
                // Risk Score
                createCell(row, colNum++, hs.getRiskScore(), numberStyle);
                // Risk Level
                createCell(row, colNum++, getRiskLevelText(hs.getRiskLevel().name()), dataStyle);
                // Trạng thái
                createCell(row, colNum++, getTrangThaiText(hs.getTrangThai().name()), dataStyle);
                // Phí bảo hiểm (as number with currency format)
                Cell phiCell = row.createCell(colNum++);
                if (hs.getPhiBaoHiem() != null) {
                    phiCell.setCellValue(hs.getPhiBaoHiem().doubleValue());
                } else {
                    phiCell.setCellValue(0);
                }
                phiCell.setCellStyle(currencyStyle);
                // Ngày tạo
                createCell(row, colNum++, hs.getCreatedAt() != null ? hs.getCreatedAt().format(DATETIME_FORMATTER) : "", dataStyle);
                // Ghi chú
                createCell(row, colNum++, hs.getGhiChu() != null ? hs.getGhiChu() : "", dataStyle);
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    public byte[] exportHopDong(List<HopDong> hopDongList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Hợp đồng");
            
            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "STT", "Mã HĐ", "Khách hàng", "Số điện thoại", "Biển số xe", 
                "Gói bảo hiểm", "Ngày ký", "Ngày hiệu lực", "Ngày hết hạn", 
                "Tổng phí", "Đã thanh toán", "Còn lại", "Trạng thái", "Ghi chú"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (HopDong hd : hopDongList) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                
                // STT
                createCell(row, colNum++, rowNum - 1, numberStyle);
                // Mã HĐ
                createCell(row, colNum++, hd.getMaHD(), dataStyle);
                // Khách hàng
                createCell(row, colNum++, hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "", dataStyle);
                // Số điện thoại
                createCell(row, colNum++, hd.getKhachHang() != null ? hd.getKhachHang().getSoDienThoai() : "", dataStyle);
                // Biển số xe
                createCell(row, colNum++, hd.getXe() != null ? hd.getXe().getBienSo() : "", dataStyle);
                // Gói bảo hiểm
                createCell(row, colNum++, hd.getGoiBaoHiem() != null ? hd.getGoiBaoHiem().getTenGoi() : "", dataStyle);
                // Ngày ký
                createCell(row, colNum++, hd.getNgayKy() != null ? hd.getNgayKy().format(DATE_FORMATTER) : "", dataStyle);
                // Ngày hiệu lực
                createCell(row, colNum++, hd.getNgayHieuLuc() != null ? hd.getNgayHieuLuc().format(DATE_FORMATTER) : "", dataStyle);
                // Ngày hết hạn
                createCell(row, colNum++, hd.getNgayHetHan() != null ? hd.getNgayHetHan().format(DATE_FORMATTER) : "", dataStyle);
                
                // Tổng phí (as number with currency format)
                Cell tongPhiCell = row.createCell(colNum++);
                if (hd.getTongPhiBaoHiem() != null) {
                    tongPhiCell.setCellValue(hd.getTongPhiBaoHiem().doubleValue());
                } else {
                    tongPhiCell.setCellValue(0);
                }
                tongPhiCell.setCellStyle(currencyStyle);
                
                // Đã thanh toán (as number with currency format)
                Cell daThanhToanCell = row.createCell(colNum++);
                if (hd.getTongDaThanhToan() != null) {
                    daThanhToanCell.setCellValue(hd.getTongDaThanhToan().doubleValue());
                } else {
                    daThanhToanCell.setCellValue(0);
                }
                daThanhToanCell.setCellStyle(currencyStyle);
                
                // Còn lại (as number with currency format)
                Cell conLaiCell = row.createCell(colNum++);
                if (hd.getTongPhiBaoHiem() != null && hd.getTongDaThanhToan() != null) {
                    conLaiCell.setCellValue(hd.getTongPhiBaoHiem().subtract(hd.getTongDaThanhToan()).doubleValue());
                } else {
                    conLaiCell.setCellValue(0);
                }
                conLaiCell.setCellStyle(currencyStyle);
                
                // Trạng thái
                createCell(row, colNum++, getHopDongStatusText(hd.getTrangThai().name()), dataStyle);
                // Ghi chú
                createCell(row, colNum++, hd.getGhiChu() != null ? hd.getGhiChu() : "", dataStyle);
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        return style;
    }
    
    private CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }
    
    private String getRiskLevelText(String riskLevel) {
        return switch (riskLevel) {
            case "CHAP_NHAN" -> "Chấp nhận";
            case "XEM_XET" -> "Xem xét";
            case "TU_CHOI" -> "Từ chối";
            default -> riskLevel;
        };
    }
    
    private String getTrangThaiText(String trangThai) {
        return switch (trangThai) {
            case "MOI_TAO" -> "Mới tạo";
            case "DANG_THAM_DINH" -> "Đang thẩm định";
            case "CHAP_NHAN" -> "Chấp nhận";
            case "TU_CHOI" -> "Từ chối";
            case "XEM_XET" -> "Xem xét";
            default -> trangThai;
        };
    }
    
    private String getHopDongStatusText(String status) {
        return switch (status) {
            case "DRAFT" -> "Bản nháp";
            case "PENDING_PAYMENT" -> "Chờ thanh toán";
            case "ACTIVE" -> "Đang hiệu lực";
            case "EXPIRED" -> "Hết hạn";
            case "RENEWED" -> "Đã tái tục";
            case "CANCELLED" -> "Đã hủy";
            case "TERMINATED" -> "Chấm dứt";
            default -> status;
        };
    }
}
