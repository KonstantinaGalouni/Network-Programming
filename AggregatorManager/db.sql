SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `mydb` ;
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET latin1 COLLATE latin1_general_cs ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Users` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Users` (
  `idUsers` INT NOT NULL AUTO_INCREMENT,
  `username` VARBINARY(45) NOT NULL,
  `password` BINARY(32) NOT NULL,
  `active` TINYINT(1) NOT NULL,
  `admin` TINYINT(1) NOT NULL,
  PRIMARY KEY (`idUsers`),
  UNIQUE INDEX `idUsers_UNIQUE` (`idUsers` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`SoftwareAgents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`SoftwareAgents` ;

CREATE TABLE IF NOT EXISTS `mydb`.`SoftwareAgents` (
  `idSoftwareAgents` INT NOT NULL AUTO_INCREMENT,
  `deviceName` VARCHAR(45) NOT NULL,
  `ipAddress` VARCHAR(45) NOT NULL,
  `macAddress` VARCHAR(45) NOT NULL,
  `osVersion` VARCHAR(45) NOT NULL,
  `nmapVersion` VARCHAR(45) NOT NULL,
  `hashKey` INT NOT NULL,
  `confirmed` TINYINT(1) NOT NULL,
  `lastRequest` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idSoftwareAgents`),
  UNIQUE INDEX `idSoftwareAgents_UNIQUE` (`idSoftwareAgents` ASC),
  UNIQUE INDEX `hashKey_UNIQUE` (`hashKey` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`NmapJobs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`NmapJobs` ;

CREATE TABLE IF NOT EXISTS `mydb`.`NmapJobs` (
  `idNmapJobs` INT NOT NULL AUTO_INCREMENT,
  `idSoftwareAgents` INT NOT NULL,
  `flags` VARCHAR(45) NOT NULL,
  `periodic` BOOLEAN NOT NULL,
  `periodicTime` INT NOT NULL,
  `sent` BOOLEAN NOT NULL,
  `stopped` BOOLEAN NOT NULL,
  PRIMARY KEY (`idNmapJobs`),
  UNIQUE INDEX `idNmapJobs_UNIQUE` (`idNmapJobs` ASC),
  INDEX `fk_NmapJobs_SoftwareAgents1_idx` (`idSoftwareAgents` ASC),
  CONSTRAINT `fk_NmapJobs_SoftwareAgents1`
    FOREIGN KEY (`idSoftwareAgents`)
    REFERENCES `mydb`.`SoftwareAgents` (`idSoftwareAgents`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`JobResults`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`JobResults` ;

CREATE TABLE IF NOT EXISTS `mydb`.`JobResults` (
  `idJobResults` INT NOT NULL AUTO_INCREMENT,
  `time` TIMESTAMP NOT NULL,	
  `result` LONGTEXT NOT NULL,
  `idNmapJobs` INT NOT NULL,
  PRIMARY KEY (`idJobResults`),
  UNIQUE INDEX `idJobResults_UNIQUE` (`idJobResults` ASC),
  INDEX `fk_JobResults_NmapJobs1_idx` (`idNmapJobs` ASC),
  CONSTRAINT `fk_JobResults_NmapJobs1`
    FOREIGN KEY (`idNmapJobs`)
    REFERENCES `mydb`.`NmapJobs` (`idNmapJobs`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
