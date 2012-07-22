SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE DATABASE MSDB CHARACTER SET utf8;
-- -----------------------------------------------------
-- Table `MSDB`.`question_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`question_type` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `name` VARCHAR(50) NULL ,
  `rendener` VARCHAR(100) NULL COMMENT '在界面展示的控件\n' ,
  `parent_id` VARCHAR(45) NULL COMMENT '父类别ID\n主要用于展示。\n可以继承父亲的rendener' ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '问题类型表，其记录包括\n选择\n填空\n问答' ;


-- -----------------------------------------------------
-- Table `MSDB`.`question`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`question` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `question_type_id` VARCHAR(32) NULL ,
  `name` VARCHAR(50) NULL ,
  `content` VARCHAR(2000) NULL ,
  `right_answer` VARCHAR(255) NULL COMMENT '答案' ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '问题表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`role`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`role` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `name` VARCHAR(50) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '角色表，此数据初始化后不可以修改\n' ;


-- -----------------------------------------------------
-- Table `MSDB`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`user` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `name` VARCHAR(45) NULL ,
  `code` VARCHAR(45) NULL ,
  `password` VARCHAR(45) NULL ,
  `loginname` VARCHAR(45) NULL ,
  `role_id` VARCHAR(32) NULL ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '用户表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`question_choice_item`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`question_choice_item` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `question_id` VARCHAR(32) NULL ,
  `label_name` VARCHAR(10) NULL ,
  `content` VARCHAR(255) NULL ,
  `idx` INT NULL ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '选择题选项' ;


-- -----------------------------------------------------
-- Table `MSDB`.`question_fill_item`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`question_fill_item` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `question_id` VARCHAR(32) NULL ,
  `display_length` INT NULL ,
  `index` INT NULL ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '填空题空格项\n' ;


-- -----------------------------------------------------
-- Table `MSDB`.`position`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`position` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `name` VARCHAR(45) NULL ,
  `next_position` VARCHAR(45) NULL COMMENT '高一级的职位' ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '职位表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`examination`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`examination` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `position_id` VARCHAR(32) NULL COMMENT '适用的职务ID' ,
  `name` VARCHAR(50) NULL COMMENT '试卷名称' ,
  `time` INT NULL COMMENT '时间，例如120分钟' ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '试卷表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`examination_catalog`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`examination_catalog` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `exam_id` VARCHAR(32) NULL COMMENT '试卷ID' ,
  `name` VARCHAR(50) NULL COMMENT '名称' ,
  `description` VARCHAR(100) NULL COMMENT '描述' ,
  `idx` INT NULL COMMENT '顺序' ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '试卷栏目表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`examination_question`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`examination_question` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `catalog_id` VARCHAR(32) NULL COMMENT '试卷栏目ID' ,
  `question_id` VARCHAR(32) NULL COMMENT '题目ID' ,
  `idx` INT NULL COMMENT '在栏目中的位置' ,
  `score` INT NULL COMMENT '分数' ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '试卷题目' ;


-- -----------------------------------------------------
-- Table `MSDB`.`interviewer`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`interviewer` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `id_code` VARCHAR(20) NULL COMMENT '省份证号' ,
  `name` VARCHAR(50) NULL ,
  `phone` VARCHAR(20) NULL ,
  `age` INT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '面试人表\n' ;


-- -----------------------------------------------------
-- Table `MSDB`.`interview`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`interview` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `start` TIMESTAMP NULL COMMENT '开始时间' ,
  `apply_position_id` VARCHAR(32) NULL COMMENT '应聘职位ID' ,
  `create_on` TIMESTAMP NULL ,
  `create_by` VARCHAR(50) NULL ,
  `status` VARCHAR(20) NULL COMMENT '状态，包括\n等待面试，\n正在面试，\n面试完成，\n缺席面试' ,
  `interviewer_id` VARCHAR(45) NULL COMMENT '面试人ID\n' ,
  `login_name` VARCHAR(50) NULL ,
  `login_password` VARCHAR(50) NULL ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = ' 面试表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`evaluate`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`evaluate` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `interview_id` VARCHAR(32) NULL COMMENT '面试人ID' ,
  `user_id` VARCHAR(20) NULL COMMENT '评价人' ,
  `comment` VARCHAR(255) NULL COMMENT '意见' ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '评价表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`examination_question_answer`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`examination_question_answer` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `interview_exam_id` VARCHAR(32) NULL COMMENT '面试面试ID' ,
  `exam_question_id` VARCHAR(32) NULL COMMENT '试卷题目ID' ,
  `answer` VARCHAR(255) NULL COMMENT '答案' ,
  `result` FLOAT NULL COMMENT '正确程度' ,
  `score` FLOAT NULL COMMENT '得分' ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '试卷答案' ;


-- -----------------------------------------------------
-- Table `MSDB`.`position_question`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`position_question` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `position_id` VARCHAR(32) NULL ,
  `question_id` VARCHAR(45) NULL ,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB, 
CHARACTER SET utf8,
COMMENT = '职位问题关联表' ;


-- -----------------------------------------------------
-- Table `MSDB`.`interview_examination`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `MSDB`.`interview_examination` (
  `ID` VARCHAR(32) NOT NULL ,
  `version` INT NULL ,
  `interview_id` VARCHAR(32) NULL ,
  `exam_id` VARCHAR(45) NULL ,
  `exam_score` INT NULL COMMENT '笔试分数\n',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB,
CHARACTER SET utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
