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
  "data": [
    "NCC ABC",
    "NCC XYZ",
    "Công ty Dược phẩm DEF"
  ]
}
```

---

## Data Flow Summary

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

---

## Project Structure

```
src/main/java/com/example/ooad/
├── controller/
│   └── inventory/
│       ├── InventoryController.java
│       └── MedicineImportController.java
├── service/
│   └── inventory/
│       ├── interfaces/
│       │   ├── InventoryService.java
│       │   └── MedicineImportService.java
│       └── implementation/
│           ├── InventoryServiceImplementation.java
│           └── MedicineImportServiceImplementation.java
├── repository/
│   ├── MedicineInventoryRepository.java (updated)
│   ├── MedicineImportRepository.java (new)
│   └── ImportDetailRepository.java (new)
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
