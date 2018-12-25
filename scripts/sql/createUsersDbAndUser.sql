SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

DROP DATABASE IF EXISTS `gemeente-users`;
CREATE DATABASE `gemeente-users`;
CREATE USER IF NOT EXISTS 'gemeente-users'@'%' identified by 'gemeente-users';
CREATE USER IF NOT EXISTS 'gemeente-users'@'localhost' identified by 'gemeente-users';
GRANT all on `gemeente-users`.* to 'gemeente-users'@'%';
GRANT all on `gemeente-users`.* to 'gemeente-users'@'localhost';

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;