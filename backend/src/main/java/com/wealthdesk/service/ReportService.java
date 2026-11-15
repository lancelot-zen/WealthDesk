package com.wealthdesk.service;

import com.wealthdesk.document.InsurancePolicy;
import com.wealthdesk.entity.Holding;
import com.wealthdesk.entity.Trade;
import com.wealthdesk.exception.ApiException;
import com.wealthdesk.report.ExcelReportGenerator;
import com.wealthdesk.report.PdfReportGenerator;
import com.wealthdesk.repository.HoldingRepository;
import com.wealthdesk.repository.InsurancePolicyRepository;
import com.wealthdesk.repository.TradeRepository;
import com.wealthdesk.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final ExcelReportGenerator excelReportGenerator;
    private final PdfReportGenerator pdfReportGenerator;
    private final SecurityUtils securityUtils;

    public ReportService(TradeRepository tradeRepository,
                         HoldingRepository holdingRepository,
                         InsurancePolicyRepository insurancePolicyRepository,
                         ExcelReportGenerator excelReportGenerator,
                         PdfReportGenerator pdfReportGenerator,
                         SecurityUtils securityUtils) {
        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.excelReportGenerator = excelReportGenerator;
        this.pdfReportGenerator = pdfReportGenerator;
        this.securityUtils = securityUtils;
    }

    public byte[] generateReport(String reportType, String format) {
        Long userId = securityUtils.getCurrentUserId();

        try {
            return switch (reportType.toLowerCase()) {
                case "pnl" -> generatePnlReport(userId, format);
                case "holdings" -> generateHoldingsReport(userId, format);
                case "insurance" -> generateInsuranceReport(userId, format);
                default -> throw ApiException.badRequest("Invalid report type: " + reportType);
            };
        } catch (IOException e) {
            throw new RuntimeException("Error generating report", e);
        }
    }

    private byte[] generatePnlReport(Long userId, String format) throws IOException {
        List<Trade> trades = tradeRepository.findByUserIdOrderByTradeDateDesc(userId);
        return switch (format.toLowerCase()) {
            case "excel" -> excelReportGenerator.generatePnlReport(trades);
            case "pdf" -> pdfReportGenerator.generatePnlReport(trades);
            default -> throw ApiException.badRequest("Invalid format: " + format + ". Use 'excel' or 'pdf'");
        };
    }

    private byte[] generateHoldingsReport(Long userId, String format) throws IOException {
        List<Holding> holdings = holdingRepository.findByUserId(userId);
        return switch (format.toLowerCase()) {
            case "excel" -> excelReportGenerator.generateHoldingsReport(holdings);
            case "pdf" -> pdfReportGenerator.generateHoldingsReport(holdings);
            default -> throw ApiException.badRequest("Invalid format: " + format + ". Use 'excel' or 'pdf'");
        };
    }

    private byte[] generateInsuranceReport(Long userId, String format) throws IOException {
        List<InsurancePolicy> policies = insurancePolicyRepository.findByUserId(userId);
        return switch (format.toLowerCase()) {
            case "excel" -> excelReportGenerator.generateInsuranceReport(policies);
            case "pdf" -> pdfReportGenerator.generateInsuranceReport(policies);
            default -> throw ApiException.badRequest("Invalid format: " + format + ". Use 'excel' or 'pdf'");
        };
    }

    public String getContentType(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }

    public String getFileExtension(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> ".xlsx";
            case "pdf" -> ".pdf";
            default -> "";
        };
    }
}