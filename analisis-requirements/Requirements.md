# Analisis de requerimientos

## Problematica

El sistema de gestión de inventarios de productos en una pequeña tiendaa de barrio es un desafío para optimizar las ventas y controlar los niveles de stock. Las tiendas necesitan herramientas para gestionar el inventario, realizar ventas, agregar productos al stock, y mantener un control adecuado de los productos más vendidos y aquellos que se encuentran por debajo del umbral de stock. Además, el control de precios, proveedores, fechas de adquisición y vencimiento es importante para evitar pérdidas y garantizar una buena rotación de productos.

## Objetivo

El objetivo principal de este proyecto es implementar un sistema de microservicios que permita gestionar los productos de una tienda de barrio. Este sistema debe facilitar la realización de ventas, el control del stock de productos, la actualización constante del inventario, y la posibilidad de realizar consultas sobre los productos más vendidos y aquellos con un stock bajo. También, debe permitir el cálculo de ganancias por ventas dentro de un rango de fechas y la correcta asignación de proveedores a los productos.

## Requerimientos, historias de usuario

Los requerimientos planteados en la problematica se obtuvieron las siguientes Historias de usuario:


| **ID** | **Historia de Usuario**                                                                                                                                                                                                                                             |
| ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1      | Como usuario administrador, quiero registrar un nuevo producto con su código único <br />generado internamente, de tal forma que cada producto tenga una identificación única y <br />estandarizada.                                                            |
| 2      | Como usuario administrador, quiero ingresar información detallada del producto (nombre,<br /> proveedor, costo, precio de venta, stock, fechas de vencimiento y adquisición), de tal forma<br /> que pueda gestionar adecuadamente la información de inventario. |
| 3      | Como usuario administrador, quiero poder actualizar la información de un producto existente, <br />de tal forma que se mantenga la información actualizada en caso de cambios en el proveedor <br />o costos.                                                     |
| 4      | Como usuario administrador, quiero agregar más unidades a un producto existente sin modificar <br />los datos de las unidades previas, de tal forma que se mantenga un historial de adquisiciones <br />y vencimientos.                                            |
| 5      | Como usuario administrador, quiero registrar la fecha de adquisición y vencimiento de cada <br />stock de productos ingresados, de tal forma que pueda gestionar el inventario por fechas de<br /> expiración.                                                    |
| 6      | Como usuario administrador, quiero visualizar el stock de cada producto, de tal forma <br />que pueda conocer cuántas unidades están disponibles para la venta.                                                                                                   |
| 7      | Como usuario administrador, quiero recibir una alerta cuando un producto tenga un stock <br />por debajo del umbral definido, de tal forma que pueda reponer el inventario antes de <br />quedarse sin unidades.                                                    |
| 8      | Como usuario vendedor, quiero registrar una venta de productos del stock, de tal forma<br /> que se actualice la cantidad disponible en el inventario.                                                                                                              |
| 9      | Como usuario vendedor, quiero que el sistema no me permita vender un producto a <br />un precio menor o mayor al 75% del costo de compra, de tal forma que se respete la<br /> política de precios establecida.                                                    |
| 10     | Como usuario vendedor, quiero que al vender un producto, se descuenten primero<br /> las unidades más antiguas, de tal forma que se eviten pérdidas por productos vencidos.                                                                                       |
| 11     | Como usuario administrador, quiero consultar los X productos más vendidos, de tal <br />forma que pueda identificar cuáles son los más populares y ajustar el stock en consecuencia.                                                                             |
| 12     | Como usuario administrador, quiero consultar los productos con stock por debajo<br /> del umbral definido, de tal forma que pueda planificar la reposición de inventario.                                                                                          |
| 13     | Como usuario administrador, quiero consultar la ganancia obtenida en un rango de fechas, <br />de tal forma que pueda analizar el rendimiento financiero del negocio.                                                                                               |
| 14     | Como usuario administrador, quiero obtener un reporte de todas las ventas realizadas <br />en un periodo de tiempo, de tal forma que pueda analizar el comportamiento del negocio.                                                                                  |
| 15     | Como usuario administrador, quiero recibir mensajes de error claros cuando ingrese datos<br /> incorrectos o incompletos indicando el error, de tal forma que pueda corregir la información<br /> antes de enviarla.                                               |
| 16     | Como usuario vendedor, quiero recibir un mensaje de error si intento vender un producto<br /> que no tiene stock disponible, de tal forma que evite errores en la gestión de inventario.                                                                           |
