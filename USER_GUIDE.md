## MedApp: User Guide

Welcome to MedApp! This document will guide you through the features and functionalities of the application, helping you manage medicines, sales, and history efficiently.

---

### 1. Introduction to MedApp

MedApp is a comprehensive application designed to streamline medicine management for pharmacies and stores. It allows you to:
*   Maintain a detailed inventory of medicines.
*   Manage stock levels and track expiry dates.
*   Process sales quickly and accurately.
*   Generate reports for sales and purchases.
*   Keep a historical record of all transactions.

---

### 2. Getting Started

To use MedApp, ensure the application is running. Once launched, you will see the main navigation options to access different features.

---

### 3. Medicine Inventory

The Medicine Inventory page provides an overview of all medicines currently in your system.

**How to View Medicines:**
1.  Navigate to the **Medicine Inventory** section from the main menu.
2.  You will see a table listing all medicines, along with their available stock and price.

**How to View Medicine Details (Popup):**
1.  In the Medicine Inventory table, locate the medicine you want to view.
2.  Click the **"View Details"** action button (usually represented by an icon) next to the medicine.
3.  A popup dialog will appear displaying detailed information about the medicine, including:
    *   Medicine Name
    *   Current Stock (Total Quantity and Latest Price)
    *   Stock History (list of batches, quantity, price, and expiry date).

**How to View Stock History (including Finished Stock):**
1.  In the Medicine Details popup, you will see the "Stock History" section.
2.  There is a toggle switch labeled **"Include Finished Stock"**.
3.  **Toggle this switch ON** to view batches that have an available quantity of zero (finished stock).
4.  **Toggle this switch OFF** to only view batches that still have available stock.

**Note on Expiry Dates:**
*   Expiry dates in the Stock History table are color-coded for quick identification:
    *   **Red:** Expiring within 30 days or already expired.
    *   **Orange:** Expiring within 91 days.
    *   **Green:** Expiring in more than 91 days.
*   Expired items will also show "Expired X days ago" in their expiry text.

---

### 4. Stock Management (Adding New Medicines and Stock)

This section allows you to add new medicines to your system and update the stock for existing ones.

**How to Add a New Medicine Name:**
1.  Navigate to the **Stock Management** section.
2.  Look for the "Add Medicine Name" section.
3.  Enter the name of the new medicine.
4.  Click **"Add Medicine"**. A success message will confirm the addition.

**How to Add Medicine Stock (Purchase):**
1.  In the **Stock Management** section, find the "Add Medicine Stock (Purchase)" area.
2.  **Select Medicine:** Start typing the medicine name in the "Medicine" field and select it from the autocomplete suggestions.
3.  **Expiration Date:** Choose the expiry date for this batch using the date picker.
4.  **Quantity:** Enter the quantity of medicines in this new batch.
5.  **Price:** Enter the per-unit purchase price for this batch.
6.  Click **"Add Stock"**.
7.  Upon success, a detailed toast notification will appear, confirming: "{Medicine Name} updated with quantity {X} having price ₹{Y} and Exp Date is {DD/MM/YY}". This toast will automatically disappear after 30 seconds.

**How to Bulk Upload Stock via CSV:**
1.  In the **Stock Management** section, locate the "Bulk Upload Stock via CSV" area.
2.  **Prepare your CSV file:** The CSV file should have a header row and subsequent rows with the following format:
    `medicineName,expDate,quantity,price`
    *   `medicineName`: Name of the medicine (e.g., Paracetamol)
    *   `expDate`: Expiration date in `DD-MM-YYYY` format (e.g., 31-12-2025)
    *   `quantity`: Integer quantity (e.g., 100)
    *   `price`: Decimal price (e.g., 25.50)
3.  Click **"Choose File"** to select your CSV file.
4.  Click **"Upload Stock"**.
5.  A dialog will show the results of the bulk upload, indicating successful and failed entries with reasons for failure.

---

### 5. Sell Medicine

The Sell Medicine page allows you to process sales transactions.

**How to Add Items to Cart:**
1.  Navigate to the **Sell Medicine** section.
2.  **Select Medicine:** Start typing the medicine name in the "Medicine" field and select it from the autocomplete suggestions.
3.  **Select Batches:** Available batches for the selected medicine will appear. Click on each batch you wish to sell.
4.  **Enter Quantity:** For each selected batch, enter the quantity you wish to sell.
5.  **Apply Discount (Optional):** Enter a discount percentage (0-100) for individual items if applicable.
6.  Click **"Add to Cart"**. The item will be added to the "Cart Items" table below.

**How to Complete a Sell:**
1.  After adding all desired items to the cart, verify the "Total" amount.
2.  **Customer Name (Optional):** Enter the customer's name if desired.
3.  **Mode of Payment:** Select the payment method (Cash, Card, or UPI) from the dropdown.
4.  Click **"Complete Sell"**.
5.  A "Sell Receipt Preview" dialog will appear, showing a summary of the transaction.
6.  You can then choose to:
    *   **"Complete Sell and Print Receipt"**: Finalizes the sell and initiates printing.
    *   **"Complete Sell without Print"**: Finalizes the sell without printing a physical receipt.
    *   **"Cancel"**: Closes the dialog without completing the sell.
7.  Upon successful completion, a toast notification "Sale completed successfully!" will appear and disappear after 30 seconds. Error notifications will also appear for 30 seconds.

---

### 6. Sales History

The Sales History page allows you to review past sales records.

**How to View Sales Records:**
1.  Navigate to the **Sells History** section.
2.  By default, it will show sales for the current day.

**How to Use the Date Filter:**
1.  You will see a single "Date" input field by default.
2.  **Single Date Mode:** Select a date to view sales for that specific day.
3.  **Enable Date Range Mode:** Click the **"Enable Date Range Mode"** toggle switch (located above the date inputs). This will reveal "From Date" and "To Date" input fields.
4.  **Date Range Mode:** Select your desired "From Date" and "To Date".
5.  Click **"Search"** to apply the filter.

**How to Use the "Quick Report" Feature:**
1.  After performing a search (either for a single date or a range), click the **"Quick Report"** button.
2.  **Note:** This button will be disabled if you change the dates without clicking "Search", or if no sales data is found.
3.  A popup dialog will appear with a sales summary for the selected period:
    *   **Title:** "Sales Report for {From Date} to {To Date}" (or just "{Date}" if from and to dates are the same).
    *   **Content:**
        *   **Total Sales:** The combined total of all sales after applying discounts.
        *   **Cash Sales:** Total sales made via Cash.
        *   **UPI Sales:** Total sales made via UPI.
        *   **Card Sales:** Total sales made via Card.
        *   **Other Sales:** (If applicable) Total sales made via any other unclassified mode of payment.

---

### 7. Purchase History

The Purchase History page allows you to review past medicine purchase records.

**How to View Purchase Records:**
1.  Navigate to the **Purchase History** section.
2.  By default, it will show purchases for the current day.

**How to Use the Date Filter:**
1.  Similar to Sales History, you will see a single "Date" input field by default.
2.  **Single Date Mode:** Select a date to view purchases for that specific day.
3.  **Enable Date Range Mode:** Click the **"Enable Date Range Mode"** toggle switch. This will reveal "From Date" and "To Date" input fields.
4.  **Date Range Mode:** Select your desired "From Date" and "To Date".
5.  Click **"Search"** to apply the filter.

**How to See the "Total Purchased Amount":**
1.  After performing a search, a **"Total Purchased Amount"** will be displayed at the bottom of the table.
2.  This value represents the sum of `(Quantity * Price/Unit)` for all items purchased within the selected date range.

---

### 8. Important Notes & Tips

*   **Currency Formatting:** All monetary amounts are displayed in the Indian Rupee format (e.g., ₹5,52,412.32).
*   **Notifications:** Success and error messages appear as toast notifications at the bottom left of the screen and will automatically disappear after 30 seconds.
*   **Responsiveness:** The application's layout is designed to adapt to various screen sizes.

---

If you have any further questions or encounter issues, please consult your administrator or support.

--- 