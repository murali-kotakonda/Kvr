package com.kvr.invoice.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.kvr.invoice.model.Invoice;
import com.kvr.invoice.model.InvoiceItem;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(99, 102, 241);
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(245, 245, 245);
    
    public byte[] generateInvoicePdf(Invoice invoice) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setMargins(30, 30, 30, 30);
            
            // Header
            Paragraph header = new Paragraph("TAX INVOICE")
                .setBold()
                .setFontSize(24)
                .setFontColor(PRIMARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
            document.add(header);
            
            // Invoice Info
            Table infoTable = new Table(2).useAllAvailableWidth().setMarginBottom(20);
            infoTable.addCell(createCell("Invoice No: " + invoice.getInvoiceNumber(), false, TextAlignment.LEFT));
            infoTable.addCell(createCell("Date: " + invoice.getInvoiceDate(), false, TextAlignment.RIGHT));
            document.add(infoTable);
            
            // From/To Section - Side by Side
            Table addressTable = new Table(2).useAllAvailableWidth().setMarginBottom(20);
            
            // From Cell
            Cell fromCell = new Cell()
                .setBorder(new SolidBorder(PRIMARY_COLOR, 1))
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(10);
            fromCell.add(new Paragraph("FROM (SELLER)").setBold().setFontSize(10).setFontColor(PRIMARY_COLOR));
            fromCell.add(new Paragraph(invoice.getFromName()).setBold().setFontSize(12).setMarginTop(5));
            fromCell.add(new Paragraph("GSTIN: " + (invoice.getFromGst() != null ? invoice.getFromGst() : "N/A")).setFontSize(9).setMarginTop(3));
            fromCell.add(new Paragraph(invoice.getFromAddress()).setFontSize(9).setMarginTop(5));
            addressTable.addCell(fromCell);
            
            // To Cell
            Cell toCell = new Cell()
                .setBorder(new SolidBorder(PRIMARY_COLOR, 1))
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(10);
            toCell.add(new Paragraph("TO (BUYER)").setBold().setFontSize(10).setFontColor(PRIMARY_COLOR));
            toCell.add(new Paragraph(invoice.getToName()).setBold().setFontSize(12).setMarginTop(5));
            toCell.add(new Paragraph("GSTIN: " + (invoice.getToGst() != null ? invoice.getToGst() : "N/A")).setFontSize(9).setMarginTop(3));
            toCell.add(new Paragraph(invoice.getToAddress()).setFontSize(9).setMarginTop(5));
            addressTable.addCell(toCell);
            
            document.add(addressTable);
            
            // Additional Details
            Table detailsTable = new Table(2).useAllAvailableWidth().setMarginBottom(15);
            detailsTable.addCell(createCell("State Code: " + (invoice.getStateCode() != null ? invoice.getStateCode() : "N/A"), false, TextAlignment.LEFT));
            detailsTable.addCell(createCell("Vehicle No: " + (invoice.getVehicleNumber() != null ? invoice.getVehicleNumber() : "N/A"), false, TextAlignment.RIGHT));
            document.add(detailsTable);
            
            // Items Table
            float[] columnWidths = {1, 4, 2, 1.5f, 2, 2, 1.5f, 2, 1.5f, 2};
            Table itemsTable = new Table(columnWidths).useAllAvailableWidth().setMarginBottom(15);
            
            // Table Headers
            String[] headers = {"S.No", "Description", "HSN", "Qty", "Rate/kg", "Value", "CGST%", "CGST₹", "SGST%", "SGST₹"};
            for (String headerText : headers) {
                itemsTable.addHeaderCell(new Cell()
                    .add(new Paragraph(headerText).setBold().setFontSize(9))
                    .setBackgroundColor(PRIMARY_COLOR)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
            }
            
            // Table Rows
            for (InvoiceItem item : invoice.getItems()) {
                itemsTable.addCell(createItemCell(String.valueOf(item.getSerialNo()), TextAlignment.CENTER));
                itemsTable.addCell(createItemCell(item.getDescription(), TextAlignment.LEFT));
                itemsTable.addCell(createItemCell(item.getHsnCode(), TextAlignment.CENTER));
                itemsTable.addCell(createItemCell(String.valueOf(item.getQuantity()), TextAlignment.RIGHT));
                itemsTable.addCell(createItemCell(String.format("%.2f", item.getRatePerKg()), TextAlignment.RIGHT));
                itemsTable.addCell(createItemCell(String.format("%.2f", item.getTotalValue()), TextAlignment.RIGHT));
                itemsTable.addCell(createItemCell(String.valueOf(item.getCgstPercent()), TextAlignment.CENTER));
                itemsTable.addCell(createItemCell(String.format("%.2f", item.getCgstAmount()), TextAlignment.RIGHT));
                itemsTable.addCell(createItemCell(String.valueOf(item.getSgstPercent()), TextAlignment.CENTER));
                itemsTable.addCell(createItemCell(String.format("%.2f", item.getSgstAmount()), TextAlignment.RIGHT));
            }
            
            document.add(itemsTable);
            
            // Totals Section
            Table totalsTable = new Table(new float[]{3, 1}).useAllAvailableWidth().setMarginBottom(10);
            totalsTable.addCell(createTotalCell("Total Value:", false));
            totalsTable.addCell(createTotalCell("₹" + String.format("%.2f", invoice.getTotalValue()), true));
            totalsTable.addCell(createTotalCell("Total CGST:", false));
            totalsTable.addCell(createTotalCell("₹" + String.format("%.2f", invoice.getTotalCgst()), true));
            totalsTable.addCell(createTotalCell("Total SGST:", false));
            totalsTable.addCell(createTotalCell("₹" + String.format("%.2f", invoice.getTotalSgst()), true));
            totalsTable.addCell(createTotalCell("Grand Total:", false).setBackgroundColor(PRIMARY_COLOR).setFontColor(ColorConstants.WHITE).setBold());
            totalsTable.addCell(createTotalCell("₹" + String.format("%.2f", invoice.getGrandTotal()), true).setBackgroundColor(PRIMARY_COLOR).setFontColor(ColorConstants.WHITE).setBold());
            document.add(totalsTable);
            
            // Footer
            Paragraph footer = new Paragraph("Thank you for your business!")
                .setFontSize(10)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setFontColor(new DeviceRgb(128, 128, 128));
            document.add(footer);
            
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
    
    private Cell createCell(String text, boolean bold, TextAlignment alignment) {
        Paragraph p = new Paragraph(text).setFontSize(10);
        if (bold) p.setBold();
        return new Cell().add(p).setTextAlignment(alignment).setBorder(Border.NO_BORDER);
    }
    
    private Cell createItemCell(String text, TextAlignment alignment) {
        return new Cell()
            .add(new Paragraph(text).setFontSize(9))
            .setTextAlignment(alignment)
            .setPadding(4)
            .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
    }
    
    private Cell createTotalCell(String text, boolean isValue) {
        Cell cell = new Cell()
            .add(new Paragraph(text).setFontSize(11))
            .setPadding(5)
            .setBorder(Border.NO_BORDER);
        if (isValue) {
            cell.setTextAlignment(TextAlignment.RIGHT);
        } else {
            cell.setTextAlignment(TextAlignment.RIGHT);
        }
        return cell;
    }
}
