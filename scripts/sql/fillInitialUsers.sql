SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

INSERT INTO `accounts` (`id`, `registration_reference`, `salutation`, `address`, `postal_code`, `city`) VALUES
(uuid(), 'NL-123', 'Mr. Joe', 'Testbeulevard 23', '1234 AB', 'Amsterdam'),
(uuid(), 'NL-124', 'Mrs. Sarah', 'Testbeulevard 231', '1235 AC', 'Den-Haag');

INSERT INTO `users` (`account_id`, `username`, `password`, `enabled`, `email`) VALUES
((select id from accounts where registration_reference = 'NL-123'), 'test', '$2a$11$7h0hncHpczNUtibioH9GxuyLHLzUnDqZNwWWG5S5Op11iSkXwtCFi', true, 'test@test123.com'),
((select id from accounts where registration_reference = 'NL-123'), 'testadmin', '$2a$11$7h0hncHpczNUtibioH9GxuyLHLzUnDqZNwWWG5S5Op11iSkXwtCFi', true, 'admin@test123.com');

INSERT INTO `authorities` (`username`, `authority`) VALUES
('test', 'USER'),
('testadmin', 'USER'),
('testadmin', 'ADMIN');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;