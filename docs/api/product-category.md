# Product Category API Specification

---

## Paginate

- Method: GET
- URL: /product-categories
- Cookies:
    - token (admin)
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

## Get

- Method: GET
- URL: /product-categories/{id}
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

## Create

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

## Update

- Method: PATCH
- URL: /product-categories/{id}
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

## Delete

- Method: DELETE
- URL: /product-categories/{id}
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