package com.wealthdesk.report;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wealthdesk.document.InsurancePolicy;
import com.wealthdesk.entity.Holding;
import com.wealthdesk.entity.Trade;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Component
public class PdfReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(33, 37, 41));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font BODY_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL);
    private static final Font SUMMARY_FONT = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(0, 100, 0));
    private static final Color HEADER_BG = new Color(44, 62, 80);

    public byte[] generatePnlReport(List<Trade> trades) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 20);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("PnL Report — WealthDesk", TITLE_FONT));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{1, 2, 3, 1.5f, 1.5f, 2, 2, 2, 1.5f, 2});
            table.setWidthPercentage(100);

            String[] headers = {"ID", "Symbol", "Name", "Type", "Qty", "Buy Price", "Sell Price", "Date", "Exchange", "PnL"};
            for (String h : headers) {
                table.addCell(headerCell(h));
            }

            BigDecimal totalPnl = BigDecimal.ZERO;
            for (Trade t : trades) {
                table.addCell(bodyCell(String.valueOf(t.getId())));
                table.addCell(bodyCell(t.getSymbol()));
                table.addCell(bodyCell(t.getName()));
                table.addCell(bodyCell(t.getType().name()));
                table.addCell(bodyCell(String.valueOf(t.getQuantity())));
                table.addCell(bodyCell(formatCurrency(t.getBuyPrice())));
                table.addCell(bodyCell(t.getSellPrice() != null ? formatCurrency(t.getSellPrice()) : "-"));
                table.addCell(bodyCell(t.getTradeDate().toString()));
                table.addCell(bodyCell(t.getExchange() != null ? t.getExchange() : "NSE"));
                BigDecimal pnl = t.getRealizedPnl();
                table.addCell(pnlCell(pnl));
                totalPnl = totalPnl.add(pnl);
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Realized PnL: ₹" + formatCurrency(totalPnl), SUMMARY_FONT));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PnL PDF", e);
        }

        return out.toByteArray();
    }

    public byte[] generateHoldingsReport(List<Holding> holdings) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 20);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Holdings Report — WealthDesk", TITLE_FONT));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{2, 3, 1.5f, 2, 2, 2, 2, 2, 1.5f, 2});
            table.setWidthPercentage(100);

            String[] headers = {"Symbol", "Name", "Qty", "Avg Buy", "Current", "Invested", "Current Val", "PnL", "PnL %", "Sector"};
            for (String h : headers) {
                table.addCell(headerCell(h));
            }

            for (Holding h : holdings) {
                table.addCell(bodyCell(h.getSymbol()));
                table.addCell(bodyCell(h.getName()));
                table.addCell(bodyCell(String.valueOf(h.getQuantity())));
                table.addCell(bodyCell(formatCurrency(h.getAvgBuyPrice())));
                table.addCell(bodyCell(formatCurrency(h.getCurrentPrice())));
                table.addCell(bodyCell(formatCurrency(h.getInvestedValue())));
                table.addCell(bodyCell(formatCurrency(h.getCurrentValue())));
                table.addCell(pnlCell(h.getUnrealizedPnl()));
                table.addCell(bodyCell(h.getPnlPercentage() + "%"));
                table.addCell(bodyCell(h.getSector() != null ? h.getSector() : ""));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Holdings PDF", e);
        }

        return out.toByteArray();
    }

    public byte[] generateInsuranceReport(List<InsurancePolicy> policies) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 20);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Insurance Report — WealthDesk", TITLE_FONT));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{2, 3, 2, 2, 2, 2, 2, 2, 2, 1.5f});
            table.setWidthPercentage(100);

            String[] headers = {"Policy #", "Name", "Provider", "Type", "Premium", "Sum Assured", "Start", "End", "Nominee", "Status"};
            for (String h : headers) {
                table.addCell(headerCell(h));
            }

            for (InsurancePolicy p : policies) {
                table.addCell(bodyCell(p.getPolicyNumber()));
                table.addCell(bodyCell(p.getPolicyName()));
                table.addCell(bodyCell(p.getProvider()));
                table.addCell(bodyCell(p.getType().name()));
                table.addCell(bodyCell(formatCurrency(p.getPremium())));
                table.addCell(bodyCell(formatCurrency(p.getSumAssured())));
                table.addCell(bodyCell(p.getStartDate().toString()));
                table.addCell(bodyCell(p.getEndDate().toString()));
                table.addCell(bodyCell(p.getNominee() != null ? p.getNominee() : ""));
                table.addCell(bodyCell(p.getStatus().name()));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Insurance PDF", e);
        }

        return out.toByteArray();
    }

    private PdfPCell headerCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(HEADER_BG);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell bodyCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, BODY_FONT));
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell pnlCell(BigDecimal value) {
        Font font = new Font(Font.HELVETICA, 9, Font.BOLD,
                value.compareTo(BigDecimal.ZERO) >= 0 ? new Color(0, 128, 0) : new Color(220, 20, 60));
        PdfPCell cell = new PdfPCell(new Phrase(formatCurrency(value), font));
        cell.setPadding(4);
        return cell;
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) return "-";
        return String.format("%,.2f", value);
    }
}