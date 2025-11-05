# Product API Specification

---

## Search

- Method: GET
- URL: /products
- Cookies:
    - token
- Query Parameters:
    - name
    - category
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
      "name": "Table",
      "price": 123.45,
      "stock": 10,
      "image_url": "foo/bar.png",
      "category": {
        "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
        "name": "Furniture"
      },
      "created_at": "2025-10-13T09:00:00",
      "updated_at": "2025-10-13T09:00:00"
    },
    {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10334",
      "name": "Chair",
      "price": 123.45,
      "stock": 10,
      "image_url": "foo/bar.png",
      "category": {
        "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
        "name": "Furniture"
      },
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

---

## Get

- Method: GET
- URL: /products/{id}
- Cookies:
    - token

### Payload

```Text
None
```

### Response (Success, 200)

```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Table",
    "price": 123.45,
    "stock": 10,
    "image_key": "foo/bar.png",
    "category": {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "name": "Furniture"
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
        "Product not found"
      ]
    }
  ]
}
```

---

## Create

- Method: POST
- URL: /products
- Headers:
    - Content-Type: multipart/form-data
- Cookies:
    - token (must be an admin)

### Payload

```json
{
  "name": "Table",
  "price": 123.45,
  "stock": 10,
  "image": "foo.png",
  "category_id": "acde070d-8c4c-4f0d-9d8a-162843c10333"
}
```

### Response (Success, 201)

```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Table",
    "price": 123.45,
    "stock": 10,
    "image_key": "foo/bar.png",
    "category": {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "name": "Furniture"
    },
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

---

## Update Product

- Method: PATCH
- URL: /products/{id}
- Headers:
    - Content-Type: multipart/form-data
- Cookies:
    - token (must be an admin)

### Payload

```json
{
  "name": "Table",
  "price": 123.45,
  "stock": 10,
  "image": "foo.png",
  "category_id": "acde070d-8c4c-4f0d-9d8a-162843c10333"
}
```

### Response (Success, 200)

```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Table",
    "price": 123.45,
    "stock": 10,
    "image_key": "foo/bar.png",
    "category": {
      "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
      "name": "Furniture"
    },
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
        "Product not found"
      ]
    }
  ]
}
```

---

## Delete Product

- Method: DELETE
- URL: /products/{id}
- Cookies:
    - token (must be an admin)

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
        "Product not found"
      ]
    }
  ]
}
```