package com.wealthdesk.report;

import com.wealthdesk.document.InsurancePolicy;
import com.wealthdesk.entity.Holding;
import com.wealthdesk.entity.Trade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ExcelReportGenerator {

    public byte[] generatePnlReport(List<Trade> trades) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PnL Report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Symbol", "Name", "Type", "Quantity", "Buy Price", "Sell Price", "Trade Date", "Exchange", "Realized PnL"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            BigDecimal totalPnl = BigDecimal.ZERO;
            int rowNum = 1;
            for (Trade trade : trades) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(trade.getId());
                row.createCell(1).setCellValue(trade.getSymbol());
                row.createCell(2).setCellValue(trade.getName());
                row.createCell(3).setCellValue(trade.getType().name());
                row.createCell(4).setCellValue(trade.getQuantity());

                Cell buyCell = row.createCell(5);
                buyCell.setCellValue(trade.getBuyPrice().doubleValue());
                buyCell.setCellStyle(currencyStyle);

                Cell sellCell = row.createCell(6);
                if (trade.getSellPrice() != null) {
                    sellCell.setCellValue(trade.getSellPrice().doubleValue());
                }
                sellCell.setCellStyle(currencyStyle);

                row.createCell(7).setCellValue(trade.getTradeDate().toString());
                row.createCell(8).setCellValue(trade.getExchange() != null ? trade.getExchange() : "NSE");

                Cell pnlCell = row.createCell(9);
                BigDecimal pnl = trade.getRealizedPnl();
                pnlCell.setCellValue(pnl.doubleValue());
                pnlCell.setCellStyle(currencyStyle);
                totalPnl = totalPnl.add(pnl);
            }

            // Summary row
            Row summaryRow = sheet.createRow(rowNum + 1);
            Cell summaryLabel = summaryRow.createCell(8);
            summaryLabel.setCellValue("Total PnL:");
            summaryLabel.setCellStyle(headerStyle);
            Cell summaryValue = summaryRow.createCell(9);
            summaryValue.setCellValue(totalPnl.doubleValue());
            summaryValue.setCellStyle(currencyStyle);

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return toByteArray(workbook);
        }
    }

    public byte[] generateHoldingsReport(List<Holding> holdings) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Holdings Report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            Row header = sheet.createRow(0);
            String[] columns = {"Symbol", "Name", "Quantity", "Avg Buy Price", "Current Price", "Invested Value", "Current Value", "Unrealized PnL", "PnL %", "Sector"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Holding h : holdings) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(h.getSymbol());
                row.createCell(1).setCellValue(h.getName());
                row.createCell(2).setCellValue(h.getQuantity());

                Cell avgCell = row.createCell(3);
                avgCell.setCellValue(h.getAvgBuyPrice().doubleValue());
                avgCell.setCellStyle(currencyStyle);

                Cell curCell = row.createCell(4);
                curCell.setCellValue(h.getCurrentPrice().doubleValue());
                curCell.setCellStyle(currencyStyle);

                Cell invCell = row.createCell(5);
                invCell.setCellValue(h.getInvestedValue().doubleValue());
                invCell.setCellStyle(currencyStyle);

                Cell curValCell = row.createCell(6);
                curValCell.setCellValue(h.getCurrentValue().doubleValue());
                curValCell.setCellStyle(currencyStyle);

                Cell pnlCell = row.createCell(7);
                pnlCell.setCellValue(h.getUnrealizedPnl().doubleValue());
                pnlCell.setCellStyle(currencyStyle);

                row.createCell(8).setCellValue(h.getPnlPercentage().doubleValue() + "%");
                row.createCell(9).setCellValue(h.getSector() != null ? h.getSector() : "");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return toByteArray(workbook);
        }
    }

    public byte[] generateInsuranceReport(List<InsurancePolicy> policies) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Insurance Report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            Row header = sheet.createRow(0);
            String[] columns = {"Policy Number", "Policy Name", "Provider", "Type", "Premium", "Sum Assured", "Start Date", "End Date", "Nominee", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (InsurancePolicy p : policies) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getPolicyNumber());
                row.createCell(1).setCellValue(p.getPolicyName());
                row.createCell(2).setCellValue(p.getProvider());
                row.createCell(3).setCellValue(p.getType().name());

                Cell premCell = row.createCell(4);
                premCell.setCellValue(p.getPremium().doubleValue());
                premCell.setCellStyle(currencyStyle);

                Cell sumCell = row.createCell(5);
                sumCell.setCellValue(p.getSumAssured().doubleValue());
                sumCell.setCellStyle(currencyStyle);

                row.createCell(6).setCellValue(p.getStartDate().toString());
                row.createCell(7).setCellValue(p.getEndDate().toString());
                row.createCell(8).setCellValue(p.getNominee() != null ? p.getNominee() : "");
                row.createCell(9).setCellValue(p.getStatus().name());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return toByteArray(workbook);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    private byte[] toByteArray(Workbook workbook) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}