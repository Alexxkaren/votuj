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

INSERT INTO votuj.`party` (id, name, info) VALUES ('1', 'Sloboda a Sloidarita', 'Strana Sloboda a Solidarita je jedna z m??la str??n postaven??ch na Hodnot??ch. Vych??dzaj??c z n????ho Poslania s?? na??imi Hodnotami Sloboda, Solidarita a Zdrav?? rozum.');
INSERT INTO votuj.`party` (id, name, info) VALUES ('2', 'Smer-SD', 'Strana SMER - soci??lna demokracia je najv??????ou a naj??spe??nej??ou politickou stranou na Slovensku. Na????m cie??om je u?? od vzniku v roku 1999 zastupova?? z??ujmy slab????ch a ohrozen??ch skup??n, v politickom s??boji presadzova?? stabilitu a pokoj v ??t??te a vybudova?? ??spe??ne Slovensko');
INSERT INTO votuj.`party` (id, name, info) VALUES ('3', 'Za ??ud??', 'Slovensko, ktor?? funguje a je radostn??m miestom na ??ivot. ??t??t, ktor?? si v????i a chr??ni poctiv??ch a slu??n??ch a trest?? klam??rov a zlodejov. Kde bud??cnos?? det?? nez??le???? od miesta narodenia, ale od vzdelania a ??ikovnosti. ??t??t, ktor?? motivuje mlad??ch, aby si zakladali rodiny a vychov??vali tu bud??ce gener??cie. Ktor?? vytv??ra f??rov?? podmienky ako pre pracuj??cich, tak podnikate??ov. Ktor?? sa postar?? o chor??ch, star????ch a trpiacich.??t??t, kde ??lovek nie je len ????slom v ??tatistike, ale centrom skuto??n??ho z??ujmu. Z??kazn??kom, ktor?? m?? pr??vo na tie najlep??ie slu??by.??t??t, ktor?? vie, ??e ochrana pr??rody mus?? ma?? prednos?? pred peniazmi a priemyslom.');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`term` (id, since, `to`) VALUES ('1', '2020', '2024');
INSERT INTO votuj.`term` (id, since, `to`) VALUES ('2', '2016', '2020');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`program` (id, name, id_party, is_active, id_term) VALUES ('1', 'N??vod na lep??ie Slovensko', '1', '1', '1');
INSERT INTO votuj.`program` (id, name, id_party, is_active, id_term) VALUES ('2', 'Aby sa doma oplatilo pracova??, podnika??, ??i??', '1', '0', '2');
INSERT INTO votuj.`program` (id, name, id_party, is_active, id_term) VALUES ('3', 'Mapa dobr??ch rie??en??', '3', '1', '1');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('1', 'Richard', 'Sul??k', '1', 'Ing. Richard Sul??k je slovensk?? ekon??m, politik, b??val?? europoslanec a b??val?? minister hospod??rstva Slovenskej republiky. Je zakladate??om pravicovo-liber??lnej politickej strany Sloboda a Solidarita. Vo vl??de Ivety Radi??ovej v obdob?? rokov 2010 a?? 2011 bol predsedom N??rodnej rady SR.
D??tum a miesto narodenia: 12. janu??ra 1968 (vek 54 rokov), Bratislava
Vzdelanie: Mnichovsk?? univerzita (1989???1992), Technische Universit??t M??nchen (1987???1989)
Funkcia: Minister hospod??rstva Slovenskej republiky', '1');
INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('2', 'R??bert', 'Fico', '1', 'Doc. JUDr. Robert Fico, CSc. je slovensk?? politik, poslanec N??rodnej rady Slovenskej republiky, b??val?? predseda vl??dy Slovenskej republiky a predseda ??avicovej politickej strany SMER ??? soci??lna demokracia. V predch??dzaj??cej politickej kari??re bol Robert Fico ??lenom KS?? a SD??. 
D??tum a miesto narodenia: 15. septembra 1964 (vek 58 rokov), Topo????any
Vzdelanie: Univerzita Komensk??ho v Bratislave (1982???1986), Pr??vnick?? fakulta Univerzity Komensk??ho v Bratislave', '2');
-- INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('3', 'Veronika', 'Remi??ov??', '1', 've', '3');

INSERT INTO votuj.`candidate` (id, name, surname, candidate_number, info, id_party) VALUES ('3', 'Veronika', 'Remi??ov??', '1', 'Mgr. art. Veronika Remi??ov??, ArtD., M.A., rod. Belo??ovi??ov?? je slovensk?? politi??ka, predsedky??a politickej strany ZA ??UD??, podpredsedn????ka vl??dy Slovenskej republiky a ministerka invest??ci??, region??lneho rozvoja a informatiz??cie SR vo vl??de Eduarda Hegera
D??tum a miesto narodenia: 31. m??ja 1976 (vek 46 rokov), ??ilina
Vzdelanie: Vysok?? ??kola m??zick??ch umen?? v Bratislave, CERIS-ULB Diplomatic School of Brussels (CERIS), Sorbonne Universit??, Univerzita Cambridge
Funkcia: Podpredsedn????ka vl??dy Slovenskej republiky', '3');

INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('1', '1');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('2', '1');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('3', '1');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('1', '2');
INSERT INTO votuj.`candidate_has_term` (id_candidate, id_term) VALUES ('2', '2');
-- ------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO votuj.`category` (id, name) VALUES ('1', 'P??dohospod??rstvo');
INSERT INTO votuj.`category` (id, name) VALUES ('2', '??kolstvo');
INSERT INTO votuj.`category` (id, name) VALUES ('3', '??ivotn?? prostredie');
INSERT INTO votuj.`category` (id, name) VALUES ('4', 'Doprava');

INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('1', '724', 'Farm??ri dostan?? vopred ur??en?? sumu poistn??ho plnenia v pr??pade, ??e nastan?? podmienky poistnej udalosti. ', '3');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('2', '722', 'Podpor??me v??chovu mlad??ch po??nohospod??rov u?? v ??t??diu n??boru na u????ovsk?? ??koly a ich v??chovu v spolupr??ci s podnikmi v praxi.', '3');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('3', '562', 'Zv????ime napojenie dom??cnost?? na verejn?? kanaliz??ciu a ??istiarne odpadov??ch v??d o 10 %.', '3');


INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('4', '8', 'Navrhneme z??kaz predaja 1090 kotlov na tuh?? palivo 1. a 2. emisnej triedy, ktor?? sa najviac podie??aj?? na zne??isten?? ovzdu??ia. ', '1');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('5', '24', 'Zv????ime ekologick?? gramotnos?? obyvate??stva. Do u??ebn??ch osnov zavedieme vzdel??vanie o ??ivotnom prostred??.', '1');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('6', '4', 'Zmen??me smerovanie v??nosov z m??tneho syst??mu tak, ??e p??jde priamo jednotliv??m spr??vcom komunik??cie, za ktorej prejazd bol poplatok zaplaten??.', '1');


INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('7', '19', 'Zavedieme zn????en?? limity na ??erpanie zliav z m??tnych poplatkov, ????m podpor??me slovensk??ch dopravcov.', '2');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('8', '56', 'Umo??n??me z??kladn??m ??kol??m hodnoti?? ??iakov a ??tudentov bez pou??itia zn??mok, napr??klad slovn??m hodnoten??m.', '2');
INSERT INTO votuj.`item` (id, name, info, id_program) VALUES ('9', '32', 'Da??ovo zv??hodn??me ekologick?? motorov?? vozidl??. ', '2');

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
INSERT INTO `votuj`.`region` (`name`) VALUES ('Bratislavsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Trnavsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Tren??iansky kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Nitriansky kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('??ilinsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Banskobystrick?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Pre??ovsk?? kraj');
INSERT INTO `votuj`.`region` (`name`) VALUES ('Ko??ick?? kraj');
