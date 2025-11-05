# Auth API Specification

---

## CSRF

- Method: GET
- URL: /auth/csrf

### Payload

```text
None
```

### Response (Success, 204)

```text
None
```

## Register

- Method: POST
- URL: /auth/register
- Headers:
    - Content-Type: application/json

### Payload

```json
{
  "name": "Adrian Yu",
  "email": "adrian.yu@gmail.com",
  "password": "adrian123"
}
```

### Response (Success, 201)

```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Adrian Yu",
    "email": "adrian.yu@gmail.com",
    "bio": null,
    "image_key": null,
    "role": "USER"
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
    },
    {
      "field": "email",
      "messages": [
        "Email is required"
      ]
    }
  ]
}
```

---

## Login

- Method: POST
- URL: /auth/login
- Headers:
    - Content-Type: application/json

### Payload

```json
{
  "email": "adrian.yu@gmail.com",
  "password": "adrian123"
}
```

### Response (Success, 200)

```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Adrian Yu",
    "email": "adrian.yu@gmail.com",
    "bio": null,
    "image_key": null,
    "role": "USER"
  }
}
```

### Response (Fail, 400)

```json
{
  "errors": [
    {
      "field": "general",
      "messages": [
        "Invalid email or password"
      ]
    }
  ]
}
```

---

## Logout

- Method: POST
- URL: /auth/logout
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