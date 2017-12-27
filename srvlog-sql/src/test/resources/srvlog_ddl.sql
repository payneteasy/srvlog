-- MySQL dump 10.15  Distrib 10.0.29-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.0.29-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hosts`
--

DROP TABLE IF EXISTS `hosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hosts` (
  `host_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `hostname` varchar(120) DEFAULT NULL,
  `ip` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`host_id`),
  UNIQUE KEY `unq_hosts_hostname` (`hostname`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logs` (
  `log_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `log_date` datetime DEFAULT NULL,
  `logs_partition_key` int(6) unsigned NOT NULL,
  `facility` int(3) DEFAULT NULL,
  `severity` int(3) DEFAULT NULL,
  `host_id` int(10) DEFAULT NULL,
  `message` text,
  `program` varchar(60) DEFAULT NULL,
  `hash` varchar(32) DEFAULT NULL,
  `has_snort_logs` int(1) NOT NULL DEFAULT '0',
  KEY `pk_log` (`log_id`),
  KEY `idx_logs_log_date` (`log_date`),
  KEY `idx_logs_host_log_date` (`host_id`,`log_date`),
  KEY `idx_logs_hash` (`hash`)
) ENGINE=InnoDB AUTO_INCREMENT=183594738 DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (logs_partition_key)
(PARTITION logs_201612 VALUES LESS THAN (201701) ENGINE = InnoDB,
 PARTITION logs_201701 VALUES LESS THAN (201702) ENGINE = InnoDB,
 PARTITION logs_201702 VALUES LESS THAN (201703) ENGINE = InnoDB,
 PARTITION logs_201703 VALUES LESS THAN (201704) ENGINE = InnoDB,
 PARTITION logs_201704 VALUES LESS THAN (201705) ENGINE = InnoDB,
 PARTITION logs_201705 VALUES LESS THAN (201706) ENGINE = InnoDB,
 PARTITION logs_201706 VALUES LESS THAN (201707) ENGINE = InnoDB,
 PARTITION logs_201707 VALUES LESS THAN (201708) ENGINE = InnoDB,
 PARTITION logs_201708 VALUES LESS THAN (201709) ENGINE = InnoDB,
 PARTITION logs_201709 VALUES LESS THAN (201710) ENGINE = InnoDB,
 PARTITION logs_201710 VALUES LESS THAN (201711) ENGINE = InnoDB,
 PARTITION logs_201711 VALUES LESS THAN (201712) ENGINE = InnoDB,
 PARTITION logs_201712 VALUES LESS THAN (201801) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mysql_routines_return_arguments`
--

DROP TABLE IF EXISTS `mysql_routines_return_arguments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mysql_routines_return_arguments` (
  `routine_name` varchar(128) NOT NULL,
  `argument_name` varchar(128) NOT NULL,
  `argument_type` varchar(128) NOT NULL,
  `ordinal_number` int(10) DEFAULT NULL,
  UNIQUE KEY `unq_mysql_routines_arguments_name_type` (`routine_name`,`argument_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ossec_logs`
--

DROP TABLE IF EXISTS `ossec_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ossec_logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `log_id` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL,
  `identifier` varchar(60) NOT NULL,
  `hash` varchar(32) DEFAULT NULL,
  KEY `pk_id` (`id`),
  KEY `idx_ossec_logs_log_id` (`id`),
  KEY `idx_ossec_logs_date` (`date`),
  KEY `idx_ossec_logs_identifier` (`identifier`),
  KEY `idx_ossec_logs_hash` (`hash`)
) ENGINE=InnoDB AUTO_INCREMENT=9719585 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `snort_logs`
--

DROP TABLE IF EXISTS `snort_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `snort_logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `snort_logs_partition_key` int(6) unsigned NOT NULL,
  `program` varchar(60) NOT NULL,
  `sensor_name` varchar(60) NOT NULL,
  `date` datetime NOT NULL,
  `priority` int(2) unsigned NOT NULL,
  `classification` varchar(255) NOT NULL,
  `alert_cause` varchar(255) NOT NULL,
  `generator_id` int(10) unsigned NOT NULL,
  `signature_id` int(10) unsigned NOT NULL,
  `signature_revision` int(10) unsigned NOT NULL,
  `protocol_number` int(5) unsigned DEFAULT NULL,
  `protocol_alias` varchar(60) DEFAULT NULL,
  `protocol_version` int(4) unsigned DEFAULT NULL,
  `source_ip` varchar(60) DEFAULT NULL,
  `destination_ip` varchar(60) DEFAULT NULL,
  `header_length` int(4) unsigned DEFAULT NULL,
  `service_type` int(6) unsigned DEFAULT NULL,
  `datagram_length` int(16) unsigned DEFAULT NULL,
  `identification` int(16) unsigned DEFAULT NULL,
  `flags` int(3) unsigned DEFAULT NULL,
  `fragment_offset` int(13) unsigned DEFAULT NULL,
  `time_to_live` int(8) unsigned DEFAULT NULL,
  `checksum` int(16) unsigned DEFAULT NULL,
  `source_port` int(16) unsigned DEFAULT NULL,
  `destination_port` int(16) unsigned DEFAULT NULL,
  `host` varchar(60) DEFAULT NULL,
  `x_forwarded_for` varchar(60) DEFAULT NULL,
  `x_real_ip` varchar(60) DEFAULT NULL,
  `payload` text NOT NULL,
  KEY `pk_id` (`id`),
  KEY `idx_snort_logs_date` (`date`),
  KEY `idx_snort_logs_generator_id` (`generator_id`),
  KEY `idx_snort_logs_signature_id` (`signature_id`)
) ENGINE=InnoDB AUTO_INCREMENT=97191679 DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (snort_logs_partition_key)
(PARTITION snort_logs_201709 VALUES LESS THAN (201710) ENGINE = InnoDB,
 PARTITION snort_logs_201710 VALUES LESS THAN (201711) ENGINE = InnoDB,
 PARTITION snort_logs_201711 VALUES LESS THAN (201712) ENGINE = InnoDB,
 PARTITION snort_logs_201712 VALUES LESS THAN (201801) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sph_counter`
--

DROP TABLE IF EXISTS `sph_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sph_counter` (
  `index_id` int(3) unsigned NOT NULL,
  `max_doc_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`index_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `unprocessed_logs`
--

DROP TABLE IF EXISTS `unprocessed_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unprocessed_logs` (
  `log_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `log_date` datetime DEFAULT NULL,
  `facility` int(3) DEFAULT NULL,
  `severity` int(3) DEFAULT NULL,
  `host` varchar(60) DEFAULT NULL,
  `message` text,
  `program` varchar(60) DEFAULT NULL,
  `hash` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-25 22:00:08
