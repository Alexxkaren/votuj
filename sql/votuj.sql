-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema votuj
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema votuj
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `votuj` DEFAULT CHARACTER SET utf8 ;
USE `votuj` ;

-- -----------------------------------------------------
-- Table `votuj`.`region`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`region` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`party`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`party` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `info` VARCHAR(1000) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`vote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`vote` (
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
    REFERENCES `votuj`.`region` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vote_party1`
    FOREIGN KEY (`id_party`)
    REFERENCES `votuj`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
KEY_BLOCK_SIZE = 16;


-- -----------------------------------------------------
-- Table `votuj`.`candidate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`candidate` (
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
    REFERENCES `votuj`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`program` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `party_id` INT NOT NULL,
  `term_from` INT NOT NULL,
  `term_to` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_program_party1_idx` (`party_id` ASC) VISIBLE,
  CONSTRAINT `fk_program_party1`
    FOREIGN KEY (`party_id`)
    REFERENCES `votuj`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `info` VARCHAR(300) NOT NULL,
  `id_program` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_point_program1_idx` (`id_program` ASC) VISIBLE,
  CONSTRAINT `fk_point_program1`
    FOREIGN KEY (`id_program`)
    REFERENCES `votuj`.`program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`item_has_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`item_has_category` (
  `id_item` INT NOT NULL,
  `id_category` INT NOT NULL,
  PRIMARY KEY (`id_item`, `id_category`),
  INDEX `fk_point_has_category_category1_idx` (`id_category` ASC) VISIBLE,
  INDEX `fk_point_has_category_point1_idx` (`id_item` ASC) VISIBLE,
  CONSTRAINT `fk_point_has_category_point1`
    FOREIGN KEY (`id_item`)
    REFERENCES `votuj`.`item` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_point_has_category_category1`
    FOREIGN KEY (`id_category`)
    REFERENCES `votuj`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`admin` (
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`name`, `password`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`term` (
  `id` INT NOT NULL,
  `since` INT NOT NULL,
  `to` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`candidate_has_term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`candidate_has_term` (
  `candidate_id` INT NOT NULL,
  `term_id` INT NOT NULL,
  PRIMARY KEY (`candidate_id`, `term_id`),
  INDEX `fk_candidate_has_term_term1_idx` (`term_id` ASC) VISIBLE,
  INDEX `fk_candidate_has_term_candidate1_idx` (`candidate_id` ASC) VISIBLE,
  CONSTRAINT `fk_candidate_has_term_candidate1`
    FOREIGN KEY (`candidate_id`)
    REFERENCES `votuj`.`candidate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_candidate_has_term_term1`
    FOREIGN KEY (`term_id`)
    REFERENCES `votuj`.`term` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
