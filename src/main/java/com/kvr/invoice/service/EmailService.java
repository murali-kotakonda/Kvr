package com.kvr.invoice.service;

import com.kvr.invoice.model.Invoice;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final PdfService pdfService;
    
    @Value("${spring.mail.username:noreply@kvr.com}")
    private String fromEmail;
    
    public void sendInvoiceEmail(Invoice invoice, String toEmail, String recipientName, boolean isSeller) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Invoice #" + invoice.getInvoiceNumber() + " - KVR Associates");
            
            String emailBody = buildEmailBody(invoice, recipientName, isSeller);
            helper.setText(emailBody, true);
            
            // Attach PDF
            byte[] pdfBytes = pdfService.generateInvoicePdf(invoice);
            helper.addAttachment("Invoice-" + invoice.getInvoiceNumber() + ".pdf", 
                               new ByteArrayResource(pdfBytes));
            
            mailSender.send(message);
            log.info("Invoice email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
    
    private String buildEmailBody(Invoice invoice, String recipientName, boolean isSeller) {
        String role = isSeller ? "Seller" : "Buyer";
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
                    <h2 style="color: #6366f1; text-align: center;">Tax Invoice</h2>
                    <p>Dear %s,</p>
                    <p>Please find attached the tax invoice for your reference.</p>
                    <div style="background: #f5f5f5; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p style="margin: 5px 0;"><strong>Invoice Number:</strong> %s</p>
                        <p style="margin: 5px 0;"><strong>Date:</strong> %s</p>
                        <p style="margin: 5px 0;"><strong>Grand Total:</strong> ₹%s</p>
                        <p style="margin: 5px 0;"><strong>Your Role:</strong> %s</p>
                    </div>
                    <p>Thank you for your business!</p>
                    <hr style="border: none; border-top: 1px solid #ddd; margin: 20px 0;">
                    <p style="font-size: 12px; color: #666; text-align: center;">
                        KVR Associates<br>
                        This is an automated email. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """, 
            recipientName,
            invoice.getInvoiceNumber(),
            invoice.getInvoiceDate(),
            String.format("%.2f", invoice.getGrandTotal()),
            role
        );
    }
}
