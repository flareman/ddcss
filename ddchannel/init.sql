DROP TABLE IF EXISTS `BS`;
CREATE TABLE `BS` (
  `networkID` varchar(20),
  `basestationID` varchar(45) DEFAULT '',
  `signalStrength` double DEFAULT 0,
  `frequency` double DEFAULT 0,
  `networkType` varchar(20) DEFAULT 0,
  `maxBitRate` double DEFAULT 0,
  `guaranteedBitRate` double DEFAULT 0,
  `netLoad` varchar(45) DEFAULT NULL,
  `provider` varchar(45) DEFAULT '',
  `range` double DEFAULT 0,
  `x` double DEFAULT 0,
  `y` double DEFAULT 0,
  `port` int DEFAULT 0,
  `chargeModel` varchar(20) DEFAULT '',
  PRIMARY KEY (`networkID`),
  UNIQUE KEY `networkID_UNIQUE` (`networkID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
