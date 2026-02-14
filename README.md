# Invoice Generator - Spring Boot Application

A full-stack Java Spring Boot application for generating Bills/Tax Invoices with GST calculations.

## Features

- ✅ Create invoices with billing details
- ✅ Dynamic bill items table with auto-calculations
- ✅ CGST and SGST calculations
- ✅ Total amount in words (Indian numbering system)
- ✅ View and list all invoices
- ✅ Print-friendly invoice view
- ✅ REST API endpoints
- ✅ Persistent storage with JPA/Hibernate

## Technology Stack

- **Backend:** Spring Boot 3.2.0, Spring MVC, Spring Data JPA
- **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript
- **Database:** H2 (development), MySQL (production-ready)
- **Build Tool:** Maven
- **Java Version:** 17

## Project Structure

```
src/
├── main/
│   ├── java/com/kvr/invoice/
│   │   ├── InvoiceApplication.java          # Main application class
│   │   ├── controller/
│   │   │   └── InvoiceController.java       # REST & Web controllers
│   │   ├── model/
│   │   │   ├── Invoice.java                 # Invoice entity
│   │   │   └── InvoiceItem.java             # Invoice item entity
│   │   ├── repository/
│   │   │   └── InvoiceRepository.java       # JPA repository
│   │   ├── service/
│   │   │   └── InvoiceService.java          # Business logic
│   │   └── util/
│   │       └── NumberToWordsUtil.java       # Number to words converter
│   └── resources/
│       ├── application.properties            # Configuration
│       ├── templates/                        # Thymeleaf templates
│       │   ├── index.html
│       │   ├── invoice-form.html
│       │   ├── invoice-view.html
│       │   └── invoice-list.html
│       └── static/
│           ├── css/style.css
│           └── js/invoice-form.js
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL (optional, H2 is configured by default)

### Installation & Running

1. **Clone the repository**
   ```bash
   cd c:\Users\MURALI\Documents\GitHub\Kvr
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open browser: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console

### Database Configuration

**H2 (Default - In-Memory)**
- Already configured in `application.properties`
- No setup required

**MySQL (Production)**
1. Create database: `invoice_db`
2. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/invoice_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   ```

## API Endpoints

### REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/invoice` | Create new invoice |
| GET | `/api/invoice/{id}` | Get invoice by ID |

### Web Pages

| URL | Description |
|-----|-------------|
| `/` | Home page |
| `/invoice/new` | Create invoice form |
| `/invoice/{id}` | View invoice |
| `/invoices` | List all invoices |

## Usage Example

### Creating an Invoice

1. Navigate to http://localhost:8080
2. Click "Create New Invoice"
3. Fill in:
   - Invoice details (number, date)
   - From/To addresses
   - State Code, GSTIN, Vehicle Number
4. Add bill items:
   - Click "Add Item"
   - Enter description, HSN code, quantity, rate
   - CGST/SGST percentages (default 9%)
5. Totals calculate automatically
6. Click "Generate Invoice"
7. View formatted invoice with amount in words
8. Print or download as needed

### Sample Invoice Data

```json
{
  "invoiceNumber": "INV-001",
  "invoiceDate": "2024-01-15",
  "fromName": "ABC Industries",
  "fromAddress": "123 Industrial Area, Mumbai",
  "toName": "XYZ Traders",
  "toAddress": "456 Market Street, Delhi",
  "stateCode": "27",
  "gstin": "27XXXXX1234X1Z5",
  "vehicleNumber": "MH-01-AB-1234",
  "items": [
    {
      "description": "Steel Rods",
      "hsnCode": "7214",
      "quantity": 100,
      "ratePerKg": 50,
      "cgstPercent": 9,
      "sgstPercent": 9
    }
  ]
}
```

## Features in Detail

### Auto-Calculations
- **Total Value** = Quantity × Rate/kg
- **CGST Amount** = (Total Value × CGST%) / 100
- **SGST Amount** = (Total Value × SGST%) / 100
- **Grand Total** = Sum of all values + CGST + SGST

### Number to Words
Converts numeric amount to Indian numbering format:
- Example: 5900.00 → "Five Thousand Nine Hundred Rupees Only"

### Print Invoice
- Click "Print Invoice" button
- Browser print dialog opens
- Print-optimized CSS removes buttons

## Development

### Adding PDF Export

To add PDF generation capability:

1. Add iText dependency (already in pom.xml)
2. Create `PdfService.java`:
   ```java
   @Service
   public class PdfService {
       public byte[] generatePdf(Invoice invoice) {
           // PDF generation logic using iText
       }
   }
   ```
3. Add endpoint in controller:
   ```java
   @GetMapping("/invoice/{id}/pdf")
   public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
       // Return PDF as byte array
   }
   ```

## Troubleshooting

**Port 8080 already in use**
- Change port in `application.properties`: `server.port=8081`

**Database connection error**
- Check MySQL is running
- Verify credentials in `application.properties`

**Build errors**
- Ensure Java 17 is installed: `java -version`
- Clean and rebuild: `mvn clean install`

## License

This project is open-source and available for educational purposes.

## Support

For issues or questions, please create an issue in the repository.
