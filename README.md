Spring MVC Example App
======================

### Prerequisites
- Install Mongo

Try it out
----------

`com.example.demo.DemoApplication`

API
---

`POST /api/catalog/renew` fetch catalog (categories recursively, product short and long descriptions) and store it in local mongo
`GET /api/catalog` returns catalog with categories (without products)
`GET /api/categories/{id}` returns category description  
`GET /api/categories/{id}/categories` returns sub-categories of specified category  
`GET /api/categories/{id}/products` returns products (short description) of specified category  
`GET /api/products/{id}` returns long product description 