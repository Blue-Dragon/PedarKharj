-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 22, 2019 at 09:12 PM
-- Server version: 5.7.24
-- PHP Version: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `registerlogin`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(200) COLLATE utf8_persian_ci NOT NULL,
  `email` varchar(200) COLLATE utf8_persian_ci NOT NULL,
  `password` text COLLATE utf8_persian_ci NOT NULL,
  `gender` varchar(6) COLLATE utf8_persian_ci NOT NULL,
  `profile_pic` varchar(50) COLLATE utf8_persian_ci NOT NULL DEFAULT 'ss.jpg',
  `picUpdateNum` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_persian_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `gender`, `profile_pic`, `picUpdateNum`) VALUES
(8, 'one', 't@w.o', 'f97c5d29941bfb1b2fdab0874906ab82', 'Female', 'ss.jpg', 0),
(9, '3', '3@3.COM', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'Female', 'ss.jpg', 0),
(10, '3', 'iopiopp@gmail.com', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'Female', 'ss.jpg', 1);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
