# Quick Start Guide - Invoice Generator

## Run the Application (3 Simple Steps)

### Step 1: Build the Project
```bash
cd c:\Users\MURALI\Documents\GitHub\Kvr
mvn clean install
```

### Step 2: Run the Application
```bash
mvn spring-boot:run
```

### Step 3: Open in Browser
```
http://localhost:8080
```

## What You'll See

1. **Home Page** - Two buttons:
   - Create New Invoice
   - View All Invoices

2. **Create Invoice Form** - Fill in:
   - Invoice Number & Date
   - From/To Addresses
   - State Code, GSTIN, Vehicle Number
   - Add multiple bill items
   - Auto-calculated totals

3. **Invoice View** - Professional invoice display with:
   - All details formatted
   - Itemized bill table
   - Total amount in words
   - Print button

## Sample Test Data

**Invoice Details:**
- Invoice Number: INV-2024-001
- Date: Today's date (auto-filled)

**From (Seller):**
- Name: KVR Industries
- Address: Plot No. 123, Industrial Area, Phase-2, Mumbai, Maharashtra - 400001

**To (Buyer):**
- Name: ABC Traders Pvt Ltd
- Address: Shop No. 45, Market Road, Delhi - 110001

**Additional Details:**
- State Code: 27
- GSTIN: 27AABCU9603R1ZM
- Vehicle Number: MH-12-AB-1234

**Bill Items:**

Item 1:
- Description: Steel Rods (Grade A)
- HSN Code: 7214
- Quantity: 100
- Rate/kg: 50
- CGST %: 9
- SGST %: 9

Item 2:
- Description: Iron Sheets
- HSN Code: 7209
- Quantity: 50
- Rate/kg: 75
- CGST %: 9
- SGST %: 9

**Expected Totals:**
- Total Value: ₹8,750.00
- Total CGST: ₹787.50
- Total SGST: ₹787.50
- Grand Total: ₹10,325.00
- In Words: Ten Thousand Three Hundred Twenty Five Rupees Only

## Key Features to Test

1. **Dynamic Rows**: Click "Add Item" to add more rows
2. **Auto-Calculation**: Change quantity/rate and see totals update
3. **Remove Items**: Click "Remove" button on any row
4. **Form Validation**: Try submitting without required fields
5. **View Invoice**: After creation, see formatted invoice
6. **Print**: Click "Print Invoice" for print preview
7. **List View**: Go to "View All Invoices" to see all created invoices

## Database Access (H2 Console)

URL: http://localhost:8080/h2-console

**Connection Details:**
- JDBC URL: jdbc:h2:mem:invoicedb
- Username: sa
- Password: (leave empty)

**Tables:**
- invoices
- invoice_items

## API Testing (Optional)

### Create Invoice via API
```bash
curl -X POST http://localhost:8080/api/invoice \
  -H "Content-Type: application/json" \
  -d '{
    "invoiceNumber": "INV-API-001",
    "invoiceDate": "2024-01-15",
    "fromName": "Test Seller",
    "fromAddress": "Seller Address",
    "toName": "Test Buyer",
    "toAddress": "Buyer Address",
    "stateCode": "27",
    "gstin": "27XXXXX1234X1Z5",
    "vehicleNumber": "MH-01-AB-1234",
    "items": [
      {
        "serialNo": 1,
        "description": "Test Item",
        "hsnCode": "1234",
        "quantity": 10,
        "ratePerKg": 100,
        "cgstPercent": 9,
        "sgstPercent": 9
      }
    ]
  }'
```

### Get Invoice by ID
```bash
curl http://localhost:8080/api/invoice/1
```

## Troubleshooting

**Application won't start:**
- Check Java version: `java -version` (should be 17+)
- Check if port 8080 is free
- Look at console for error messages

**Can't see invoices:**
- H2 is in-memory, data resets on restart
- Check H2 console to verify data

**Build fails:**
- Run: `mvn clean`
- Delete `target` folder
- Run: `mvn install` again

## Next Steps

1. ✅ Test the application with sample data
2. ✅ Create multiple invoices
3. ✅ Test print functionality
4. 📝 Customize CSS for your branding
5. 📝 Add PDF export feature (see README.md)
6. 📝 Switch to MySQL for production (see README.md)

## Stop the Application

Press `Ctrl + C` in the terminal where the application is running.

---

**Enjoy using the Invoice Generator! 🎉**
