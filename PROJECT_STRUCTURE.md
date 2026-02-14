# Invoice Generator - Complete Project Structure

## 📁 Project Overview

```
Kvr/
├── src/
│   └── main/
│       ├── java/com/kvr/invoice/
│       │   ├── InvoiceApplication.java          ⭐ Main Spring Boot Application
│       │   ├── controller/
│       │   │   └── InvoiceController.java       🎮 REST APIs & Web Controllers
│       │   ├── model/
│       │   │   ├── Invoice.java                 📄 Invoice Entity (JPA)
│       │   │   └── InvoiceItem.java             📄 Invoice Item Entity (JPA)
│       │   ├── repository/
│       │   │   └── InvoiceRepository.java       💾 Database Repository
│       │   ├── service/
│       │   │   └── InvoiceService.java          ⚙️ Business Logic & Calculations
│       │   └── util/
│       │       └── NumberToWordsUtil.java       🔢 Number to Words Converter
│       └── resources/
│           ├── application.properties            ⚙️ Configuration
│           ├── sample-data.sql                   📊 Sample Test Data
│           ├── static/
│           │   ├── css/
│           │   │   └── style.css                🎨 Styling
│           │   └── js/
│           │       └── invoice-form.js          ⚡ Dynamic Form Logic
│           └── templates/
│               ├── index.html                    🏠 Home Page
│               ├── invoice-form.html             📝 Create Invoice Form
│               ├── invoice-view.html             👁️ View Invoice
│               └── invoice-list.html             📋 List All Invoices
├── pom.xml                                       📦 Maven Dependencies
├── .gitignore                                    🚫 Git Ignore Rules
├── README.md                                     📖 Full Documentation
└── QUICKSTART.md                                 🚀 Quick Start Guide
```

## 🎯 Key Components

### Backend (Java/Spring Boot)

1. **InvoiceApplication.java**
   - Main entry point
   - Starts Spring Boot application

2. **InvoiceController.java**
   - Handles HTTP requests
   - REST API endpoints: `/api/invoice`
   - Web page routes: `/`, `/invoice/new`, `/invoice/{id}`, `/invoices`

3. **Invoice.java & InvoiceItem.java**
   - JPA entities
   - Database table mappings
   - One-to-Many relationship

4. **InvoiceRepository.java**
   - Spring Data JPA repository
   - Database CRUD operations

5. **InvoiceService.java**
   - Business logic
   - GST calculations (CGST, SGST)
   - Total calculations
   - Number to words conversion

6. **NumberToWordsUtil.java**
   - Converts numbers to Indian format words
   - Handles Rupees and Paise

### Frontend (Thymeleaf/HTML/CSS/JS)

1. **index.html**
   - Landing page
   - Navigation menu

2. **invoice-form.html**
   - Dynamic invoice creation form
   - Add/remove item rows
   - Real-time calculations

3. **invoice-view.html**
   - Professional invoice display
   - Print-friendly layout
   - Shows amount in words

4. **invoice-list.html**
   - Lists all invoices
   - Quick view links

5. **style.css**
   - Responsive design
   - Print-optimized styles
   - Professional appearance

6. **invoice-form.js**
   - Dynamic row management
   - Auto-calculations
   - Form submission via AJAX

### Configuration

1. **application.properties**
   - Server port: 8080
   - H2 database (in-memory)
   - JPA/Hibernate settings
   - MySQL configuration (commented)

2. **pom.xml**
   - Spring Boot 3.2.0
   - Dependencies: Web, JPA, Thymeleaf, MySQL, H2, Lombok, iText

## 🔄 Application Flow

```
User Request
    ↓
InvoiceController (receives request)
    ↓
InvoiceService (business logic)
    ↓
InvoiceRepository (database operations)
    ↓
Database (H2/MySQL)
    ↓
Response (JSON/HTML)
    ↓
User Interface
```

## 📊 Database Schema

### Table: invoices
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary Key (Auto) |
| invoice_number | VARCHAR | Unique invoice number |
| invoice_date | DATE | Invoice date |
| from_name | VARCHAR | Seller name |
| from_address | VARCHAR | Seller address |
| to_name | VARCHAR | Buyer name |
| to_address | VARCHAR | Buyer address |
| state_code | VARCHAR | State code |
| gstin | VARCHAR | GST identification |
| vehicle_number | VARCHAR | Vehicle number |
| total_value | DOUBLE | Sum of item values |
| total_cgst | DOUBLE | Total CGST amount |
| total_sgst | DOUBLE | Total SGST amount |
| grand_total | DOUBLE | Final total |
| grand_total_in_words | VARCHAR | Amount in words |

### Table: invoice_items
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary Key (Auto) |
| invoice_id | BIGINT | Foreign Key → invoices |
| serial_no | INT | Item serial number |
| description | VARCHAR | Item description |
| hsn_code | VARCHAR | HSN code |
| quantity | DOUBLE | Quantity |
| rate_per_kg | DOUBLE | Rate per kg |
| total_value | DOUBLE | Quantity × Rate |
| cgst_percent | DOUBLE | CGST percentage |
| cgst_amount | DOUBLE | CGST amount |
| sgst_percent | DOUBLE | SGST percentage |
| sgst_amount | DOUBLE | SGST amount |

## 🌐 API Endpoints

### REST APIs
- `POST /api/invoice` - Create new invoice
- `GET /api/invoice/{id}` - Get invoice by ID

### Web Pages
- `GET /` - Home page
- `GET /invoice/new` - Create invoice form
- `GET /invoice/{id}` - View invoice
- `GET /invoices` - List all invoices

## 🧮 Calculation Logic

```
For each item:
  Total Value = Quantity × Rate/kg
  CGST Amount = (Total Value × CGST%) / 100
  SGST Amount = (Total Value × SGST%) / 100

For invoice:
  Total Value = Sum of all item Total Values
  Total CGST = Sum of all item CGST Amounts
  Total SGST = Sum of all item SGST Amounts
  Grand Total = Total Value + Total CGST + Total SGST
  Grand Total in Words = NumberToWordsUtil.convert(Grand Total)
```

## 🚀 Running the Application

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Access
http://localhost:8080
```

## 📝 Features Implemented

✅ Full-stack Spring Boot application
✅ RESTful API endpoints
✅ Thymeleaf templates for UI
✅ JPA/Hibernate for persistence
✅ H2 in-memory database (development)
✅ MySQL support (production-ready)
✅ Dynamic invoice form
✅ Auto-calculation of GST
✅ Number to words conversion (Indian format)
✅ Invoice viewing and listing
✅ Print-friendly invoice layout
✅ Responsive design
✅ Form validation
✅ AJAX form submission

## 🎨 Customization Options

1. **Branding**: Update CSS colors, logo in templates
2. **GST Rates**: Modify default CGST/SGST percentages
3. **Fields**: Add custom fields to Invoice/InvoiceItem entities
4. **PDF Export**: Implement using iText library (dependency included)
5. **Email**: Add email functionality for sending invoices
6. **Authentication**: Add Spring Security for user login
7. **Reports**: Add invoice reports and analytics

## 📚 Technologies Used

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Framework
- **Spring MVC** - Web layer
- **Spring Data JPA** - Data access
- **Hibernate** - ORM
- **Thymeleaf** - Template engine
- **H2 Database** - In-memory database
- **MySQL** - Production database
- **Maven** - Build tool
- **Lombok** - Boilerplate reduction
- **iText** - PDF generation (ready to use)
- **HTML5/CSS3/JavaScript** - Frontend

## 🎓 Learning Points

This project demonstrates:
- Spring Boot application structure
- RESTful API design
- JPA entity relationships (One-to-Many)
- Service layer pattern
- Repository pattern
- Thymeleaf templating
- AJAX form submission
- Dynamic DOM manipulation
- Responsive CSS design
- Business logic implementation
- Number formatting and conversion

---

**Ready to use! Start with QUICKSTART.md** 🚀
