-- MySQL Workbench Forward Engineering

DROP database IF EXISTS votuj;

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
-- Table `votuj`.`term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`term` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `since` INT NOT NULL,
  `to` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`program` (
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
    REFERENCES `votuj`.`party` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_program_term1`
    FOREIGN KEY (`id_term`)
    REFERENCES `votuj`.`term` (`id`)
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
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `votuj`.`candidate_has_term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `votuj`.`candidate_has_term` (
  `id_candidate` INT NOT NULL,
  `id_term` INT NOT NULL,
  PRIMARY KEY (`id_candidate`, `id_term`),
  INDEX `fk_candidate_has_term_term1_idx` (`id_term` ASC) VISIBLE,
  INDEX `fk_candidate_has_term_candidate1_idx` (`id_candidate` ASC) VISIBLE,
  CONSTRAINT `fk_candidate_has_term_candidate1`
    FOREIGN KEY (`id_candidate`)
    REFERENCES `votuj`.`candidate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_candidate_has_term_term1`
    FOREIGN KEY (`id_term`)
    REFERENCES `votuj`.`term` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- ------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`admin` (id, name, password) VALUES ('1', 'admin', '$2a$10$LEQF7b3K5Bh6lS2GVLUsFu8zoF6ls7qsdfoltIHCqQcgUgy7PlChO');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`party` (id, name, info) VALUES ('1', 'Sloboda a Sloidarita', 'Strana Sloboda a Solidarita je jedna z mála strán postavených na Hodnotách. Vychádzajúc z nášho Poslania sú našimi Hodnotami Sloboda, Solidarita a Zdravý rozum.');
INSERT INTO votuj.`party` (id, name, info) VALUES ('2', 'Smer-SD', 'Strana SMER - sociálna demokracia je najväčšou a najúspešnejšou politickou stranou na Slovensku. Naším cieľom je už od vzniku v roku 1999 zastupovať záujmy slabších a ohrozených skupín, v politickom súboji presadzovať stabilitu a pokoj v štáte a vybudovať úspešne Slovensko');
INSERT INTO votuj.`party` (id, name, info) VALUES ('3', 'Za ľudí', 'Slovensko, ktoré funguje a je radostným miestom na život. Štát, ktorý si váži a chráni poctivých a slušných a trestá klamárov a zlodejov. Kde budúcnosť detí nezáleží od miesta narodenia, ale od vzdelania a šikovnosti. Štát, ktorý motivuje mladých, aby si zakladali rodiny a vychovávali tu budúce generácie. Ktorý vytvára férové podmienky ako pre pracujúcich, tak podnikateľov. Ktorý sa postará o chorých, starších a trpiacich.Štát, kde človek nie je len číslom v štatistike, ale centrom skutočného záujmu. Zákazníkom, ktorý má právo na tie najlepšie služby.Štát, ktorý vie, že ochrana prírody musí mať prednosť pred peniazmi a priemyslom.');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`term` (id, since, `to`) VALUES ('1', '2020', '2024');
INSERT INTO votuj.`term` (id, since, `to`) VALUES ('2', '2016', '2020');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`program` (id, name, id_party, is_active, id_term) VALUES ('1', 'Návod na lepšie Slovensko', '1', '1', '1');
INSERT INTO votuj.`program` (id, name, id_party, is_active, id_term) VALUES ('2', 'Aby sa doma oplatilo pracovať, podnikať, žiť', '1', '0', '2');
INSERT INTO votuj.`program` (id, name, id_party, is_active, id_term) VALUES ('3', 'Mapa dobrých riešení', '3', '1', '1');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('1', 'Richard', 'Sulík', '1', 'Ing. Richard Sulík je slovenský ekonóm, politik, bývalý europoslanec a bývalý minister hospodárstva Slovenskej republiky. Je zakladateľom pravicovo-liberálnej politickej strany Sloboda a Solidarita. Vo vláde Ivety Radičovej v období rokov 2010 až 2011 bol predsedom Národnej rady SR.
Dátum a miesto narodenia: 12. januára 1968 (vek 54 rokov), Bratislava
Vzdelanie: Mnichovská univerzita (1989–1992), Technische Universität München (1987–1989)
Funkcia: Minister hospodárstva Slovenskej republiky', '1');
INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('2', 'Róbert', 'Fico', '1', 'Doc. JUDr. Robert Fico, CSc. je slovenský politik, poslanec Národnej rady Slovenskej republiky, bývalý predseda vlády Slovenskej republiky a predseda ľavicovej politickej strany SMER – sociálna demokracia. V predchádzajúcej politickej kariére bol Robert Fico členom KSČ a SDĽ. 
Dátum a miesto narodenia: 15. septembra 1964 (vek 58 rokov), Topoľčany
Vzdelanie: Univerzita Komenského v Bratislave (1982–1986), Právnická fakulta Univerzity Komenského v Bratislave', '2');
-- INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('3', 'Veronika', 'Remišová', '1', 've', '3');

INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('3', 'Veronika', 'Remišová', '1', 'Mgr. art. Veronika Remišová, ArtD., M.A., rod. Belošovičová je slovenská politička, predsedkyňa politickej strany ZA ĽUDÍ, podpredsedníčka vlády Slovenskej republiky a ministerka investícií, regionálneho rozvoja a informatizácie SR vo vláde Eduarda Hegera
Dátum a miesto narodenia: 31. mája 1976 (vek 46 rokov), Žilina
Vzdelanie: Vysoká škola múzických umení v Bratislave, CERIS-ULB Diplomatic School of Brussels (CERIS), Sorbonne Université, Univerzita Cambridge
Funkcia: Podpredsedníčka vlády Slovenskej republiky', '3');

INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('1', '1');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('2', '1');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('3', '1');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('1', '2');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('2', '2');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`category` (id, name) VALUES ('1', 'Pôdohospodárstvo');
INSERT INTO votuj.`category` (id, name) VALUES ('2', 'Školstvo');
INSERT INTO votuj.`category` (id, name) VALUES ('3', 'Životné prostredie');
INSERT INTO votuj.`category` (id, name) VALUES ('4', 'Doprava');

INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('1', '724', 'Farmári dostanú vopred určenú sumu poistného plnenia v prípade, že nastanú podmienky poistnej udalosti. ', '3');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('2', '722', 'Podporíme výchovu mladých poľnohospodárov už v štádiu náboru na učňovské školy a ich výchovu v spolupráci s podnikmi v praxi.', '3');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('3', '562', 'Zvýšime napojenie domácností na verejnú kanalizáciu a čistiarne odpadových vôd o 10 %.', '3');


INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('4', '8', 'Navrhneme zákaz predaja 1090 kotlov na tuhé palivo 1. a 2. emisnej triedy, ktoré sa najviac podieľajú na znečistení ovzdušia. ', '1');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('5', '24', 'Zvýšime ekologickú gramotnosť obyvateľstva. Do učebných osnov zavedieme vzdelávanie o životnom prostredí.', '1');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('6', '4', 'Zmeníme smerovanie výnosov z mýtneho systému tak, že pôjde priamo jednotlivým správcom komunikácie, za ktorej prejazd bol poplatok zaplatený.', '1');


INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('7', '19', 'Zavedieme znížené limity na čerpanie zliav z mýtnych poplatkov, čím podporíme slovenských dopravcov.', '2');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('8', '56', 'Umožníme základným školám hodnotiť žiakov a študentov bez použitia známok, napríklad slovným hodnotením.', '2');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('9', '32', 'Daňovo zvýhodníme ekologické motorové vozidlá. ', '2');

INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('1', '1');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('2', '1');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('2', '2');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('3', '3');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('4', '3');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('5', '2');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('5', '3');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('6', '4');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('7', '4');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('8', '2');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('9', '4');
INSERT INTO votuj.`item_has_category` (id_item, id_category) VALUES ('9', '3');
-- ------------------------------------------------------------------------------------------------------------------------------------------
INSERT INTO `votuj`.`region` (`name`) VALUES ('Bratislavský kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Trnavský kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Trenčiansky kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Nitriansky kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Žilinský kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Banskobystrický kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Prešovský kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Košický kraj');
