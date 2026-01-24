package com.example.bakery.service;

import com.example.bakery.component.Cart;
import com.example.bakery.entity.Orderrequest;
import com.example.bakery.entity.CategoryItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class pdfService {
public ByteArrayInputStream createPdf(Orderrequest order, Cart cart, Long orderId) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // HEADER
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.ORANGE);
            Paragraph title = new Paragraph("Kirat Bakers", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // NEW: SHOW ORDER ID
            Paragraph idPara = new Paragraph("Order ID: #" + orderId, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            idPara.setAlignment(Element.ALIGN_CENTER);
            document.add(idPara);
            
            document.add(new Paragraph("\n"));
            // 2. CUSTOMER DETAILS
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            document.add(new Paragraph("Billed To:", headerFont));
            document.add(new Paragraph("Name: " + order.getFullName()));
            document.add(new Paragraph("Phone (+1): " + order.getPhoneNumber()));
            document.add(new Paragraph("Address: " + order.getAddress()));
            document.add(new Paragraph("\n")); // Space

            // 3. ORDER TABLE
            PdfPTable table = new PdfPTable(4); // 4 Columns
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 2, 2, 2}); // Relative widths

            // Table Headers
            addHeader(table, "Item Name");
            addHeader(table, "Price ($)");
            addHeader(table, "Qty");
            addHeader(table, "Total ($)");

            // Table Rows (Loop through Cart)
            for (CategoryItem item : cart.getItems()) {
                table.addCell(item.getItemName());
                table.addCell(String.valueOf(item.getPrice()));
                table.addCell(item.getQuantity());
                table.addCell(String.valueOf(item.getTotal()));
            }

            document.add(table);

            // 4. GRAND TOTAL
            Paragraph totalPara = new Paragraph("\nGrand Total: $" + cart.getTotalPrice(), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPara);
            
            // 5. FOOTER
            Paragraph footer = new Paragraph("\nThank you for ordering with Kirat Bakers!", 
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

// NEW METHOD FOR CUSTOM BILLS
    public ByteArrayInputStream createManualPdf(String name, String phone, 
                                                String[] itemNames, 
                                                Double[] itemPrices, 
                                                Integer[] itemQuantities,
                                                String customId) { // <--- NEW PARAM
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // HEADER
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.ORANGE);
            Paragraph title = new Paragraph("Kirat Bakers", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // NEW: SHOW CUSTOM ID
            Paragraph idPara = new Paragraph("Bill Ref: " + customId, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            idPara.setAlignment(Element.ALIGN_CENTER);
            document.add(idPara);

            document.add(new Paragraph("\n"));

            // 2. Customer Info
            document.add(new Paragraph("Billed To: " + name));
            document.add(new Paragraph("Phone: " + phone));
            document.add(new Paragraph("Date: " + java.time.LocalDate.now()));
            document.add(new Paragraph("\n"));

            // 3. Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 2, 2, 2});

            addHeader(table, "Item Description");
            addHeader(table, "Price");
            addHeader(table, "Qty");
            addHeader(table, "Total");

            double grandTotal = 0.0;

            // Loop through the arrays
            for (int i = 0; i < itemNames.length; i++) {
                double lineTotal = itemPrices[i] * itemQuantities[i];
                grandTotal += lineTotal;

                table.addCell(itemNames[i]);
                table.addCell("$" + itemPrices[i]);
                table.addCell(String.valueOf(itemQuantities[i]));
                table.addCell("$" + String.format("%.2f", lineTotal));
            }

            document.add(table);

            // 4. Total
            Paragraph totalPara = new Paragraph("\nGrand Total: $" + String.format("%.2f", grandTotal), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPara);
            
            // 5. Footer
            Paragraph footer = new Paragraph("\n*Custom Bill generated by Admin*", 
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }    
}
