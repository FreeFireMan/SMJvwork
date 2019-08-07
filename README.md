Spring MVC Example App
======================

### Prerequisites
- Install Mongo


Try it out
----------

`com.example.demo.DemoApplication`

```bash
# query the catalog
curl http://localhost:8080/api/catalog/
# should return 404 as there is nothing there yet

# request catalog upgrade (connect to agent, doenload catalog and cache it)
curl -D "" http://localhost:8080/api/catalog/upgrade

# query the catalog
curl http://localhost:8080/api/catalog/
# at this point it should give you the catalog
```