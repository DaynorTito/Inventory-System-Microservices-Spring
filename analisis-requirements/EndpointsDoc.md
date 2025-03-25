
# Endpoints del proyecto

Se describen los siguientes enpoints con sus respectivos cuerpos de solicitud si aplica:

## Brand

---

#### POST http://localhost:8090/api/v1/product-server/brand

Crea una marca con el campo de nombre requerido, el campo descripcion es opcional
Body:

```json
{
    "name": "Samsung",
    "description": "Aparatos Samsung"
}
```
---



#### PUT http://localhost:8090/api/v1/product-server/brand/{idBrand}

Modifica el objeto con un ID el cuerpo de la solicitud necesita de un nombre o descipcion, o ambos

Body:
```json
{
    "name": "Sony"
}
```

---

#### GET http://localhost:8090/api/v1/product-server/brand

Obteiene el listado de todas las marcas en un array

Body: None

---


#### GET http://localhost:8090/api/v1/product-server/brand/{ID}

Obteiene la marca con ID especificado

Body: None

---

#### DETELE http://localhost:8090/api/v1/product-server/brand/{ID}

Elimina la marca con id especificado

Body: None

---

## Category

---

#### POST http://localhost:8090/api/v1/product-server/category

Crea una nueva categoria con el campo de name obligatorio, descripcion es opcional

Body:

```json
{
    "name": "Electronica",
    "description": "Dispositivos electronicos"
}
```
---


#### PUT http://localhost:8090/api/v1/product-server/category/{ID}

Actualiza informacion de la categoria, campos opcionales name y description o ambos

Body:

```json
{
    "description": "Dispositivos electronicos"
}
```

---

#### GET http://localhost:8090/api/v1/product-server/category/{ID}

Obtiene informacion de la categoria, especificada con ID

Body: None

---

#### GET http://localhost:8090/api/v1/product-server/category

Obtiene informacion de todas las categorias

Body: None

---

#### DELETE http://localhost:8090/api/v1/product-server/category

Elimina informacion de la ctagoria con ID especificado

Body: None

---

## Product

---

#### POST http://localhost:8090/api/v1/product-server/product

Crea informacion de un nuevo producto, los campos de nombre, precio, son obligatorios, los campos de descripcion marca y categoria son opcionales, y se deben ingresar los nombres de la marca y la categoria creada en los anteriores pasos

Body:

```json
{
    "name": "Radio",
    "description": "Radio Am y Fm",
    "salePrice": 1000.21,
    "brand": "LG",
    "category": "Electronica"
}
```
---

#### GET http://localhost:8090/api/v1/product-server/product

Obtiene todo el listado de los productos en un array con informacion del producto incluyendo informacion de marca y categoria si este cuenta con mencionada informacion

Body: None

---

#### GET http://localhost:8090/api/v1/product-server/product/{ID}

Obtiene informacion de un producto con ID especificado con informacion del producto incluyendo informacion de marca y categoria si este cuenta con mencionada informacion

Body: None

---

#### PUT http://localhost:8090/api/v1/product-server/product/{ID}

Crea informacion de un nuevo producto, los campos de nombre, precio, son obligatorios, los campos de descripcion marca y categoria son opcionales, y se deben ingresar los nombres de la marca y la categoria creada en los anteriores pasos

Body:

```json
{
    "name": "Radio Digital",
    "description": "Radio Am y Fm con funcion de conexion Bluethoot",
    "salePrice": 1500.00
}
```
---

#### DELETE http://localhost:8090/api/v1/product-server/product/{ID}

Elimina informacion de un producto con un ID definido

---
#### GET http://localhost:8090/api/v1/product-server/product/category-brand-price?minPrice=0&maxPrice=10000&brand=samsung&category=electronica
#### GET http://localhost:8090/api/v1/product-server/product/category-brand-price?brand=samsung&category=electronica

Params:

```text
minPrice: precio minimo para filtrar
maxPrice: cota superior del rango de precio de busqueda
category: una categoria valida registrada
brand: nombre de una marca registrada
```
Obtiene todos los productos dada una categoria o marca determiada, ademas de filtrar con un rango de precios
Estos query params son opcionales

---

#### GET http://localhost:8090/api/v1/product-server/product/search?word=television

Obtiene todos los productos que contengan una palabra especificada, se mostraran todos los productos que contengan esta palabra clave dentro del nombre del producto o en su descripcion

Params:

```text
word: palabra por la cual buscar
```
---

## Provider

---
#### POST http://localhost:8090/api/v1/purchase-server/provider

Crea un proveedor de productos para registrar compras, los campos de nombre direccion, telefono son requeridos

Body:

```json
{
    "name": "Electro Devices",
    "address": "Av america",
    "phone": "123234",
    "email": "electro@correo.com"
}
```
---

#### GET http://localhost:8090/api/v1/purchase-server/provider
#### GET http://localhost:8090/api/v1/purchase-server/provider/{id}

Obtiene todos los proveedores registrados

Y obtiene un proveedor registrado con un id especifico, este debe existir

---

#### PUT http://localhost:8090/api/v1/purchase-server/provider/{id}
Actualiza informacion de un proveedor con id especificado, los campos son opcionales, si se quiere cambiar informacion solo del nombre el cuerpo de solicitud deberia ser asi:


Body:

```json
{
    "name": "Provider name changed"
}
```

---

#### DELETE http://localhost:8090/api/v1/purchase-server/provider/{id}
Elimina un proveedor con id registrado, este no debe estar registrado en una compra, y el id debe ser valido de un proveedor que se haya registrado anteriormente

---

## Purchases

---

#### POST http://localhost:8090/api/v1/purchase-server/purchase/existing

Crea una compra de productos ya registrados en el catalogo, si se quieren agregar stocks nuevos a los que ya existen registrados

Body:
```json
{
"provider": "{{nameProvider}}",
"items": [
{"productId": "{{codProd2}}", "quantity": 10, "unitPrice": 180, "expirationDate": "2027-10-10"}

    ]
}
```

El proveedor debe ser el nombre de un proveedor ya existente y el campo es requerido, la lista de items es requerido dentro de esta
se encuentra el id del producto que se compra su precio unitario de compra y la cantidad, el dato de fecha de expiracion es opcional solo
para productos no perecederos

---

#### POST http://localhost:8090/api/v1/purchase-server/purchase/new

Se crea una compra de un nuevo producto no registrado anteriormente, especificando el proveedor
dentro de producto se especifica informacion propia del producto vista en el enpoind POST de producto
ademas de la cantidad y el precio unitario de la compra

Body:

```json
{
    "provider": "{{nameProvider2}}",
    "items": [
        {
            "product": {
                "name": "Licuadora",
                "description": "Licuadora marca Oster",
                "salePrice": 1000.10,
                "brand": "Samsung",
                "category": "Electronica"
            },
            "quantity": 100,
            "unitPrice": 290
        }
    ]
}

```
---

#### GET http://localhost:8090/api/v1/purchase-server/purchase

Obtiene el listado de todas las compras hechas

---

#### GET http://localhost:8090/api/v1/purchase-server/purchase/{ID}

Obtiene informacion de una compra hecha con id en especifico

---

## Stocks

---

#### POST http://localhost:8090/api/v1/inventory-server/stock

Se crea un stock de un producto existente con un proveedor existente, indicando la cantidad y el
precio unitario de estos, todos estos campos son requeridos

Body:
```json
{
    "quantity": 100,
    "purchaseUnitCost": 120,
    "providerId": "{{idProvider2}}",
    "productId": "{{codProd2}}",
    "purchaseDate": "2025-03-26"
}
```

---

#### GET http://localhost:8090/api/v1/inventory-server/stock

Obtiene todo el listado de los stocks registrados

---

#### GET http://localhost:8090/api/v1/inventory-server/stock/{ID}

Obtiene informaicon detallada de un stock en especifico, por el ID

---

#### PUT http://localhost:8090/api/v1/inventory-server/stock

Actualiza informacion de un stock realizado, especificando el nombre de los campos
a actualizaron con sus valores nuevos, por ejemplo si se desea actualizar la cantidad
o el precio unitario de compra:

Body:

```json
{
    "quantity": 90,
    "purchaseUnitCost": 100
}
```
---

#### GET http://localhost:8090/api/v1/inventory-server/stock/total?productId={{codProd}}

Obtiene informacion de cantidades de stock de un producto en especifico

Params:
````text
productId: codigo de un producto existente
````

Response ej:

```json
{
    "Stock caducado: ": "0",
    "Stock valido: ": "100",
    "Nombre de producto": "Auriculares Inalámbricos",
    "Total: ": "100"
}
```
---

#### GET http://localhost:8090/api/v1/inventory-server/stock/oldest?productId={{codProd}}

Obtiene informacion del stock mas antigui de un producto es especifico

Params:
```text
productId: codigo de un producto existente
```

---

#### GET http://localhost:8090/api/v1/inventory-server/stock/expiring?days={{numDays}}

Obtiene todos los stocks que estan por caducar a x dias

Params:
```text
days: cantidad de dias faltantes de caducidad
```

---

#### GET http://localhost:8090/api/v1/inventory-server/stock/between-dates?startDate=2025-01-01&endDate=2025-04-01

Obtiene todos los stocks que se registraron en un rango de fechas determinado

Params:
```text
startDate: umbral o cantidad por la cual filtrar
endDate: nombre de la categoria para filtrar
```

---
#### GET http://localhost:8090/api/v1/inventory-server/stock/status

Obtiene un listado rapido de todos los productos con el cod de producto e indicando aquellos 
que estan sin stock, con stcok o que ya estan por quedarse sin stock

---

## Kardex

---
#### POST http://localhost:8090/api/v1/inventory-server/kardex

Registra un movimiento en kardez, todos los campos son requeridos, el tipo de movimiento indica si es un ingreo o egreso de stocks de productos

Body:

```json
{
    "typeMovement": "OUTCOME",
    "quantity": 10,
    "productId": "{{codProd2}}",
    "unitPrice": 10
}
```

---
#### PUT http://localhost:8090/api/v1/inventory-server/kardex/{ID}

Actualiza un campo de un registro de Kardex en especifico

```json
{
    "quantity": 200
}
```
---

#### GET http://localhost:8090/api/v1/inventory-server/kardex/product-history/{{codProd2}}

Obtiene el listado de movimientos de kardex de un producto en especifico

---

#### GET http://localhost:8090/api/v1/inventory-server/kardex/inventory-movements?startDate=2025-01-01&endDate=2025-05-01

Obtiene el listado de movimientos de kardex de en un rango de fechas especificado

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
```

---
#### GET http://localhost:8090/api/v1/inventory-server/kardex/inventory-movements/{{codProd2}}?startDate=2025-01-01&endDate=2025-05-01

Obtiene el listado de movimientos de kardex de en un rango de fechas especificado de un producto

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
```

---
#### GET http://localhost:8090/api/v1/inventory-server/kardex/most-sold-products?limit=1&startDate=2025-01-01&endDate=2025-05-01

Obtiene el listado de productos mas vendidos en un rango de fechas especificado, se puede especificar la cantidad
de productos que se desea mostrar

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
limit: cantidad de productos mas vendidos a mostrar
```

---
#### GET http://localhost:8090/api/v1/inventory-server/kardex/earnings-report?startDate=2025-01-01&endDate=2025-05-01

Muestra un informe detallado de las ganancias en un rango de fechas, ganancia neta, gastos totales, los productos mas vendidos
informacion sobre el total de ventas hechas por categorias de productos y el total de productos que se venideron

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
```

---

#### DELETE http://localhost:8090/api/v1/inventory-server/kardex/{ID}

Elimina un registro de kardex especificado por su ID

---

## Sales

---

#### POST http://localhost:8090/api/v1/sale-server/sale

Crea una venta, con informacion del cliente, del metodo de pago, y los detalles de venta
todos estos campos son requeridos, dentro de detalles de compra se tiene informacion del producto
la cantidad que se esta comprando y el precio unitario que es opcional ya que puede usar el por defecto registrado al crear el producto
este precio no debe ser ni mayor ni menor al 75% de precio de compra del mismo, de igual forma se puede definir un
descuento el cual no debe ser mayor al unit price, por defecto el descuento es 0


Body:

```json
{
    "customerName": "Juan Perez",
    "paymentMethod": "Efectivo",
    "saleDetails": [
         {"productId": "{{codProd2}}", "quantity": 20, "unitPrice": 100, "discount":  10}
    ]
}
```

---

#### GET http://localhost:8090/api/v1/sale-server/sale/{ID}

Se obtiene informacion de una venta realizada, con un ID especificado

---

#### GET http://localhost:8090/api/v1/sale-server/sale/by-dates?startDate=2025-01-20&endDate=2025-04-20

Se obtiene informacion de las ventas realizadas, en un rango de fechas especificado

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
```

---

#### GET http://localhost:8090/api/v1/sale-server/sale/by-product/{{codProd2}}

Se obtiene informacion de ventas realizadas de un producto especifico

---
#### GET http://localhost:8090/api/v1/sale-server/by-product/{{codProd2}}/dates?startDate=2025-02-11&endDate=2025-04-11

Se obtiene informacion de ventas realizadas de un producto especifico, en un rango de fechas especifico

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
```
---
#### GET http://localhost:8090/api/v1/sale-server/by-customer?customerName=Juan Perez

Se obtiene informacion de ventas realizadas de un usuario con nombre especificado

---

#### GET http://localhost:8090/api/v1/sale-server/by-customer/dates?startDate=2025-01-01&endDate=2025-04-01&customerName=Juan Perez

Se obtiene informacion de ventas realizadas de un usuario con nombre especificado en un rango de fechas

Params:
```text
startDate: fecha de inicio para mostrar los registros
endDate: fecha tope para listar los registros en el rango
```

---

#### GET http://localhost:8090/api/v1/sale-server/by-price/greater-than?price=10000

Se obtiene informacion de ventas realizadas con un precio mayor a un especificado

Params:
```text
price: precio especificado
```

---

#### DELETE http://localhost:8090/api/v1/sale-server/{ID}

Se elimina un regsitro de venta, con un ID especificado

---
