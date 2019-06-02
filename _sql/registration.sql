-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 02, 2019 at 12:12 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `registration`
--

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`id`, `name`) VALUES
('CSCI 3314', 'Algorithm Analysis And Design'),
('ARAB 1202', 'Arabic Language'),
('CSCI 4304', 'Artificial Intelligence'),
('CSCI 3304', 'Automata Theory'),
('MATHA1301', 'Calculus I'),
('CSCI 4328', 'Communication Skills'),
('CSCI 4302', 'Computer Graphics'),
('SICT 3309', 'Computer Networks'),
('SICT 3109', 'Computer Networks - Lab'),
('CSCI 2310', 'Computer Organization'),
('CSCI 2110', 'Computer Organization -Lab'),
('CSCI 4320', 'Cryptographia'),
('SDEV 3304', 'Data Mining'),
('CSCI 2309', 'Data Structure'),
('CSCI 2109', 'Data Structure - Lab'),
('SICT 2305', 'Database And Management System 1'),
('SICT 2105', 'Database And Management System 1 -Lab'),
('SICT 3308', 'Database And Management System 2'),
('SICT 3108', 'Database And Management System 2 - Lab'),
('CSCI 2303', 'Discrete Mathematics'),
('SICT 4401', 'Distributed System'),
('ENGL 1307', 'English Language'),
('SICT 4309', 'Information Security'),
('CSCI 1303', 'Introduction to Computing'),
('CSCI 1103', 'Introduction to Computing - Lab'),
('CSCI 2301', 'Logic Design'),
('CSCI 4310', 'Machine Learning'),
('CSCI 3310', 'Mathematical Computation'),
('CSCI 3306', 'Operation System'),
('CSCI 3106', 'Operation System - Lab'),
('CSCI 4316', 'Pattern Recognition'),
('CSCI 1304', 'Programming 1'),
('CSCI 1104', 'Programming 1 -Lab'),
('CSCI 1306', 'Programming 2'),
('CSCI 1106', 'Programming 2 -Lab'),
('CSCI 2308', 'Programming 3'),
('CSCI 2108', 'Programming 3 -Lab'),
('CSCI 3303', 'Programming Fundamentals'),
('SDEV 2302', 'Software Engineering'),
('SICT 2107', 'Web Programming	1 - Lab'),
('SICT 2308', 'Web Programming	2'),
('SICT 2108', 'Web Programming	2 - Lab'),
('SICT 2307', 'Web Programming 1');

-- --------------------------------------------------------

--
-- Table structure for table `instructor`
--

CREATE TABLE `instructor` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instructor`
--

INSERT INTO `instructor` (`id`, `name`) VALUES
('cmichael', 'Charles Michael'),
('jliam', 'John Liam'),
('jnoah', 'James Noah'),
('mjacob', 'Michael Jacob'),
('rmason', 'Robert Mason'),
('talexander', 'Thomas Alexander'),
('wethan', 'David Ethan'),
('wwilliam', 'William William');

-- --------------------------------------------------------

--
-- Table structure for table `precourses`
--

CREATE TABLE `precourses` (
  `studentid` varchar(255) NOT NULL,
  `courseid` varchar(255) NOT NULL,
  `semesterid` varchar(100) DEFAULT NULL,
  `grade` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `precourses`
--

INSERT INTO `precourses` (`studentid`, `courseid`, `semesterid`, `grade`) VALUES
('120170311', 'Arabic Language', 'firstsemester', 82),
('120170311', 'Calculus I', 'firstsemester', 90),
('120170311', 'Data Structure', 'thirdsemester', 90),
('120170311', 'Data Structure - Lab', 'thirdsemester', 90),
('120170311', 'Discrete Mathematics', 'secondsemester', 92),
('120170311', 'Introduction to Computing', 'firstsemester', 97),
('120170311', 'Introduction to Computing - Lab', 'firstsemester', 88),
('120170311', 'Logic Design', 'secondsemester', 97),
('120170311', 'Mathematical Computation', 'thirdsemester', 95),
('120170311', 'Programming 1', 'firstsemester', 82),
('120170311', 'Programming 1 -Lab', 'firstsemester', 94),
('120170311', 'Programming 2', 'secondsemester', 87),
('120170311', 'Programming 2 -Lab', 'secondsemester', 90),
('120170311', 'Software Engineering', 'thirdsemester', 97),
('120170311', 'Web Programming	1 - Lab', 'thirdsemester', 85),
('120170311', 'Web Programming 1', 'thirdsemester', 90);

-- --------------------------------------------------------

--
-- Table structure for table `regcourses`
--

CREATE TABLE `regcourses` (
  `studentid` varchar(255) NOT NULL,
  `coursename` varchar(255) NOT NULL,
  `starttime` time NOT NULL,
  `endtime` time NOT NULL,
  `sectionnumber` int(11) NOT NULL,
  `instructor` varchar(255) NOT NULL,
  `lab` varchar(100) NOT NULL,
  `days` varchar(100) NOT NULL,
  `semesternumber` int(11) DEFAULT NULL,
  `semesteryear` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `regcourses`
--

INSERT INTO `regcourses` (`studentid`, `coursename`, `starttime`, `endtime`, `sectionnumber`, `instructor`, `lab`, `days`, `semesternumber`, `semesteryear`) VALUES
('120170311', 'Computer Organization -Lab', '12:00:00', '14:00:00', 102, 'William William', 'I308', 'Mon', 2, '2019'),
('120170311', 'Programming 3 -Lab', '10:00:00', '12:00:00', 102, 'Charles Michael', 'I318', 'Tue', 2, '2019'),
('120170311', 'Computer Organization', '09:00:00', '10:00:00', 101, 'Charles Michael', 'I116', 'Sat-Mon-Wed', 2, '2019'),
('120170311', 'Web Programming	2 - Lab', '12:00:00', '14:00:00', 103, 'Thomas Alexander', 'I302', 'Wed', 2, '2019');

-- --------------------------------------------------------

--
-- Table structure for table `section`
--

CREATE TABLE `section` (
  `sectionnumber` int(11) NOT NULL,
  `coursename` varchar(255) NOT NULL,
  `instructorid` varchar(255) NOT NULL,
  `starttime` time DEFAULT NULL,
  `endtime` time DEFAULT NULL,
  `days` varchar(100) NOT NULL,
  `lab` varchar(100) NOT NULL,
  `semesterid` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `section`
--

INSERT INTO `section` (`sectionnumber`, `coursename`, `instructorid`, `starttime`, `endtime`, `days`, `lab`, `semesterid`) VALUES
(101, 'Arabic Language', 'jnoah', '08:00:00', '09:00:00', 'sat-mon-wed', 'K101', 'firstsemester'),
(101, 'Computer Organization', 'cmichael', '09:00:00', '10:00:00', 'Sat-Mon-Wed', 'I116', 'fifthsemester'),
(101, 'Computer Organization -Lab', 'wwilliam', '12:00:00', '14:00:00', 'Sat', 'I308', 'fifthsemester'),
(101, 'Data Structure - Lab', 'cmichael', '10:00:00', '12:00:00', 'sat', 'I301', 'fourthsemester'),
(101, 'Database And Management System 1', 'mjacob', '12:30:00', '14:00:00', 'Sun-Tue', 'K031', 'fifthsemester'),
(101, 'Database And Management System 1 -Lab', 'wethan', '12:00:00', '14:00:00', 'Sat', 'I318', 'fifthsemester'),
(101, 'Discrete Mathematics', 'rmason', '12:00:00', '13:00:00', 'sat-mon-wed', 'I201', 'secondsemester'),
(101, 'English Language', 'mjacob', '08:00:00', '09:30:00', 'sun-tue', 'K101', 'secondsemester'),
(101, 'Logic Design', 'cmichael', '14:00:00', '15:30:00', 'sun-tue', 'I301', 'secondsemester'),
(101, 'Mathematical Computation', 'rmason', '12:00:00', '13:00:00', 'sat-mon-wed', 'K301', 'fourthsemester'),
(101, 'Programming 1', 'wethan', '10:00:00', '12:00:00', 'sat-mon-wed', 'I101', 'firstsemester'),
(101, 'Programming 3', 'jnoah', '14:00:00', '15:30:00', 'Sun-Tue', 'I116', 'fifthsemester'),
(101, 'Programming 3 -Lab', 'cmichael', '10:00:00', '12:00:00', 'Sun', 'I318', 'fifthsemester'),
(101, 'Software Engineering', 'wwilliam', '13:00:00', '14:00:00', 'sat-mon-wed', 'K412', 'fourthsemester'),
(101, 'Web Programming	2', 'jliam', '08:00:00', '09:00:00', 'Sat-Mon-Wed', 'I116', 'fifthsemester'),
(101, 'Web Programming	2 - Lab', 'talexander', '12:00:00', '14:00:00', 'Sat', 'I302', 'fifthsemester'),
(102, 'Computer Organization -Lab', 'wwilliam', '12:00:00', '14:00:00', 'Mon', 'I308', 'fifthsemester'),
(102, 'Data Structure - Lab', 'cmichael', '10:00:00', '12:00:00', 'mon', 'I301', 'fourthsemester'),
(102, 'Database And Management System 1 -Lab', 'wethan', '12:00:00', '14:00:00', 'Mon', 'I318', 'fifthsemester'),
(102, 'Programming 3 -Lab', 'cmichael', '10:00:00', '12:00:00', 'Tue', 'I318', 'fifthsemester'),
(102, 'Web Programming	2 - Lab', 'talexander', '12:00:00', '14:00:00', 'Mon', 'I302', 'fifthsemester'),
(103, 'Computer Organization -Lab', 'wwilliam', '12:00:00', '14:00:00', 'Wed', 'I308', 'fifthsemester'),
(103, 'Database And Management System 1 -Lab', 'wethan', '12:00:00', '14:00:00', 'Wed', 'I318', 'fifthsemester'),
(103, 'Web Programming	2 - Lab', 'talexander', '12:00:00', '14:00:00', 'Wed', 'I302', 'fifthsemester');

-- --------------------------------------------------------

--
-- Table structure for table `semester`
--

CREATE TABLE `semester` (
  `number` int(11) NOT NULL,
  `id` varchar(100) NOT NULL,
  `year` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `semester`
--

INSERT INTO `semester` (`number`, `id`, `year`) VALUES
(2, 'eighthsemester', '2021'),
(1, 'fifthsemester', '2019'),
(1, 'firstsemester', '2017'),
(2, 'fourthsemester', '2019'),
(2, 'secondsemester', '2018'),
(1, 'seventhsemester', '2020'),
(2, 'sixthsemester', '2020'),
(1, 'thirdsemester', '2018');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `name`, `lname`, `email`, `phone`, `address`, `password`) VALUES
('120170311', 'mohammed', 'ghunaim', 'mohammed@gmail.com', '+970595822374', 'gaza', '827ccb0eea8a706c4c34a16891f84e7b');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `instructor`
--
ALTER TABLE `instructor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `precourses`
--
ALTER TABLE `precourses`
  ADD PRIMARY KEY (`studentid`,`courseid`),
  ADD KEY `semesterid_fore` (`semesterid`),
  ADD KEY `courseid_fore` (`courseid`);

--
-- Indexes for table `section`
--
ALTER TABLE `section`
  ADD PRIMARY KEY (`sectionnumber`,`coursename`,`instructorid`),
  ADD KEY `coursename_cons` (`coursename`),
  ADD KEY `instructorid_cons` (`instructorid`),
  ADD KEY `semesterid_cons` (`semesterid`);

--
-- Indexes for table `semester`
--
ALTER TABLE `semester`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `precourses`
--
ALTER TABLE `precourses`
  ADD CONSTRAINT `courseid_fore` FOREIGN KEY (`courseid`) REFERENCES `course` (`name`),
  ADD CONSTRAINT `semesterid_fore` FOREIGN KEY (`semesterid`) REFERENCES `semester` (`id`),
  ADD CONSTRAINT `studentid_fore` FOREIGN KEY (`studentid`) REFERENCES `student` (`id`);

--
-- Constraints for table `section`
--
ALTER TABLE `section`
  ADD CONSTRAINT `coursename_cons` FOREIGN KEY (`coursename`) REFERENCES `course` (`name`),
  ADD CONSTRAINT `instructorid_cons` FOREIGN KEY (`instructorid`) REFERENCES `instructor` (`id`),
  ADD CONSTRAINT `semesterid_cons` FOREIGN KEY (`semesterid`) REFERENCES `semester` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
