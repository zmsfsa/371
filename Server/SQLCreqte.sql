CREATE TABLE `users` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(20) DEFAULT NULL,
  `fName` varchar(20) DEFAULT NULL,
  `lName` varchar(20) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `phone` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8;

CREATE TABLE `photos` (
  `idPhoto` int(11) NOT NULL AUTO_INCREMENT,
  `photoFile` blob,
  `idEvent` int(11) DEFAULT NULL,
  PRIMARY KEY (`idPhoto`),
  KEY `idEvent_idx` (`idEvent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `includes` (
  `idInclude` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `idEvent` int(11) DEFAULT NULL,
  PRIMARY KEY (`idInclude`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

CREATE TABLE `friends` (
  `idFirst` int(11) NOT NULL,
  `idSecond` int(11) NOT NULL,
  `idFriendship` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idFriendship`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `events` (
  `idEvent` int(11) NOT NULL AUTO_INCREMENT,
  `nameEvent` varchar(45) DEFAULT NULL,
  `dateEvent` date DEFAULT NULL,
  PRIMARY KEY (`idEvent`),
  UNIQUE KEY `nameEvent_UNIQUE` (`nameEvent`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;
