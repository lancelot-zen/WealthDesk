package com.wealthdesk.controller;

import com.wealthdesk.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Download PnL, Holdings, and Insurance reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{type}")
    @Operation(summary = "Generate and download a report",
            description = "Type: pnl, holdings, insurance. Format: excel, pdf")
    public ResponseEntity<byte[]> downloadReport(
            @PathVariable String type,
            @RequestParam(defaultValue = "excel") String format) {

        byte[] reportData = reportService.generateReport(type, format);
        String contentType = reportService.getContentType(format);
        String extension = reportService.getFileExtension(format);
        String filename = type + "_report" + extension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(reportData);
    }
}