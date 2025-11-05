# User API Specification

---

## Me

- Method: GET
- URL: /auth/me
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
    "name": "Adrian Yu",
    "email": "adrian.yu@gmail.com",
    "bio": null,
    "image_key": null,
    "role": "USER"
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

## Update User Information

- Method: PATCH
- URL: /users/me
- Headers:
    - Content-Type: multipart/form-data
- Cookies:
    - token

### Payload

```json
{
  "name": "Adrian Yu",
  "bio": "Software Engineer",
  "image": "foo.png"
}
```

### Response (Success, 200)

```json
{
  "data": {
    "id": "acde070d-8c4c-4f0d-9d8a-162843c10333",
    "name": "Adrian Yu",
    "email": "adrian.yu@gmail.com",
    "bio": "Software Engineer",
    "image_key": "foo/bar.png"
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

---

## Update User Password

- Method: PATCH
- URL: /users/me/password
- Headers:
    - Content-Type: application/json
- Cookies:
    - token

### Payload

```json
{
  "new_password": "adrian123",
  "current_password": "adrian"
}
```

### Response (Success, 204)

```text
None
```

### Response (Fail, 400)

```json
{
  "errors": [
    {
      "field": "new_password",
      "messages": [
        "New password is required"
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