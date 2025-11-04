# Order API Specification

---

## Get Orders
- Method: GET
- URL: /orders
- Headers:
    - Content-Type: application/json
- Cookies:
    - token
- Query Parameters:
  - page (start from 1)
  - size (default is 10)

### Payload
```text
None
```

### Response (Success, 200)
```json
{
  "data": [
    {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "total_price": "210.54",
      "status": "finalized",
      "created_at": "2025-10-13T09:00:00"
    },
    {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "total_price": "210.54",
      "status": "finalized",
      "created_at": "2025-10-13T09:00:00"
    }
  ],
  "pagination": {
    "page": 1,
    "size": 10,
    "total_pages": 10,
    "total_elements": 100
  }
}
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

---

## Get Order
- Method: GET
- URL: /orders/{order_id}
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload
```text
None
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "total_price": "210.54",
    "status": "finalized",
    "created_at": "2025-10-13T09:00:00"
  }
}
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

### Response (Fail, 404)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Order not found"
      ]
    }
  ]
}
```

---

## Get Order Items
- Method: GET
- URL: /orders/{order_id}/items
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload
```text
None
```

### Response (Success, 200)
```json
{
  "data": [
    {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "quantity": 5,
      "product": {
        "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
        "name": "Table",
        "price": 123.45,
        "stock": 10,
        "image_url": "foo/bar.png",
        "category": {
          "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
          "name": "Furniture"
        }
      },
      "created_at": "2025-10-13T09:00:00",
      "updated_at": "2025-10-13T09:00:00"
    },
    {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "quantity": 5,
      "product": {
        "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
        "name": "Table",
        "price": 123.45,
        "stock": 10,
        "image_url": "foo/bar.png",
        "category": {
          "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
          "name": "Furniture"
        }
      },
      "created_at": "2025-10-13T09:00:00",
      "updated_at": "2025-10-13T09:00:00"
    }
  ]
}
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

### Response (Fail, 404)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Order not found"
      ]
    }
  ]
}
```

---

## Create Order Item
- Method: POST
- URL: /orders/items
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload
```json
{
  "quantity": 5,
  "product_id": "acde070d-8c4c-4f0d-9d8a-162843c10333"
}
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "quantity": 5,
    "product": {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "name": "Table",
      "price": 123.45,
      "stock": 10,
      "image_url": "foo/bar.png",
      "category": {
        "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
        "name": "Furniture"
      }
    },
    "created_at": "2025-10-13T09:00:00",
    "updated_at": "2025-10-13T09:00:00"
  }
}
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

### Response (Fail, 404)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Order not found"
      ]
    }
  ]
}
```

---

## Update Order Item
- Method: PATCH
- URL: /orders/items/{order_item_id}
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload
```json
{
  "quantity": 5
}
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "quantity": 5,
    "product": {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "name": "Table",
      "price": 123.45,
      "stock": 10,
      "image_url": "foo/bar.png",
      "category": {
        "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
        "name": "Furniture"
      }
    },
    "created_at": "2025-10-13T09:00:00",
    "updated_at": "2025-10-13T09:00:00"
  }
}
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

### Response (Fail, 404)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Order not found"
      ]
    }
  ]
}
```

## Delete Order Item
- Method: DELETE
- URL: /orders/items/{order_item_id}
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload
```text
None
```

### Response (Success, 204)
```text
None
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

### Response (Fail, 404)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Order item not found"
      ]
    }
  ]
}
```

## Finalize Order
- Method: POST
- URL: /orders/finalize
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload
```text
None
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "total_price": "210.54",
    "status": "finalized",
    "created_at": "2025-10-13T09:00:00"
  }
}
```

### Response (Fail, 401)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Unauthorized"
      ]
    }
  ]
}
```

### Response (Fail, 404)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Order item not found"
      ]
    }
  ]
}
```