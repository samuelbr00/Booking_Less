-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Creato il: Ott 08, 2023 alle 16:36
-- Versione del server: 5.7.34
-- Versione PHP: 7.4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lesson_booking`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `affiliazione`
--

CREATE TABLE `affiliazione` (
  `teacher_id` varchar(255) NOT NULL,
  `course_title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `affiliazione`
--

INSERT INTO `affiliazione` (`teacher_id`, `course_title`) VALUES
('1', 'Analisi'),
('2', 'Analisi'),
('1', 'Fisica'),
('3', 'Fisica');

-- --------------------------------------------------------

--
-- Struttura della tabella `corso`
--

CREATE TABLE `corso` (
  `title` varchar(255) NOT NULL,
  `desc` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `corso`
--

INSERT INTO `corso` (`title`, `desc`) VALUES
('Analisi', 'Limiti, derivate e integrali'),
('Fisica', 'Circuiti');

-- --------------------------------------------------------

--
-- Struttura della tabella `docente`
--

CREATE TABLE `docente` (
  `id_number` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `docente`
--

INSERT INTO `docente` (`id_number`, `name`, `surname`) VALUES
('1', 'Mario', 'Rossi'),
('2', 'Giovanni', 'Neri'),
('3', 'Alessio', 'Verdi');

-- --------------------------------------------------------

--
-- Struttura della tabella `ripetizione`
--

CREATE TABLE `ripetizione` (
  `teacher` varchar(100) NOT NULL,
  `t_slot` varchar(100) NOT NULL,
  `day` varchar(100) NOT NULL,
  `status` varchar(255) NOT NULL,
  `user` varchar(100) NOT NULL,
  `course` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `ripetizione`
--

INSERT INTO `ripetizione` (`teacher`, `t_slot`, `day`, `status`, `user`, `course`, `name`, `surname`) VALUES
('3', '16:00-17:00', 'Giovedi', 'effettuata', 'samuel.bruno', 'Fisica', 'Alessio', 'Verdi');

-- --------------------------------------------------------

--
-- Struttura della tabella `utente`
--

CREATE TABLE `utente` (
  `account` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `utente`
--

INSERT INTO `utente` (`account`, `password`, `role`) VALUES
('luca.disalvo', '6E6BC4E49DD477EBC98EF4046C067B5F', 'utente'),
('samuel.bruno', '6E6BC4E49DD477EBC98EF4046C067B5F', 'amministratore'),
('samuel.prova', 'DE43660386DB04FA5DCE2030568416B0', 'utente');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `affiliazione`
--
ALTER TABLE `affiliazione`
  ADD PRIMARY KEY (`teacher_id`,`course_title`),
  ADD KEY `affiliazione_course___fk` (`course_title`);

--
-- Indici per le tabelle `corso`
--
ALTER TABLE `corso`
  ADD PRIMARY KEY (`title`);

--
-- Indici per le tabelle `docente`
--
ALTER TABLE `docente`
  ADD PRIMARY KEY (`id_number`);

--
-- Indici per le tabelle `ripetizione`
--
ALTER TABLE `ripetizione`
  ADD PRIMARY KEY (`teacher`,`t_slot`,`day`,`user`),
  ADD KEY `ripetizione_user___fk` (`user`);

--
-- Indici per le tabelle `utente`
--
ALTER TABLE `utente`
  ADD PRIMARY KEY (`account`);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `affiliazione`
--
ALTER TABLE `affiliazione`
  ADD CONSTRAINT `affiliazione_course___fk` FOREIGN KEY (`course_title`) REFERENCES `corso` (`title`) ON DELETE CASCADE,
  ADD CONSTRAINT `affiliazione_teacher__fk` FOREIGN KEY (`teacher_id`) REFERENCES `docente` (`id_number`) ON DELETE CASCADE;

--
-- Limiti per la tabella `ripetizione`
--
ALTER TABLE `ripetizione`
  ADD CONSTRAINT `ripetizione_user___fk` FOREIGN KEY (`user`) REFERENCES `utente` (`account`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
