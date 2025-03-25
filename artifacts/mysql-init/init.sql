-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: inventory_system
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoce3937d2f4mpfqrycbr0l93m` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'Marca de ropa y calzado deportivo','Nike'),(2,'Marca internacional de ropa deportiva y estilo de vida','Adidas'),(3,'Empresa de electrónica y entretenimiento','Twich'),(4,'Compañía de electrónica y tecnología','Philips'),(5,'Tecnología y hardware informático','Apple'),(6,'Alimentos comestibles, lacteos, despensa','PIL'),(7,'Alimentos de cocina, aceites','Fino'),(8,'Chocolateria, productos de chocolate bolivianos','Ceibo');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Dispositivos electrónicos y accesorios','Electrodomesticos'),(2,'Prendas de vestir y artículos de moda','Ropa'),(3,'Productos comestibles e ingredientes','Alimentos'),(4,'Bebidas y refrescos líquidos','Bebidas'),(5,'Equipamiento deportivo y artículos de fitness','Artículos Deportivos');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail_purchases`
--

DROP TABLE IF EXISTS `detail_purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_purchases` (
  `id` binary(16) NOT NULL,
  `expiration_date` date DEFAULT NULL,
  `product_id` varchar(255) DEFAULT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  `purchase_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK18ou9v95y4j1a4i9cwj3tqxru` (`purchase_id`),
  CONSTRAINT `FK18ou9v95y4j1a4i9cwj3tqxru` FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_purchases`
--

LOCK TABLES `detail_purchases` WRITE;
/*!40000 ALTER TABLE `detail_purchases` DISABLE KEYS */;
INSERT INTO `detail_purchases` VALUES (0xC3C0C979082411F0B6DC4851C5F70A21,NULL,'ELEC-001',50,650.00,0xBC66E5BD082411F0B6DC4851C5F70A21),(0xC3C0CE1F082411F0B6DC4851C5F70A21,NULL,'ELEC-002',100,180.00,0xBC66E911082411F0B6DC4851C5F70A21),(0xC3C0CF3C082411F0B6DC4851C5F70A21,NULL,'ROPA-001',75,80.00,0xBC66EA02082411F0B6DC4851C5F70A21),(0xC3C0CFDE082411F0B6DC4851C5F70A21,NULL,'ROPA-002',120,20.00,0xBC66EA02082411F0B6DC4851C5F70A21),(0xC3C0D077082411F0B6DC4851C5F70A21,'2026-03-23','ALIM-001',200,3.50,0xBC66EAF5082411F0B6DC4851C5F70A21),(0xC3C0D1B2082411F0B6DC4851C5F70A21,'2027-03-23','ALIM-002',150,8.00,0xBC66EAF5082411F0B6DC4851C5F70A21),(0xC3C0D26A082411F0B6DC4851C5F70A21,'2025-09-23','BEB-001',500,0.75,0xBC66EAF5082411F0B6DC4851C5F70A21),(0xC3C0D301082411F0B6DC4851C5F70A21,'2025-06-23','BEB-002',200,2.00,0xBC66EAF5082411F0B6DC4851C5F70A21);
/*!40000 ALTER TABLE `detail_purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kardex`
--

DROP TABLE IF EXISTS `kardex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kardex` (
  `id` binary(16) NOT NULL,
  `movement_date` date NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `total_price` decimal(38,2) NOT NULL,
  `type_movement` enum('INCOME','OUTCOME','RETURN') NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kardex`
--

LOCK TABLES `kardex` WRITE;
/*!40000 ALTER TABLE `kardex` DISABLE KEYS */;
INSERT INTO `kardex` VALUES (0xD7900E60082411F0B6DC4851C5F70A21,'2025-03-23','ELEC-001',50,32500.00,'INCOME',650.00),(0xD7901086082411F0B6DC4851C5F70A21,'2025-03-23','ELEC-002',100,18000.00,'INCOME',180.00),(0xD79010EF082411F0B6DC4851C5F70A21,'2025-03-23','ROPA-001',75,6000.00,'INCOME',80.00),(0xD7901135082411F0B6DC4851C5F70A21,'2025-03-23','ROPA-002',120,2400.00,'INCOME',20.00),(0xD7901178082411F0B6DC4851C5F70A21,'2025-03-23','ELEC-001',1,899.99,'OUTCOME',899.99),(0xD790123C082411F0B6DC4851C5F70A21,'2025-03-23','ROPA-001',3,389.97,'OUTCOME',129.99),(0xD7901283082411F0B6DC4851C5F70A21,'2025-03-23','ELEC-001',3,2699.97,'OUTCOME',899.99),(0xD79012C9082411F0B6DC4851C5F70A21,'2025-03-22','DEP-001',1,29.99,'RETURN',29.99);
/*!40000 ALTER TABLE `kardex` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `cod` varchar(36) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT '0.00',
  `name` varchar(50) NOT NULL,
  `sale_price` decimal(38,2) NOT NULL,
  `brand_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`cod`),
  KEY `FKa3a4mpsfdf4d2y6r8ra3sc8mv` (`brand_id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('ALIM-001','2025-03-23 16:22:59.000000','Tableta de chocolate negro 85% cacao',0.00,'Chocolate Premium',5.99,8,3),('ALIM-002','2025-03-23 16:22:59.000000','Aceite de oliva español de primera calidad',0.00,'Aceite de Oliva Extra Virgen',12.99,7,3),('BEB-001','2025-03-23 16:22:59.000000','Agua mineral natural de manantial',0.00,'Agua Mineral',1.25,6,4),('BEB-002','2025-03-23 16:22:59.000000','Zumo de naranja 100% natural',0.00,'Zumo Natural',3.50,6,4),('DEP-001','2025-03-23 16:22:59.000000','Balón de fútbol profesional',0.00,'Balón de Fútbol',29.99,2,5),('DEP-002','2025-03-23 16:22:59.000000','Raqueta profesional de carbono',15.00,'Raqueta de Tenis',159.99,1,5),('ELEC-001','2025-03-23 16:22:59.000000','Último modelo de smartphone con características avanzadas',0.00,'Smartphone X12',899.99,4,1),('ELEC-002','2025-03-23 16:22:59.000000','Auriculares con cancelación de ruido',10.00,'Auriculares Inalámbricos',249.99,3,1),('ROPA-001','2025-03-23 16:22:59.000000','Zapatillas para running de alto rendimiento',0.00,'Zapatillas Deportivas',129.99,1,2),('ROPA-002','2025-03-23 16:22:59.000000','Camiseta transpirable para actividades deportivas',5.00,'Camiseta Técnica',34.99,2,2);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providers`
--

DROP TABLE IF EXISTS `providers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `providers` (
  `id` binary(16) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfuh4835foq2trqy6ur286u3s0` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providers`
--

LOCK TABLES `providers` WRITE;
/*!40000 ALTER TABLE `providers` DISABLE KEYS */;
INSERT INTO `providers` VALUES (0x6B632182082411F0B6DC4851C5F70A21,0x01,'Calle Principal 123, Zona Industrial','contacto@distribuidoraglobal.com','Distribuidora Global SA','65545434'),(0x6B63236F082411F0B6DC4851C5F70A21,0x01,'Avenida Tecnológica 456, Parque de Innovación','pedidos@tecnologiasunidas.com','Tecnologías Unidas','77654566'),(0x6B6323F7082411F0B6DC4851C5F70A21,0x01,'Carretera del Valle 789, Valle Verde','ventas@alimentosfrescos.com','Alimentos Frescos SL','66577656'),(0x6B632463082411F0B6DC4851C5F70A21,0x01,'Calle del Estadio 321, Centro Atlético','info@deporteselite.com','Deportes Elite','76765545'),(0x6B6324A3082411F0B6DC4851C5F70A21,0x00,'Paseo de la Moda 654, Distrito del Diseño','mayorista@modaactual.com','Moda Actual','76656677');
/*!40000 ALTER TABLE `providers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `id` binary(16) NOT NULL,
  `adquisition_date` datetime(6) NOT NULL,
  `canceled` bit(1) DEFAULT NULL,
  `total` decimal(38,2) DEFAULT NULL,
  `provider_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbn2k5burari3lcgietiifq9ho` (`provider_id`),
  CONSTRAINT `FKbn2k5burari3lcgietiifq9ho` FOREIGN KEY (`provider_id`) REFERENCES `providers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
INSERT INTO `purchases` VALUES (0xBC66E5BD082411F0B6DC4851C5F70A21,'2025-03-23 16:23:52.000000',0x00,32500.00,0x6B63236F082411F0B6DC4851C5F70A21),(0xBC66E911082411F0B6DC4851C5F70A21,'2025-03-23 16:23:52.000000',0x00,18000.00,0x6B63236F082411F0B6DC4851C5F70A21),(0xBC66EA02082411F0B6DC4851C5F70A21,'2025-03-23 16:23:52.000000',0x00,8400.00,0x6B632463082411F0B6DC4851C5F70A21),(0xBC66EAF5082411F0B6DC4851C5F70A21,'2025-03-23 16:23:52.000000',0x00,2675.00,0x6B6323F7082411F0B6DC4851C5F70A21),(0xBC66EBD6082411F0B6DC4851C5F70A21,'2025-02-23 16:23:52.000000',0x01,5000.00,0x6B6324A3082411F0B6DC4851C5F70A21);
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sale_details`
--

DROP TABLE IF EXISTS `sale_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `subtotal` decimal(38,2) NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  `sale_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6nruj5m7ntwhc29etigqnlk0m` (`sale_id`),
  CONSTRAINT `FK6nruj5m7ntwhc29etigqnlk0m` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sale_details`
--

LOCK TABLES `sale_details` WRITE;
/*!40000 ALTER TABLE `sale_details` DISABLE KEYS */;
INSERT INTO `sale_details` VALUES (1,'ELEC-001',1,899.99,899.99,0xCA26B5A1082411F0B6DC4851C5F70A21),(2,'ROPA-002',1,49.99,49.99,0xCA26B5A1082411F0B6DC4851C5F70A21),(3,'ROPA-001',3,389.97,129.99,0xCA26BC18082411F0B6DC4851C5F70A21),(4,'ALIM-001',3,17.97,5.99,0xCA26BC18082411F0B6DC4851C5F70A21),(5,'ROPA-002',2,69.98,34.99,0xCA26BC18082411F0B6DC4851C5F70A21),(6,'ELEC-001',3,2699.97,899.99,0xCA26BC81082411F0B6DC4851C5F70A21),(7,'ELEC-002',1,249.99,249.99,0xCA26BC81082411F0B6DC4851C5F70A21),(8,'BEB-002',10,35.00,3.50,0xCA26BC81082411F0B6DC4851C5F70A21),(9,'BEB-001',15,18.75,1.25,0xCA26BC81082411F0B6DC4851C5F70A21),(10,'DEP-001',3,89.97,29.99,0xCA26BCC2082411F0B6DC4851C5F70A21),(11,'ALIM-002',3,38.97,12.99,0xCA26BCC2082411F0B6DC4851C5F70A21),(12,'BEB-001',1,1.25,1.25,0xCA26BCC2082411F0B6DC4851C5F70A21);
/*!40000 ALTER TABLE `sale_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` binary(16) NOT NULL,
  `customer_name` varchar(50) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `sale_date` datetime(6) NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `discount_percentage` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (0xCA26B5A1082411F0B6DC4851C5F70A21,'María García','Tarjeta','2025-03-23 16:24:15.000000',949.99,5.00),(0xCA26BC18082411F0B6DC4851C5F70A21,'Juan Rodríguez','Efectivo','2025-03-23 16:24:15.000000',479.97,0.00),(0xCA26BC81082411F0B6DC4851C5F70A21,'Ana Martínez','Transferencia','2025-03-22 16:24:15.000000',2999.96,10.00),(0xCA26BCC2082411F0B6DC4851C5F70A21,'Carlos López','Tarjeta','2025-03-21 16:24:15.000000',129.97,0.00);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `id` binary(16) NOT NULL,
  `expiry_date` date DEFAULT NULL,
  `product_id` varchar(255) NOT NULL,
  `provider_id` binary(16) NOT NULL,
  `purchase_date` date NOT NULL,
  `purchase_unit_cost` decimal(38,2) NOT NULL,
  `quantity` int DEFAULT NULL,
  `total_purchase_cost` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (0xB34D9C46082411F0B6DC4851C5F70A21,NULL,'ELEC-001',0x6B63236F082411F0B6DC4851C5F70A21,'2025-03-23',650.00,50,32500.00),(0xB34DA300082411F0B6DC4851C5F70A21,NULL,'ELEC-002',0x6B63236F082411F0B6DC4851C5F70A21,'2025-03-23',180.00,100,18000.00),(0xB34DA402082411F0B6DC4851C5F70A21,NULL,'ROPA-001',0x6B632463082411F0B6DC4851C5F70A21,'2025-03-23',80.00,75,6000.00),(0xB34DA487082411F0B6DC4851C5F70A21,NULL,'ROPA-002',0x6B632463082411F0B6DC4851C5F70A21,'2025-03-23',20.00,120,2400.00),(0xB34DA4FF082411F0B6DC4851C5F70A21,'2026-03-23','ALIM-001',0x6B6323F7082411F0B6DC4851C5F70A21,'2025-03-23',3.50,200,700.00),(0xB34DA590082411F0B6DC4851C5F70A21,'2027-03-23','ALIM-002',0x6B6323F7082411F0B6DC4851C5F70A21,'2025-03-23',8.00,150,1200.00),(0xB34DA62A082411F0B6DC4851C5F70A21,'2025-09-23','BEB-001',0x6B6323F7082411F0B6DC4851C5F70A21,'2025-03-23',0.75,500,375.00),(0xB34DA6FD082411F0B6DC4851C5F70A21,'2025-06-23','BEB-002',0x6B6323F7082411F0B6DC4851C5F70A21,'2025-03-23',2.00,200,400.00),(0xB34DA77F082411F0B6DC4851C5F70A21,NULL,'DEP-001',0x6B632463082411F0B6DC4851C5F70A21,'2025-03-23',18.00,50,900.00),(0xB34DA7F1082411F0B6DC4851C5F70A21,NULL,'DEP-002',0x6B632463082411F0B6DC4851C5F70A21,'2025-03-23',100.00,30,3000.00);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-23 16:38:56