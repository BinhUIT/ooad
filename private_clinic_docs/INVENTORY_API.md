# API Documentation - Medicine Inventory & Import Management

## Overview

Backend APIs for managing Medicine Inventory (Screens 27-28) and Medicine Import (Screens 30-36).

---

## 1. Inventory APIs (Quản lý Kho thuốc)

### 1.1 Get Inventory List

**Endpoint:** `GET /api/inventory`

**Description:** Get paginated list of medicine inventory with search and filter options. Includes medicines with 0 quantity by default.

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | Page number (0-indexed) |
| size | Integer | No | 10 | Page size |
| sortBy | String | No | medicineId | Field to sort by (medicineId, medicineName, totalQuantity, nearestExpiryDate) |
| sortType | String | No | ASC | Sort direction (ASC/DESC) |
| keyword | String | No | - | Search by medicine name |
| includeOutOfStock | Boolean | No | true | Include medicines with 0 quantity |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {
    "content": [
      {
        "medicineId": 1,
        "medicineName": "Paracetamol",
        "unit": "TABLET",
        "totalQuantity": 100,
        "nearestExpiryDate": "2024-12-31",
        "manufacturer": "ABC Pharma",
        "concentration": "500mg",
        "storageCondition": "NORMAL"
      }
    ],
    "totalElements": 50,
    "totalPages": 5,
    "size": 10,
    "number": 0
  }
}
```

---

### 1.2 Get All Inventory Items

**Endpoint:** `GET /api/inventory/all`

**Description:** Get all inventory items without pagination.

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": [
    {
      "medicineId": 1,
      "medicineName": "Paracetamol",
      "unit": "TABLET",
      "totalQuantity": 100,
      "nearestExpiryDate": "2024-12-31",
      "manufacturer": "ABC Pharma",
      "concentration": "500mg",
      "storageCondition": "NORMAL"
    }
  ]
}
```

---

### 1.3 Get Inventory Detail

**Endpoint:** `GET /api/inventory/{id}`

**Description:** Get detailed inventory information for a specific medicine including all batches from different imports.

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Medicine ID |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {
    "medicineId": 1,
    "medicineName": "Paracetamol",
    "unit": "TABLET",
    "concentration": "500mg",
    "form": "Viên nén",
    "manufacturer": "ABC Pharma",
    "usageInstructions": "Uống sau ăn",
    "image": "url_to_image",
    "storageCondition": "NORMAL",
    "totalQuantity": 100,
    "nearestExpiryDate": "2024-12-31",
    "batches": [
      {
        "importId": 1,
        "medicineId": 1,
        "medicineName": "Paracetamol",
        "unit": "TABLET",
        "quantityInStock": 50,
        "expiryDate": "2024-12-31",
        "importPrice": 5000,
        "importDate": "2024-01-15",
        "supplier": "NCC ABC",
        "manufacturer": "ABC Pharma",
        "concentration": "500mg",
        "storageCondition": "NORMAL"
      }
    ]
  }
}
```

---

## 2. Medicine Import APIs (Quản lý Phiếu nhập)

### 2.1 Get Import List

**Endpoint:** `GET /api/imports`

**Description:** Get paginated list of medicine imports with search and filter options.

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | Page number (0-indexed) |
| size | Integer | No | 10 | Page size |
| sortBy | String | No | importId | Field to sort by |
| sortType | String | No | DESC | Sort direction (ASC/DESC) |
| fromDate | Date | No | - | Filter by import date from |
| toDate | Date | No | - | Filter by import date to |
| supplierName | String | No | - | Filter by supplier name |
| keyword | String | No | - | Search by supplier name |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {
    "content": [
      {
        "importId": 1,
        "importDate": "2024-01-15",
        "importerName": "Nguyễn Văn A",
        "supplier": "NCC ABC",
        "totalQuantity": 500,
        "totalValue": 25000000
      }
    ],
    "totalElements": 20,
    "totalPages": 2,
    "size": 10,
    "number": 0
  }
}
```

---

### 2.2 Get All Imports

**Endpoint:** `GET /api/imports/all`

**Description:** Get all medicine imports without pagination.

---

### 2.3 Get Import Detail

**Endpoint:** `GET /api/imports/{id}`

**Description:** Get detailed information about a specific medicine import including all imported items.

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Import ID |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {
    "importId": 1,
    "importDate": "2024-01-15",
    "importerId": 5,
    "importerName": "Nguyễn Văn A",
    "supplier": "NCC ABC",
    "totalQuantity": 500,
    "totalValue": 25000000,
    "details": [
      {
        "medicineId": 1,
        "medicineName": "Paracetamol",
        "unit": "TABLET",
        "quantity": 200,
        "importPrice": 5000,
        "totalAmount": 1000000,
        "expiryDate": "2024-12-31"
      }
    ]
  }
}
```

---

### 2.4 Create Import

**Endpoint:** `POST /api/imports`

**Description:** Create a new medicine import with details. This will automatically update the medicine inventory.

**Request Body:**

```json
{
  "supplier": "NCC ABC",
  "importDate": "2024-01-15",
  "details": [
    {
      "medicineId": 1,
      "quantity": 200,
      "importPrice": 5000,
      "expiryDate": "2024-12-31"
    },
    {
      "medicineId": 2,
      "quantity": 100,
      "importPrice": 10000,
      "expiryDate": "2024-06-30"
    }
  ]
}
```

**Validation Rules:**

- `supplier`: Required, max 100 characters
- `importDate`: Required
- `details`: Required, at least 1 item
- `details[].medicineId`: Required
- `details[].quantity`: Required, min 1
- `details[].importPrice`: Required, min 0
- `details[].expiryDate`: Required

**Response:** Same as Get Import Detail

---

### 2.5 Update Import

**Endpoint:** `PUT /api/imports/{id}`

**Description:** Update an existing medicine import. This will recalculate inventory (subtract old quantities, add new quantities).

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Import ID |

**Request Body:** Same as Create Import

**Response:** Same as Get Import Detail

---

### 2.6 Delete Import

**Endpoint:** `DELETE /api/imports/{id}`

**Description:** Delete a medicine import. This will also remove the corresponding inventory entries.

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Import ID |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": null
}
```

---

## 3. Supporting APIs (API Bổ trợ)

### 3.1 Get Medicines for Selection

**Endpoint:** `GET /api/medicines/selection`

**Description:** Get list of medicines for dropdown selection when creating import.

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": [
    {
      "medicineId": 1,
      "medicineName": "Paracetamol",
      "unit": "TABLET",
      "concentration": "500mg",
      "manufacturer": "ABC Pharma"
    }
  ]
}
```

---

### 3.2 Get Suppliers

**Endpoint:** `GET /api/suppliers`

**Description:** Get list of distinct supplier names for dropdown selection.

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": ["NCC ABC", "NCC XYZ", "Công ty Dược phẩm DEF"]
}
```

---

## 4. Dispensing APIs (API cho cấp phát thuốc - Invoice Integration)

Các API này được thiết kế để tích hợp với module Invoice khi cấp phát thuốc cho bệnh nhân. Sử dụng logic **FEFO (First Expiry First Out)** - xuất thuốc có hạn dùng gần nhất trước.

### 4.1 Get Available Quantity

**Endpoint:** `GET /api/inventory/{medicineId}/available`

**Description:** Lấy số lượng thuốc khả dụng (loại trừ thuốc hết hạn và sắp hết hạn).

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| medicineId | Integer | Yes | Medicine ID |

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| minMonthsBeforeExpiry | Integer | No | 3 | Số tháng tối thiểu trước khi hết hạn |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": 150
}
```

---

### 4.2 Check Medicine Availability

**Endpoint:** `GET /api/inventory/{medicineId}/check-availability`

**Description:** Kiểm tra xem thuốc có đủ số lượng yêu cầu không.

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| medicineId | Integer | Yes | Medicine ID |

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| requiredQuantity | Integer | Yes | - | Số lượng cần kiểm tra |
| minMonthsBeforeExpiry | Integer | No | 3 | Số tháng tối thiểu trước khi hết hạn |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": true
}
```

---

### 4.3 Check Bulk Availability

**Endpoint:** `POST /api/inventory/check-bulk-availability`

**Description:** Kiểm tra khả dụng cho nhiều thuốc cùng lúc.

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| minMonthsBeforeExpiry | Integer | No | 3 | Số tháng tối thiểu trước khi hết hạn |

**Request Body:**

```json
{
  "1": 50,
  "2": 30,
  "5": 100
}
```

_Map: medicineId → required quantity_

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {
    "1": true,
    "2": true,
    "5": false
  }
}
```

_Map: medicineId → availability (true/false)_

---

### 4.4 Deduct Inventory (Trừ kho)

**Endpoint:** `POST /api/inventory/{medicineId}/deduct`

**Description:** Trừ số lượng thuốc khi cấp phát. Sử dụng logic FEFO - trừ từ lô có hạn dùng gần nhất trước.

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| medicineId | Integer | Yes | Medicine ID |

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| quantity | Integer | Yes | - | Số lượng cần trừ |
| minMonthsBeforeExpiry | Integer | No | 3 | Số tháng tối thiểu trước khi hết hạn |

**Response (Success):**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": "Đã trừ số lượng thuốc thành công"
}
```

**Response (Error - Insufficient Stock):**

```json
{
  "statusCode": 400,
  "message": "Không đủ số lượng thuốc: Tên thuốc. Cần: 100, Còn: 50",
  "data": null
}
```

---

### 4.5 Deduct Bulk Inventory (Trừ kho hàng loạt)

**Endpoint:** `POST /api/inventory/deduct-bulk`

**Description:** Trừ số lượng nhiều thuốc cùng lúc. Đây là giao dịch **All-or-Nothing** - nếu bất kỳ thuốc nào không đủ số lượng, không có gì bị trừ.

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| minMonthsBeforeExpiry | Integer | No | 3 | Số tháng tối thiểu trước khi hết hạn |

**Request Body:**

```json
{
  "1": 50,
  "2": 30,
  "5": 100
}
```

_Map: medicineId → quantity to deduct_

**Response (Success):**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": "Đã trừ số lượng thuốc thành công"
}
```

**Response (Error):**

```json
{
  "statusCode": 400,
  "message": "Không đủ số lượng thuốc: Tên thuốc A (cần: 100, còn: 50), Tên thuốc B (cần: 50, còn: 20)",
  "data": null
}
```

---

### 4.6 Restore Inventory (Khôi phục kho)

**Endpoint:** `POST /api/inventory/{medicineId}/restore`

**Description:** Khôi phục số lượng thuốc khi hủy đơn hoặc trả thuốc. Cộng vào lô nhập gần nhất.

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| medicineId | Integer | Yes | Medicine ID |

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| quantity | Integer | Yes | - | Số lượng cần khôi phục |

**Response:**

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": "Đã khôi phục số lượng thuốc thành công"
}
```

---

## 5. Usage Guide for Invoice Integration

### Quy trình cấp phát thuốc (Dispensing Workflow):

```
1. Invoice thêm thuốc vào đơn
   ↓
2. Gọi GET /api/inventory/{id}/check-availability để kiểm tra
   ↓
3. Nếu TRUE → cho phép thêm vào đơn
   Nếu FALSE → thông báo không đủ thuốc
   ↓
4. Khi xác nhận Invoice (confirm/pay)
   ↓
5. Gọi POST /api/inventory/deduct-bulk với danh sách thuốc
   ↓
6. Nếu thành công → cập nhật trạng thái Invoice
   Nếu thất bại → rollback, thông báo lỗi
```

### Quy trình hủy/hoàn thuốc (Cancel/Return Workflow):

```
1. Invoice bị hủy hoặc hoàn thuốc
   ↓
2. Gọi POST /api/inventory/{id}/restore cho từng thuốc
   ↓
3. Cập nhật trạng thái Invoice
```

### Code Example (trong InvoiceService):

```java
// Kiểm tra trước khi thanh toán
Map<Integer, Integer> medicineQuantities = new HashMap<>();
for (InvoiceDetail detail : invoice.getDetails()) {
    if (detail.getMedicineId() != null) {
        medicineQuantities.merge(detail.getMedicineId(), detail.getQuantity(), Integer::sum);
    }
}

Map<Integer, Boolean> availability = inventoryService.checkBulkAvailability(medicineQuantities, 3);

// Kiểm tra có thuốc nào không đủ không
for (Map.Entry<Integer, Boolean> entry : availability.entrySet()) {
    if (!entry.getValue()) {
        throw new BadRequestException("Thuốc ID " + entry.getKey() + " không đủ số lượng");
    }
}

// Nếu đủ thì trừ kho
inventoryService.deductBulkInventory(medicineQuantities, 3);
```

---

## 6. Data Flow Summary

### When Creating Import:

1. Insert into `medicine_import` table
2. For each detail:
   - Insert into `import_detail` table
   - Insert into `medicine_inventory` table (adds stock)
3. Calculate and update `total_quantity` and `total_value` in `medicine_import`

### When Updating Import:

1. Delete old `medicine_inventory` records for this import
2. Delete old `import_detail` records for this import
3. Update `medicine_import` info
4. Re-create new `import_detail` and `medicine_inventory` records

### When Deleting Import:

1. Delete `medicine_inventory` records (reduces stock)
2. Delete `import_detail` records
3. Delete `medicine_import` record

### When Dispensing Medicine (FEFO):

1. Query batches sorted by expiry date (nearest first)
2. Filter out expired and near-expiry batches
3. Deduct from each batch until required quantity is met
4. Update `medicine_inventory.quantity_in_stock` for each affected batch

### When Restoring Inventory:

1. Find the most recent batch for the medicine
2. Add quantity back to that batch's `quantity_in_stock`

---

## 7. Project Structure

```
src/main/java/com/example/ooad/
├── controller/
│   └── inventory/
│       ├── InventoryController.java      # Inventory + Dispensing APIs
│       └── MedicineImportController.java # Import CRUD APIs
├── service/
│   └── inventory/
│       ├── interfaces/
│       │   ├── InventoryService.java     # Interface with FEFO methods
│       │   └── MedicineImportService.java
│       └── implementation/
│           ├── InventoryServiceImplementation.java  # FEFO logic implementation
│           └── MedicineImportServiceImplementation.java
├── repository/
│   ├── MedicineInventoryRepository.java  # With FEFO queries
│   ├── MedicineImportRepository.java
│   └── ImportDetailRepository.java
├── dto/
│   ├── request/
│   │   └── inventory/
│   │       ├── InventoryFilterRequest.java
│   │       ├── MedicineImportFilterRequest.java
│   │       ├── MedicineImportRequest.java
│   │       └── ImportDetailRequest.java
│   └── response/
│       └── inventory/
│           ├── InventoryItemResponse.java
│           ├── InventoryBatchResponse.java
│           ├── InventoryDetailResponse.java
│           ├── MedicineImportListResponse.java
│           ├── MedicineImportDetailResponse.java
│           ├── ImportDetailResponse.java
│           └── MedicineSelectionResponse.java
└── mapper/
    └── InventoryMapper.java
```

---

## 8. API Summary Table

| Method | Endpoint                                 | Description                     | Category   |
| ------ | ---------------------------------------- | ------------------------------- | ---------- |
| GET    | `/api/inventory`                         | Paginated inventory list        | Inventory  |
| GET    | `/api/inventory/all`                     | All inventory items             | Inventory  |
| GET    | `/api/inventory/{id}`                    | Inventory detail by medicine ID | Inventory  |
| GET    | `/api/inventory/{id}/available`          | Available quantity              | Dispensing |
| GET    | `/api/inventory/{id}/check-availability` | Check single availability       | Dispensing |
| POST   | `/api/inventory/check-bulk-availability` | Check bulk availability         | Dispensing |
| POST   | `/api/inventory/{id}/deduct`             | Deduct single medicine (FEFO)   | Dispensing |
| POST   | `/api/inventory/deduct-bulk`             | Deduct multiple medicines       | Dispensing |
| POST   | `/api/inventory/{id}/restore`            | Restore inventory               | Dispensing |
| GET    | `/api/imports`                           | Paginated import list           | Import     |
| GET    | `/api/imports/all`                       | All imports                     | Import     |
| GET    | `/api/imports/{id}`                      | Import detail                   | Import     |
| POST   | `/api/imports`                           | Create import                   | Import     |
| PUT    | `/api/imports/{id}`                      | Update import                   | Import     |
| DELETE | `/api/imports/{id}`                      | Delete import                   | Import     |
| GET    | `/api/medicines/selection`               | Medicines for dropdown          | Supporting |
| GET    | `/api/suppliers`                         | Suppliers for dropdown          | Supporting |
