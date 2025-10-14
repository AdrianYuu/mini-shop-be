# Product Category API Specification

---

## Get Product Categories
- Method: GET
- URL: /product-categories
- Headers:
    - None
- Cookies:
    - token (admin)

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
      "name": "Furniture",
      "created_at": "2025-10-13T09:00:00",
      "updated_at": "2025-10-13T09:00:00"
    },
    {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "name": "Furniture",
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

### Response (Fail, 403)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Forbidden"
      ]
    }
  ]
}
```

---

## Get Product Category
- Method: GET
- URL: /product-categories/{product_category_id}
- Headers:
    - None
- Cookies:
    - token (admin)

### Payload
```text
None
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Furniture",
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

### Response (Fail, 403)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Forbidden"
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
        "Product category not found"
      ]
    }
  ]
}
```

---

## Create Product Category
- Method: POST
- URL: /product-categories
- Headers:
    - Content-Type: application/json
- Cookies:
    - token (admin)

### Payload
```json
{
  "name": "Furniture"
}
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Furniture",
    "created_at": "2025-10-13T09:00:00",
    "updated_at": "2025-10-13T09:00:00"
  }
}
```

### Response (Fail, 400)
```json
{
  "errors": [
    {
      "field": "name",
      "messages": [
        "Name is required"
      ]
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

### Response (Fail, 403)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Forbidden"
      ]
    }
  ]
}
```

### Response (Fail, 409)
```json
{
  "errors": [
    {
      "field": "name",
      "messages": [
        "Name already exists"
      ]
    }
  ]
}
```

---

## Update Product Category
- Method: PATCH
- URL: /product-categories/{product_category_id}
- Headers:
    - Content-Type: application/json
- Cookies:
    - token (admin)

### Payload
```json
{
  "name": "Furniture"
}
```

### Response (Success, 200)
```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Furniture",
    "created_at": "2025-10-13T09:00:00",
    "updated_at": "2025-10-13T09:00:00"
  }
}
```

### Response (Fail, 400)
```json
{
  "errors": [
    {
      "field": "name",
      "messages": [
        "Name is required"
      ]
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

### Response (Fail, 403)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Forbidden"
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
        "Product category not found"
      ]
    }
  ]
}
```

### Response (Fail, 409)
```json
{
  "errors": [
    {
      "field": "name",
      "messages": [
        "Name already exists"
      ]
    }
  ]
}
```

---

## Delete Product Category
- Method: DELETE
- URL: /product-categories/{product_category_id}
- Headers:
    - None
- Cookies:
    - token (admin)

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

### Response (Fail, 403)
```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Forbidden"
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
        "Product category not found"
      ]
    }
  ]
}
```