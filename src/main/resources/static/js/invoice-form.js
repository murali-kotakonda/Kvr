let itemCounter = 0;

// Load seller address when user is selected
async function loadSellerAddress() {
    const sellerSelect = document.getElementById('sellerSelect');
    const sellerId = sellerSelect.value;
    const fromName = document.getElementById('fromName');
    const fromAddress = document.getElementById('fromAddress');
    const fromGst = document.getElementById('fromGst');
    
    if (!sellerId) {
        fromName.value = '';
        fromAddress.value = '';
        fromGst.value = '';
        return;
    }
    
    const selectedOption = sellerSelect.selectedOptions[0];
    fromName.value = selectedOption.text.split('(')[0].trim();
    fromGst.value = selectedOption.getAttribute('data-gstin') || '';
    
    try {
        const response = await fetch(`/api/address/USER/${sellerId}`);
        if (response.ok) {
            const address = await response.json();
            fromAddress.value = formatAddress(address);
        }
    } catch (error) {
        console.error('Error loading seller address:', error);
    }
}

// Load buyer address when client is selected
async function loadBuyerAddress() {
    const buyerSelect = document.getElementById('buyerSelect');
    const buyerId = buyerSelect.value;
    const toName = document.getElementById('toName');
    const toAddress = document.getElementById('toAddress');
    const toGst = document.getElementById('toGst');
    
    if (!buyerId) {
        toName.value = '';
        toAddress.value = '';
        toGst.value = '';
        toName.readOnly = false;
        toAddress.readOnly = false;
        toGst.readOnly = false;
        toName.style.background = '';
        toAddress.style.background = '';
        toGst.style.background = '';
        return;
    }
    
    if (buyerId === 'others') {
        toName.value = '';
        toAddress.value = '';
        toGst.value = '';
        toName.readOnly = false;
        toAddress.readOnly = false;
        toGst.readOnly = false;
        toName.style.background = '';
        toAddress.style.background = '';
        toGst.style.background = '';
        return;
    }
    
    const selectedOption = buyerSelect.selectedOptions[0];
    toName.value = selectedOption.text;
    toGst.value = selectedOption.getAttribute('data-gstin') || '';
    toName.readOnly = true;
    toGst.readOnly = true;
    toName.style.background = '#f5f5f5';
    toGst.style.background = '#f5f5f5';
    
    try {
        const response = await fetch(`/api/address/CLIENT/${buyerId}`);
        if (response.ok) {
            const address = await response.json();
            toAddress.value = formatAddress(address);
            toAddress.readOnly = true;
            toAddress.style.background = '#f5f5f5';
        }
    } catch (error) {
        console.error('Error loading buyer address:', error);
    }
}

// Format address object to string
function formatAddress(address) {
    const parts = [];
    if (address.houseNo) parts.push(address.houseNo);
    if (address.address1) parts.push(address.address1);
    if (address.address2) parts.push(address.address2);
    if (address.city) parts.push(address.city);
    if (address.state) parts.push(address.state);
    if (address.country) parts.push(address.country);
    if (address.pincode) parts.push(address.pincode);
    return parts.join(',\n');
}

function addRow() {
    itemCounter++;
    const tbody = document.getElementById('itemsBody');
    const row = document.createElement('tr');
    row.className = 'item-row';
    row.innerHTML = `
        <td>${itemCounter}</td>
        <td><input type="text" class="description" required></td>
        <td><input type="text" class="hsnCode"></td>
        <td><input type="number" class="quantity" step="0.01" onchange="calculateRow(this)" required></td>
        <td><input type="number" class="ratePerKg" step="0.01" onchange="calculateRow(this)" required></td>
        <td><input type="number" class="totalValue" readonly></td>
        <td><input type="number" class="cgstPercent" step="0.01" onchange="calculateRow(this)" value="9"></td>
        <td><input type="number" class="cgstAmount" readonly></td>
        <td><input type="number" class="sgstPercent" step="0.01" onchange="calculateRow(this)" value="9"></td>
        <td><input type="number" class="sgstAmount" readonly></td>
        <td><button type="button" class="remove-btn" onclick="removeRow(this)">Remove</button></td>
    `;
    tbody.appendChild(row);
}

function removeRow(btn) {
    btn.closest('tr').remove();
    updateSerialNumbers();
    calculateTotals();
}

function updateSerialNumbers() {
    const rows = document.querySelectorAll('#itemsBody tr');
    rows.forEach((row, index) => {
        row.cells[0].textContent = index + 1;
    });
    itemCounter = rows.length;
}

function calculateRow(input) {
    const row = input.closest('tr');
    const quantity = parseFloat(row.querySelector('.quantity').value) || 0;
    const rate = parseFloat(row.querySelector('.ratePerKg').value) || 0;
    const cgstPercent = parseFloat(row.querySelector('.cgstPercent').value) || 0;
    const sgstPercent = parseFloat(row.querySelector('.sgstPercent').value) || 0;
    
    const totalValue = quantity * rate;
    row.querySelector('.totalValue').value = totalValue.toFixed(2);
    
    const cgstAmount = (totalValue * cgstPercent) / 100;
    row.querySelector('.cgstAmount').value = cgstAmount.toFixed(2);
    
    const sgstAmount = (totalValue * sgstPercent) / 100;
    row.querySelector('.sgstAmount').value = sgstAmount.toFixed(2);
    
    calculateTotals();
}

function calculateTotals() {
    let totalValue = 0;
    let totalCgst = 0;
    let totalSgst = 0;
    
    document.querySelectorAll('#itemsBody tr').forEach(row => {
        totalValue += parseFloat(row.querySelector('.totalValue').value) || 0;
        totalCgst += parseFloat(row.querySelector('.cgstAmount').value) || 0;
        totalSgst += parseFloat(row.querySelector('.sgstAmount').value) || 0;
    });
    
    const grandTotal = totalValue + totalCgst + totalSgst;
    
    document.getElementById('totalValue').textContent = totalValue.toFixed(2);
    document.getElementById('totalCgst').textContent = totalCgst.toFixed(2);
    document.getElementById('totalSgst').textContent = totalSgst.toFixed(2);
    document.getElementById('grandTotal').textContent = grandTotal.toFixed(2);
    
    // Show amount in words if grand total > 0
    const amountInWordsElement = document.getElementById('amountInWords');
    if (grandTotal > 0) {
        amountInWordsElement.textContent = numberToWords(grandTotal);
    } else {
        amountInWordsElement.textContent = '';
    }
}

// Convert number to words (Indian numbering system)
function numberToWords(amount) {
    const units = ['', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine'];
    const teens = ['Ten', 'Eleven', 'Twelve', 'Thirteen', 'Fourteen', 'Fifteen', 'Sixteen', 'Seventeen', 'Eighteen', 'Nineteen'];
    const tens = ['', '', 'Twenty', 'Thirty', 'Forty', 'Fifty', 'Sixty', 'Seventy', 'Eighty', 'Ninety'];
    
    if (amount === 0) return 'Zero Rupees Only';
    
    const rupees = Math.floor(amount);
    const paise = Math.round((amount - rupees) * 100);
    
    let words = convertToWords(rupees) + ' Rupees';
    if (paise > 0) {
        words += ' and ' + convertToWords(paise) + ' Paise';
    }
    words += ' Only';
    
    return words;
    
    function convertToWords(num) {
        if (num === 0) return 'Zero';
        if (num < 10) return units[num];
        if (num < 20) return teens[num - 10];
        if (num < 100) return tens[Math.floor(num / 10)] + (num % 10 > 0 ? ' ' + units[num % 10] : '');
        if (num < 1000) return units[Math.floor(num / 100)] + ' Hundred' + (num % 100 > 0 ? ' ' + convertToWords(num % 100) : '');
        if (num < 100000) return convertToWords(Math.floor(num / 1000)) + ' Thousand' + (num % 1000 > 0 ? ' ' + convertToWords(num % 1000) : '');
        if (num < 10000000) return convertToWords(Math.floor(num / 100000)) + ' Lakh' + (num % 100000 > 0 ? ' ' + convertToWords(num % 100000) : '');
        return convertToWords(Math.floor(num / 10000000)) + ' Crore' + (num % 10000000 > 0 ? ' ' + convertToWords(num % 10000000) : '');
    }
}

document.getElementById('invoiceForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const items = [];
    document.querySelectorAll('#itemsBody tr').forEach((row, index) => {
        items.push({
            serialNo: index + 1,
            description: row.querySelector('.description').value,
            hsnCode: row.querySelector('.hsnCode').value,
            quantity: parseFloat(row.querySelector('.quantity').value),
            ratePerKg: parseFloat(row.querySelector('.ratePerKg').value),
            cgstPercent: parseFloat(row.querySelector('.cgstPercent').value),
            sgstPercent: parseFloat(row.querySelector('.sgstPercent').value)
        });
    });
    
    const invoice = {
        invoiceDate: document.getElementById('invoiceDate').value,
        fromName: document.getElementById('fromName').value,
        fromAddress: document.getElementById('fromAddress').value,
        fromGst: document.getElementById('fromGst').value,
        toName: document.getElementById('toName').value,
        toAddress: document.getElementById('toAddress').value,
        toGst: document.getElementById('toGst').value,
        stateCode: document.getElementById('stateCode').value,
        vehicleNumber: document.getElementById('vehicleNumber').value,
        items: items
    };
    
    try {
        const response = await fetch('/api/invoice', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(invoice)
        });
        
        if (response.ok) {
            const savedInvoice = await response.json();
            window.location.href = `/invoice/${savedInvoice.id}`;
        } else {
            alert('Error creating invoice');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error creating invoice');
    }
});

// Set today's date as default
document.getElementById('invoiceDate').valueAsDate = new Date();

// Add first row by default
addRow();
