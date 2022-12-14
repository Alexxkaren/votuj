-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema votuj_test
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema votuj_test
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `votuj_test` DEFAULT CHARACTER SET utf8 ;
USE `votuj_test` ;

-- -----------------------------------------------------
-- Table `votuj_test`.`region`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`region` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`party`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`party` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `info` VARCHAR(1000) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`vote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`vote` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `age` INT NOT NULL,
  `male` TINYINT NOT NULL,
  `date` DATETIME NOT NULL,
  `id_region` INT NOT NULL,
  `id_party` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_vote_region_idx` (`id_region` ASC) VISIBLE,
  INDEX `fk_vote_party1_idx` (`id_party` ASC) VISIBLE,
  CONSTRAINT `fk_vote_region`
    FOREIGN KEY (`id_region`)
    REFERENCES `votuj_test`.`region` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vote_party1`
    FOREIGN KEY (`id_party`)
    REFERENCES `votuj_test`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
KEY_BLOCK_SIZE = 16;


-- -----------------------------------------------------
-- Table `votuj_test`.`candidate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`candidate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `candidate_number` INT NOT NULL,
  `info` VARCHAR(1000) NOT NULL,
  `id_party` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_candidate_party1_idx` (`id_party` ASC) VISIBLE,
  CONSTRAINT `fk_candidate_party1`
    FOREIGN KEY (`id_party`)
    REFERENCES `votuj_test`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

use votuj_test;
ALTER TABLE candidate DROP FOREIGN KEY `fk_candidate_party1`;
Alter table candidate  add constraint `fk_candidate_party1` foreign key (`id_party`) references `votuj_test`.`party` (`id`)
ON DELETE cascade;

-- -----------------------------------------------------
-- Table `votuj_test`.`term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`term` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `since` INT NOT NULL,
  `to` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`program` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `id_party` INT NOT NULL,
  `is_active` TINYINT NOT NULL,
  `id_term` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_program_party1_idx` (`id_party` ASC) VISIBLE,
  INDEX `fk_program_term1_idx` (`id_term` ASC) VISIBLE,
  CONSTRAINT `fk_program_party1`
    FOREIGN KEY (`id_party`)
    REFERENCES `votuj_test`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_program_term1`
    FOREIGN KEY (`id_term`)
    REFERENCES `votuj_test`.`term` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `info` VARCHAR(300) NOT NULL,
  `id_program` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_point_program1_idx` (`id_program` ASC) VISIBLE,
  CONSTRAINT `fk_point_program1`
    FOREIGN KEY (`id_program`)
    REFERENCES `votuj_test`.`program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`item_has_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`item_has_category` (
  `id_item` INT NOT NULL,
  `id_category` INT NOT NULL,
  PRIMARY KEY (`id_item`, `id_category`),
  INDEX `fk_point_has_category_category1_idx` (`id_category` ASC) VISIBLE,
  INDEX `fk_point_has_category_point1_idx` (`id_item` ASC) VISIBLE,
  CONSTRAINT `fk_point_has_category_point1`
    FOREIGN KEY (`id_item`)
    REFERENCES `votuj_test`.`item` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_point_has_category_category1`
    FOREIGN KEY (`id_category`)
    REFERENCES `votuj_test`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj_test`.`candidate_has_term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj_test`.`candidate_has_term` (
  `id_candidate` INT NOT NULL,
  `id_term` INT NOT NULL,
  PRIMARY KEY (`id_candidate`, `id_term`),
  INDEX `fk_candidate_has_term_term1_idx` (`id_term` ASC) VISIBLE,
  INDEX `fk_candidate_has_term_candidate1_idx` (`id_candidate` ASC) VISIBLE,
  CONSTRAINT `fk_candidate_has_term_candidate1`
    FOREIGN KEY (`id_candidate`)
    REFERENCES `votuj_test`.`candidate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_candidate_has_term_term1`
    FOREIGN KEY (`id_term`)
    REFERENCES `votuj_test`.`term` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;




SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
-- ---------------------------------------------------------------------------------------------------------------------------
INSERT INTO `votuj`.`region` (`name`) VALUES ('Bratislavsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Trnavsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Tren??iansky kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Nitriansky kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('??ilinsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Banskobystrick?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Pre??ovsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Ko??ick?? kraj');
