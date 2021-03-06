/**
 * 备份创表语句
 */
CREATE TABLE `b_educations` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `resumeId` varchar(20) NOT NULL,
  `startTime` varchar(50) NOT NULL,
  `endTime` varchar(50) NOT NULL,
  `school` varchar(500) NOT NULL,
  `specialty` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=640 DEFAULT CHARSET=utf8;

CREATE TABLE `b_projects` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `resumeId` varchar(20) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `startTime` varchar(50) DEFAULT NULL,
  `endTime` varchar(50) DEFAULT NULL,
  `projectDetails` varchar(1600) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1605 DEFAULT CHARSET=utf8;

CREATE TABLE `b_resume` (
  `resumeId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(10) DEFAULT NULL,
  `destination` varchar(50) DEFAULT NULL,
  `expectedSalary` varchar(10) DEFAULT NULL,
  `skills` varchar(500) DEFAULT NULL,
  `jobTitle` varchar(200) DEFAULT NULL,
  `jobYear` int(3) DEFAULT '0',
  `name` varchar(50) DEFAULT NULL,
  `sex` int(1) DEFAULT '0',
  `age` varchar(50) DEFAULT NULL,
  `education` int(1) DEFAULT '0',
  `summary` varchar(300) DEFAULT NULL,
  `details` varchar(1600) DEFAULT NULL,
  `productUrl` varchar(2500) DEFAULT NULL,
  `productImg` varchar(1600) DEFAULT NULL
  PRIMARY KEY (`resumeId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `b_workexp` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `resumeId` varchar(20) DEFAULT NULL,
  `startTime` varchar(50) DEFAULT NULL,
  `endTime` varchar(50) DEFAULT NULL,
  `companyName` varchar(200) DEFAULT NULL,
  `jobTitle` varchar(100) DEFAULT NULL,
  `workContent` varchar(800) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1423 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `talent_demand`;
CREATE TABLE `talent_demand` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `companyId` varchar(20) NOT NULL,
  `skills` varchar(50) NOT NULL,
  `website` varchar(500) DEFAULT NULL,
  `updateTime` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;


update special s,(select date_format(bidTime,'%Y-%m-%d %T') as t,id from special) a set s.bidTime=a.t where a.id=s.id;
update bid_logs s,(select date_format(bidTime,'%Y-%m-%d %T') as t,id from bid_logs) a set s.bidTime=a.t where a.id=s.id;
update bid_logs s,(select date_format(replyTime,'%Y-%m-%d %T') as t,id from bid_logs) a set s.replyTime=a.t where a.id=s.id;
update read_logs s,(select date_format(readTime,'%Y-%m-%d %T') as t,id from read_logs) a set s.readTime=a.t where a.id=s.id;
update resume s,(select date_format(checkTime,'%Y-%m-%d %T') as t,resumeId from resume) a set s.checkTime=a.t where a.resumeId=s.resumeId;
update resume s,(select date_format(creatTime,'%Y-%m-%d %T') as t,resumeId from resume) a set s.creatTime=a.t where a.resumeId=s.resumeId;
update resume s,(select date_format(updateTime,'%Y-%m-%d %T') as t,resumeId from resume) a set s.updateTime=a.t where a.resumeId=s.resumeId;
update resume s,(select date_format(commitTime,'%Y-%m-%d %T') as t,resumeId from resume) a set s.commitTime=a.t where a.resumeId=s.resumeId;
update resume s,(select date_format(shelvesTime,'%Y-%m-%d %T') as t,resumeId from resume) a set s.shelvesTime=a.t where a.resumeId=s.resumeId;
update special_logs s,(select date_format(bidTime,'%Y-%m-%d %T') as t,id from special_logs) a set s.bidTime=a.t where a.id=s.id;
update special_logs s,(select date_format(collectTime,'%Y-%m-%d %T') as t,id from special_logs) a set s.collectTime=a.t where a.id=s.id;
update talent_demand s,(select date_format(updateTime,'%Y-%m-%d %T') as t,id from talent_demand) a set s.updateTime=a.t where a.id=s.id;

alter table bid_logs add column compUserId varchar(20) default null;
update bid_logs set compUserId=companyId;
alter table bid_logs add column acquireTime varchar(50) default null;
alter table rigistration add column activaCode varchar(50) default null;
alter table talent_demand add column minSalary int(3) default 0;
alter table talent_demand add column maxSalary int(3) default 0;
alter table bid_logs add column minBidPrice int(3) default 0;
alter table bid_logs add column maxBidPrice int(3) default 0;
update bid_logs set minBidPrice=round(bidPrice)-2,maxBidPrice=round(bidPrice)+2;
alter table resume add column isNextBide int(1) default 0;
alter table bid_logs add column isAutomatic int(1) default 0;
alter table resume change isOxcoder isSendEmail int(1) default 0;
alter table bid_logs drop column bidPrice;
alter table bid_logs drop column monthCount;
alter table resume drop column telephone;
alter table resume drop column expectedProvince;
alter table resume drop column nowSalary;
alter table resume drop column languages;
alter table resume drop column certificates;
alter table resume drop column educations;
alter table resume drop column entryTime;
alter table resume drop column province;
alter table resume drop column city;
alter table resume drop column idCard;
alter table resume drop column companyProgress;
alter table resume drop column companySize;
alter table resume drop column companyArea;
alter table resume drop column userEmail;
alter table work_experiences drop column companyArea;
alter table work_experiences drop column companyType;
alter table work_experiences drop column companySize;
alter table projects drop column softwares;
alter table projects drop column hardwares;
alter table projects drop column responsibilities;
alter table projects drop column applicationTechnology;
alter table bid_logs drop column optionStart;
alter table bid_logs drop column optionEnd;
alter table b_resume drop column isOxcoder;

alter table rigistration add column gold int(10) default 0;
alter table company add column telephone varchar(20) default null;
alter table company add column extension varchar(20) default null;


alter table suggestion add column status int(1) default 0;
alter table suggestion add column email varchar(200) default null;



DROP TABLE IF EXISTS `qr_code`;
CREATE TABLE `qr_code` (
  `userId` int(10) NOT NULL,
  `url` varchar(100) NOT NULL,
  `ticket` varchar(200) NOT NULL,
  `creatTime` varchar(20) NOT NULL,
  `status` int(1) default 0,
  `price` int(10) default 0,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bonus_logs`;
CREATE TABLE `bonus_logs` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `userId` int(10) NOT NULL,
  `price` int(10) NOT NULL,
  `openid` varchar(100) NOT NULL,
  `listid` varchar(100) NOT NULL,
  `mch_billno` varchar(100) NOT NULL,
  `sendTime` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=640 DEFAULT CHARSET=utf8;

/**
 * 请他出价
 */
DROP TABLE IF EXISTS `forward_resume`;
CREATE TABLE `forward_resume` (
  `id` varchar(100) NOT NULL,
  `companyUser` int(10) NOT NULL,
  `companyId` int(10) NOT NULL,
  `userId` int(10) NOT NULL,
  `toEmail` varchar(50) NOT NULL,
  `message` varchar(100) NOT NULL,
  `creatTime` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/**
 * 充值记录
 */
DROP TABLE IF EXISTS `recharge_log`;
CREATE TABLE `recharge_log` (
  `out_trade_no` varchar(100) NOT NULL,
  `trade_no` varchar(100) NOT NULL,
  `total_fee` int(10) NOT NULL,
  `gold` int(10) NOT NULL,
  `status` int(1) DEFAULT 1,
  `subject` varchar(250) NOT NULL,
  `gmt_create` varchar(50) NOT NULL,
  `gmt_payment` varchar(50) NOT NULL,
  `buyer_email` varchar(250) NOT NULL,
  `compId` int(10) NOT NULL,
  `compUserId` int(10) NOT NULL,
  PRIMARY KEY (`out_trade_no`),
  KEY `compId` (`compId`),
  KEY `compUserId` (`compUserId`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 邀请记录
 */
DROP TABLE IF EXISTS `comp_invite_logs`;
CREATE TABLE `comp_invite_logs` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userId` int(10) NOT NULL,
  `compId` int(10) NOT NULL,
  `compUserId` int(10) NOT NULL,
  `inviteTime` varchar(50) NOT NULL,
  `status` int(1) DEFAULT 0,
  `replyTime` varchar(50) DEFAULT NULL,
  `isOption` int(1) NOT NULL,
  `minBidPrice` int(10) NOT NULL,
  `maxBidPrice` int(10) NOT NULL,
  `gold` int(10) NOT NULL,
  `message` varchar(200) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `isRead` int(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `compId` (`compId`),
  KEY `compUserId` (`compUserId`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 邀请阅读记录
 */
DROP TABLE IF EXISTS `invite_read_logs`;
CREATE TABLE `invite_read_logs` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `compUserId` int(10) NOT NULL,
  `userId` int(10) NOT NULL,
  `status` int(1) DEFAULT 0,
  `readTime` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `compUserId` (`compUserId`),
  KEY `userId` (`userId`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/**
 * 注册存储过程
 */
DROP PROCEDURE IF EXISTS user_register;
create procedure user_register(
	in i_email varchar(50),
	in i_pwd varchar(50),
	in i_status int,
	in i_telephone varchar(11),
	in i_name varchar(500),
	in i_jobTitle varchar(500),
	in i_skills varchar(500),
	in i_destination varchar(50),
	in i_inviteCode varchar(10),
	in i_activaCode varchar(50),
	out resultNumber int
)
begin
	declare v_user_count int;
	declare v_user_id int;
	declare v_invite_id int;
	declare v_invite_count int;
	declare v_resume_count int;
	select count(*) into v_user_count from rigistration where email=i_email;
	if v_user_count>0 then
		set resultNumber = 0;
	else
		if i_inviteCode is not null then 
			select id into v_invite_id from invite where inviteCode=i_inviteCode;
			select count(*) into v_invite_count from invite_logs where email=i_email;
			if v_invite_id>0 and v_invite_count<1 then
				insert into invite_logs set inviteId=v_invite_id,email=i_email,inviteTime=NOW();
			end if;
		end if;
		insert into rigistration set email=i_email,password=i_pwd,status=i_status,isNormal=1,rigistraTime=NOW(),name=i_name,jobTitle=i_jobTitle,telephone=i_telephone,inviteId=v_invite_id,activaCode=I_activaCode;
		if row_count()>0 then
			set resultNumber = 1;
		end if;
		if i_status=0 then 
			select userId into v_user_id from rigistration where email=i_email limit 1;
			select count(*) into v_resume_count from resume where userId=v_user_id;
			if v_resume_count<1 then
				if v_user_id>0 then
					insert into resume set userId=v_user_id,jobYear=0,surplusTime=0,cvBidStatus=0,cvCheckResult=0,cvCheckStatus=0,bidCount=0,special='0',specialCount='0',sex=0,education=0,skills=i_skills,destination=i_destination,creatTime=NOW();
				end if;
			end if;
		end if;
	end if;
end;


/**
 * 简历提交审核 存储过程
 */
DROP PROCEDURE IF EXISTS user_backup;
create procedure user_backup(
	in i_resumeId varchar(50),
	in i_userId varchar(50),
	in i_commitTime varchar(50),
	out resultNumber varchar(1)
)
begin
	update resume set updateTime=NOW(),status=1,isOnline=1,isHired=0,special=0,specialCount=0,cvCheckResult=0,cvCheckStatus=0,cvBidStatus=0,commitTime=NOW(),repeatBidTime=i_commitTime,bidCount=bidCount+1 where userId=i_userId;
	if row_count()>0 then 
		delete from b_resume where resumeId=i_resumeId;
		delete from b_educations where resumeId=i_resumeId;
		delete from b_projects where resumeId=i_resumeId;
		delete from b_workexp where resumeId=i_resumeId;
		insert into b_resume (resumeId,userId,destination,expectedSalary,skills,jobTitle,jobYear,name,sex,age,education,summary,details,productUrl,productImg) 
			select resumeId,userId,destination,expectedSalary,skills,jobTitle,jobYear,name,sex,age,education,summary,details,productUrl,productImg from resume where resumeId=i_resumeId;
		insert into b_workexp (resumeId,startTime,endTime,companyName,jobTitle,workContent) 
			select resumeId,startTime,endTime,companyName,jobTitle,workContent from work_experiences where resumeId=i_resumeId;
		insert into b_projects (resumeId,name,startTime,endTime,projectDetails) 
			select resumeId,name,startTime,endTime,projectDetails from projects where resumeId=i_resumeId;
		insert into b_educations (resumeId,startTime,endTime,school,specialty) 
			select resumeId,startTime,endTime,school,specialty from educations where resumeId=i_resumeId;
		set resultNumber = '1';
	else 
		set resultNumber = '0';
	end if;
end;


/**
 * 更新求职意向 （不更新简历状态）
 */
DROP PROCEDURE IF EXISTS user_update_itent;
create procedure user_update_itent(
	in i_expectedSalary varchar(50),
	in i_destination varchar(50),
	in i_content varchar(50),
	in i_userId varchar(50),
	out resultNumber varchar(1)
)
begin
	set resultNumber = '0';
	if i_expectedSalary is not null then
		update resume set expectedSalary=i_expectedSalary where userId=i_userId;
		set resultNumber = '1';
	end if;
	if i_destination is not null then
		update resume set destination=i_destination where userId=i_userId;
		set resultNumber = '1';
	end if;
	if i_content is not null then
		delete from shield_logs where userId=i_userId;
		insert into shield_logs (userId,content,shieldTime) values(i_userId,i_content,NOW());
		set resultNumber = '1';
	end if;
end;


/**
 * 更新求职意向（更改简历重新上传）
 */
DROP PROCEDURE IF EXISTS user_resubmit_check;
create procedure user_resubmit_check(
	in i_expectedSalary varchar(50),
	in i_destination varchar(50),
	in i_jobTitle varchar(50),
	in i_skills varchar(50),
	in i_content varchar(50),
	in i_userId varchar(50),
	out resultNumber varchar(1)
)
begin
	declare sqlstr varchar(1000) DEFAULT '';
	set resultNumber = '0';
	set sqlstr = concat(sqlstr,'update resume set isHired=0,status=2,cvBidStatus=0,cvCheckStatus=0,cvCheckResult=0');
	if i_expectedSalary is not null then
		set sqlstr = concat(sqlstr,',expectedSalary=',i_expectedSalary);
	end if;
	
	if i_destination is not null then
		set sqlstr = concat(sqlstr,",destination='",i_destination,"'");
	end if;
	
	if i_jobTitle is not null then
		set sqlstr = concat(sqlstr,",jobTitle='",i_jobTitle,"'");
	end if;
	
	if i_skills is not null then
		set sqlstr = concat(sqlstr,",skills='",i_skills,"'");
	end if;
	
	set sqlstr = concat(sqlstr,' where userId=',i_userId,';');
	set @v_sql = sqlstr;
	prepare stmt from @v_sql;
	EXECUTE stmt;
	deallocate prepare stmt;
	set resultNumber = '1';
	if i_content is not null then
		delete from shield_logs where userId=i_userId;
		insert into shield_logs (userId,content,shieldTime) values(i_userId,i_content,NOW());
		set resultNumber = '1';
	end if;
end;


/**
 * 候选人删除（公共）存储过程
 */
DROP PROCEDURE IF EXISTS resume_base_del;
create procedure resume_base_del(
	in i_tabelName varchar(50),
	in i_userId varchar(50),
	in i_id varchar(100),
	out resultNumber int
)
begin
	declare v_resumeId varchar(50);
	declare sqlstr varchar(1000) DEFAULT '';
	set resultNumber = '0';
	select resumeId into v_resumeId from resume where userId=i_userId;
	if v_resumeId is not null then
		set sqlstr = concat(sqlstr,"delete from ",i_tabelName," where resumeId = ",v_resumeId," and id=",i_id);
		set @v_sql = sqlstr;
		prepare stmt from @v_sql;
		EXECUTE stmt;
		deallocate prepare stmt;
		set resultNumber = '1';
	end if;
end;

/**
 * 简历再次参加拍卖，不需要审核
 */
DROP PROCEDURE IF EXISTS resume_direct_Bid;
create procedure resume_direct_Bid(
	in i_userId varchar(50),
	out resultNumber int
)
begin
	declare v_bidStatus int(1);
	declare v_special varchar(10);
	declare v_specialCount varchar(10);
	set resultNumber = '0';
	select cvBidStatus into v_bidStatus from resume where userId=i_userId;
	if v_bidStatus=2 then
		select special into v_special from resume where userId=i_userId;
		select specialCount into v_specialCount from special where id=v_special;
		if v_specialCount is not null then
			update resume set updateTime=NOW(),cvBidStatus=3,isHired=0,specialCount=v_specialCount,commitTime=NOW() where userId=i_userId;
			if row_count()>0 then
				set resultNumber ='1';
			end if;
		end if;
	end if;
end;

/**
 * 公司竞拍
 */
DROP PROCEDURE IF EXISTS company_auction;
create procedure company_auction(
	in i_userId varchar(50),
	in i_compUserId varchar(50),
	in i_companyId varchar(50),
	in i_minBidPrice int(3),
	in i_maxBidPrice int(3),
	in i_isOption int(1),
	in i_workTitle varchar(1600),
	in i_bidCount int(3),
	in i_special int(5),
	in i_specialCount int(5),
	out resultNumber int
)
begin
	declare v_bid_count int(1) DEFAULT 0;
	declare v_bid_status int(1) DEFAULT 0;
	declare v_read_count int(10);
	set resultNumber = 0;
	select count(*) into v_bid_status from resume where userId=i_userId and cvBidStatus=1;
	if v_bid_status > 0 then 
		select count(*) into v_bid_count from bid_logs where userId=i_userId and companyId=i_companyId;
		if v_bid_count<2 then
			select count(*) into v_read_count from read_logs where userId=i_userId and companyId=i_companyId;
			if v_read_count>0 then
				update read_logs set status=1,readTime=NOW() where userId=i_userId and companyId=i_companyId;
			else
				insert into read_logs (userId,companyId,status,readTime) values (i_userId,i_companyId,1,NOW());
			end if;
			insert into bid_logs (userId,compUserId,companyId,minBidPrice,maxBidPrice,isOption,workTitle,bidTime,bidCount,special,specialCount,isReply) 
			values (i_userId,i_compUserId,i_companyId,i_minBidPrice,i_maxBidPrice,i_isOption,i_workTitle,NOW(),i_bidCount,i_special,i_specialCount,0);
			set resultNumber = 1;
		else
			set resultNumber = 2;
		end if;
	end if;
end;

/**
 * 新增公司
 */
DROP PROCEDURE IF EXISTS add_company;
create procedure add_company(
	in i_userId varchar(50),
	in i_name varchar(200),
	out resultNumber int
)
begin
	declare v_num int(10);
	set resultNumber = 0;
	select count(*) into v_num from company where companyId=i_userId;
	if v_num<1 then
		insert into company (companyId,name,creatTime) values (i_userId,i_name,NOW());
		update rigistration set companyId=i_userId where userId=i_userId;
		set resultNumber =1;
	end if;
end;

/**
 * 微信号绑定
 */
DROP PROCEDURE IF EXISTS wechat_bind;
create procedure wechat_bind(
	in i_userId varchar(50),
	in i_openId varchar(50),
	out resultNumber int
)
begin
	declare v_num int(10);
	set resultNumber = 0;
	select count(*) into v_num from rigistration where openId=i_openId;
	if v_num<1 then
		update rigistration set openId=i_openId where userId=i_userId;
		if row_count()>0 then
			set resultNumber =1;
		end if;
	else 
		update rigistration set openId=null where openId=i_openId;
		update rigistration set openId=i_openId where userId=i_userId;
		if row_count()>0 then
			set resultNumber =1;
		end if;
	end if;
end;

/**
 * 继续参加拍卖
 */
DROP PROCEDURE IF EXISTS continue_bid;
create procedure continue_bid(
	in i_userId varchar(50),
	out resultNumber int
)
begin
	declare v_num int(1);
	set resultNumber = 0;
	select count(*) into v_num from resume where cvBidStatus=1 and userId=i_userId and isNextBide=0;
	if v_num>0 then
		update resume set isNextBide=1 where userId=i_userId;
		set resultNumber = 1;
	end if;
end;

/**
 * 添加公司产品信息
 */
DROP PROCEDURE IF EXISTS add_products;
create procedure add_products(
	in i_companyId varchar(20),
	in i_productName varchar(300),
	in i_imgName varchar(50),
	in i_progress int(1),
	in i_website varchar(200),
	in i_brief varchar(800),
	out resultNumber int
)
begin
	declare v_num int(1);
	set resultNumber = 0;
	select count(*) into v_num from products where companyId=i_companyId;
	if v_num<3 then
		insert into products (
        	companyId,productName,imgName,progress,website,brief
        ) values (
        	i_companyId,i_productName,i_imgName,i_progress,i_website,i_brief 
        );
       set resultNumber=1;
	end if;
end;

/**
 * 添加公司团队信息
 */
DROP PROCEDURE IF EXISTS add_founder;
create procedure add_founder(
	in i_companyId varchar(20),
	in i_imgName varchar(50),
	in i_name varchar(100),
	in i_jobName varchar(300),
	in i_website varchar(200),
	in i_brief varchar(800),
	out resultNumber int
)
begin
	declare v_num int(1);
	set resultNumber = 0;
	select count(*) into v_num from founder where companyId=i_companyId;
	if v_num<3 then
		insert into founder (
        	companyId,jobName,name,imgName,website,brief
        ) values (
        	i_companyId,i_jobName,i_name,i_imgName,i_website,i_brief
        );
       set resultNumber=1;
	end if;
end;

/**
 * 删除人才招聘需求
 */
DROP PROCEDURE IF EXISTS del_talent_demand;
create procedure del_talent_demand(
	in i_companyId varchar(20),
	in i_id varchar(50),
	out resultNumber int
)
begin
	set resultNumber = 0;
	delete from talent_demand where companyId=i_companyId and id=i_id;
	if row_count()>0 then
		set resultNumber = 1;
		delete from match_talent where companyId=i_companyId and demandId=i_id;
	end if;
end;

/**
 * 简历下架
 */
DROP PROCEDURE IF EXISTS resume_off_shelves;
create procedure resume_off_shelves(
	in i_userId varchar(20),
	in i_openId varchar(50),
	out resultNumber int
)
begin
	set resultNumber = 0;
	if i_userId is not null then 
		update resume set cvBidStatus=2,offShelvesTime=NOW() where cvBidStatus=1 and userId=i_userId;
	else
		update resume set cvBidStatus=2,offShelvesTime=NOW() where cvBidStatus=1 and userId=(select userId from rigistration where openId=i_openId);
	end if;
	if row_count()>0 then
		set resultNumber = 1;
		if i_userId is not null then
			delete from match_talent WHERE resumeId = (select resumeId from resume where userId=i_userId);
		else
			delete from match_talent WHERE resumeId = (select resumeId from resume where userId=(select userId from rigistration where openId=i_openId));
		end if;
	end if;
end;

/**
 *红包发送成功
 */
DROP PROCEDURE IF EXISTS send_bonus;
create procedure send_bonus(
	in i_userId varchar(10),
	in i_openid varchar(100),
	out resultNumber int
)
begin
	declare v_price int(10);
	declare v_num int;
	set resultNumber = 0;
	select price into v_price from qr_code where userId=i_userId and status=0;
	select count(*) into v_num from bonus_logs where openid=i_openid;
	if v_num<1 then
		if v_price is not null then
			update qr_code set status=1 where userId=i_userId;
			set resultNumber = v_price;
		end if;
	end if;
end;

/**
 *红包发送失败
 */
DROP PROCEDURE IF EXISTS send_bonus_failure;
create procedure send_bonus_failure(
	in i_userId varchar(10),
	out resultNumber int
)
begin
	set resultNumber = 0;
	update qr_code set status=0 where userId=i_userId;
	if row_count()>0 then
		delete from bonus_logs where userId=i_userId;
		set resultNumber = 1;
	end if;
end;

/**
 * 邀请出价
 */
DROP PROCEDURE IF EXISTS add_forward_resume;
create procedure add_forward_resume(
	in i_id varchar(100),
	in i_companyUser int,
	in i_companyId int,
	in i_userId int,
	in i_toEmail varchar(100),
	in i_message varchar(100),
	out resultNumber int
)
begin
	declare v_bid_status int(1) default 0;
	declare v_comp_status int(1) default 0;
	set resultNumber = 0;
	select count(*) into v_bid_status from resume where userId=i_userId and cvBidStatus=1;
	if v_bid_status>0 then
		select isSaveUserInfo into v_comp_status from rigistration where userId=i_companyUser;
		if v_comp_status>0 then
			insert into forward_resume (id,companyUser,companyId,userId,toEmail,message,creatTime) 
			values (i_id,i_companyUser,i_companyId,i_userId,i_toEmail,i_message,NOW());
			set resultNumber = 1;
		else
			set resultNumber = 3;
		end if;
	else
		set resultNumber = 2;
	end if;
end;

/**
 * 添加阅读纪录
 */
DROP PROCEDURE IF EXISTS add_read_logs;
create procedure add_read_logs(
	in i_userId int,
	in i_companyId int,
	out resultNumber int
)
begin
	declare v_read_count int(1) default 0;
	set resultNumber = 0;
	select count(*) into v_read_count from read_logs where userId=i_userId and companyId=i_companyId;
	if v_read_count<1 then
		insert into read_logs (userId,companyId,status,readTime) values (i_userId,i_companyId,0,NOW());
	end if;
end;


/**
 * 公司竞拍
 */
DROP PROCEDURE IF EXISTS interview_invite;
create procedure interview_invite(
	in i_userId varchar(50),
	in i_compUserId varchar(50),
	in i_companyId varchar(50),
	in i_minBidPrice int(3),
	in i_maxBidPrice int(3),
	in i_isOption int(1),
	in i_workTitle varchar(1600),
	out resultNumber int
)
begin
	declare v_bid_count int(1) DEFAULT 0;
	declare v_bid_status int(1) DEFAULT 0;
	declare v_bid int(1) DEFAULT 0;
	declare v_read_count int(10);
	declare v_gold int(10);
	set resultNumber = 0;
	select count(*) into v_bid_status from resume where userId=i_userId and cvCheckResult=2;
	if v_bid_status > 0 then 
		select gold into v_gold from rigistration where userId=i_compUserId;
		if v_gold >= fn_gold_price() then
			select count(*) into v_bid_count from comp_invite_logs where userId=i_userId and compUserId=i_compUserId and status=0;
			if v_bid_count<1 then
				select count(*) into v_bid from comp_invite_logs where userId=i_userId and compUserId=i_compUserId;
				if v_bid<2 then
					select count(*) into v_read_count from invite_read_logs where userId=i_userId and compUserId=i_compUserId;
					if v_read_count>0 then
						update invite_read_logs set status=1,readTime=NOW() where userId=i_userId and compUserId=i_compUserId;
					else
						insert into invite_read_logs (userId,compUserId,status,readTime) values (i_userId,i_compUserId,1,NOW());
					end if;
					insert into comp_invite_logs (userId,compUserId,compId,minBidPrice,maxBidPrice,isOption,message,inviteTime,gold) 
					values (i_userId,i_compUserId,i_companyId,i_minBidPrice,i_maxBidPrice,i_isOption,i_workTitle,NOW(),fn_gold_price());
					if row_count()>0 then
						update rigistration set gold=(gold-fn_gold_price()) where userId=i_compUserId;
					end if;
					set resultNumber = 1;
				else
					set resultNumber = 3;
				end if;
			else
				set resultNumber = 2;
			end if;
		else 
			set resultNumber = 4;
		end if;
	end if;
end;

/**
 * 添加阅读纪录
 */
DROP PROCEDURE IF EXISTS invite_read_logs;
create procedure invite_read_logs(
	in i_userId int,
	in i_compUserId int,
	out resultNumber int
)
begin
	declare v_read_count int(1) default 0;
	set resultNumber = 0;
	select count(*) into v_read_count from invite_read_logs where userId=i_userId and compUserId=i_compUserId;
	if v_read_count<1 then
		insert into invite_read_logs (userId,compUserId,status,readTime) values (i_userId,i_compUserId,0,NOW());
	end if;
end;

/**
 * 充值
 */
DROP PROCEDURE IF EXISTS recharge;
create procedure recharge(
	in i_out_trade_no varchar(100),
	in i_trade_no varchar(100),
	in i_total_fee int(10),
	in i_gold int(10),
	in i_subject varchar(250),
	in i_gmt_create varchar(50),
	in i_gmt_payment varchar(50),
	in i_buyer_email varchar(100),
	in i_compId varchar(100),
	in i_compUserId varchar(100),
	out resultNumber int
)
begin
	declare v_total_fee int(10);
	declare v_count int(10) default 0;
	select count(*) into v_count from recharge_log where out_trade_no=i_out_trade_no;
	if v_count<1 then 
		insert into recharge_log (out_trade_no,trade_no,total_fee,gold,status,subject,gmt_create,gmt_payment,buyer_email,compId,compUserId) 
		values (i_out_trade_no,i_trade_no,i_total_fee,i_gold,1,i_subject,i_gmt_create,i_gmt_payment,i_buyer_email,i_compId,i_compUserId);
		if row_count()>0 then
			update rigistration set gold= (gold+i_gold) where userId=i_compUserId;
			set resultNumber =1;
		end if;
	end if;
end;

DROP PROCEDURE IF EXISTS is_recharge;
create procedure is_recharge(
	in i_compUserId varchar(100),
	out resultNumber int
)
begin
	declare v_recharge_log int(10) default 0;
	declare v_count int(10) default 0;
	set resultNumber = 0;
	select count(*) into v_recharge_log from recharge_log where compUserId=i_compUserId;
	if v_recharge_log>0 then
		set resultNumber =1;
	else
		select count(*) into v_count from (select compUserId,count(*) from recharge_log group by compUserId) a;
		if v_count<20 then 
			set resultNumber =1;
		end if;
	end if;
end;


DROP PROCEDURE IF EXISTS is_load;
create procedure is_load(
	in i_compUserId varchar(100),
	out resultNumber int
)
begin
	declare v_gold int(10) default 0;
	declare v_count int(10) default 0;
	set resultNumber = 0;
	select count(*) into v_count from comp_invite_logs where compUserId=i_compUserId;
	if v_count>0 then
		set resultNumber =1;
	else
		select gold into v_gold from rigistration where userId=i_compUserId;
		if v_gold >= fn_gold_price() then 
			set resultNumber =1;
		end if;
	end if;
end;

/**
 * 判断是否下载简历
 */
DROP PROCEDURE IF EXISTS is_download;
create procedure is_download(
	in i_compUserId varchar(100),
	in i_userId varchar(100),
	out resultNumber int
)
begin
	declare v_invite_count int(10) default 0;
	declare v_bid_count int(10) default 0;
	set resultNumber = 0;
	select count(*) into v_bid_count from bid_logs where i_userId=i_userId and compUserId=i_compUserId and isReply=3;
	if v_bid_count>0 then
		set resultNumber =1;
	else
		select count(*) into v_invite_count from comp_invite_logs where compUserId=i_compUserId and userId=i_userId and status=1;
		if v_invite_count>0 then 
			set resultNumber =1;
		end if;
	end if;
end;


DROP PROCEDURE IF EXISTS add_suggestion;
create procedure add_suggestion(
	in i_status int(1),
	in i_email varchar(100),
	out resultNumber int
)
begin
	declare v_count int(10) default 0;
	select count(*) into v_count from suggestion where email=i_email;
	if v_count<1 then
		insert into suggestion (time,status,email) values (NOW(),i_status,i_email);
	end if;
end;




/**
 * 全文检索
 */
drop procedure if exists one_match_talent_pro;
create PROCEDURE one_match_talent_pro(IN demandId_pro INT, IN companyId_pro INT, IN minJobYear_pro INT,IN maxJobYear_pro INT,
	IN minSalary_pro INT, IN maxSalary_pro INT, IN email_pro VARCHAR(200),IN matchField_pro VARCHAR(500))
BEGIN
	DELETE FROM match_talent WHERE demandId=demandId_pro;
	INSERT INTO match_talent(resumeId,demandId,companyId,weight,yearWeight,salaryWeight,jobWeight)
	(SELECT i.resumeId,demandId_pro,companyId_pro,(jobWeight*0.7+yearWeight*0.2+salaryWeight*0.1) AS weight,
		i.yearWeight,i.salaryWeight,i.jobWeight
		FROM (
			SELECT resumeId,expectedSalary,fn_salaryWeight(expectedSalary,10,15) AS salaryWeight,
			jobYear,fn_yearWeight(jobYear,0,5) AS yearWeight,
			fn_matchWeight(MATCH(skills) AGAINST(@queryCondition),MATCH(jobTitle) AGAINST(@queryCondition),MATCH(details) AGAINST(@queryCondition)) as jobWeight
			FROM resume_index WHERE MATCH(skills,jobTitle,details) AGAINST(@queryCondition)
			AND jobYear<=ifnull(5,30) AND jobYear>=ifnull(0,0)
			AND expectedSalary<=ifnull(20,30) AND expectedSalary>=ifnull(5,0)
			AND userId NOT IN(SELECT distinct userId FROM shield_logs WHERE content LIKE concat('%','qq.com','%'))
		) i
		JOIN (SELECT * FROM resume where cvBidStatus=1) r
		ON i.resumeId=r.resumeId
		WHERE (jobWeight*0.7+yearWeight*0.2+salaryWeight*0.1)>=0.5);
END;


-- 工作年限权重
DROP FUNCTION IF EXISTS fn_yearWeight;
CREATE FUNCTION fn_yearWeight(jobYear DOUBLE,minJobYear DOUBLE,maxJobYear DOUBLE) RETURNS DOUBLE
begin
	DECLARE yearWeight DOUBLE;  
	set jobYear=ifnull(jobYear,0);
	set minJobYear=ifnull(minJobYear,0);
	set maxJobYear=ifnull(maxJobYear,10);
	IF jobYear<(minJobYear-1) THEN
		SET yearWeight=0.3;
	ELSEIF jobYear<minJobYear THEN
		SET yearWeight=0.5;
	ELSEIF jobYear>maxJobYear THEN
		SET yearWeight=EXP(-(jobYear-LN(5)))+0.8;
	ELSE
		SET yearWeight=jobYear*0.2/(maxJobYear-minJobYear)+((4*maxJobYear-5*minJobYear)*0.2/(maxJobYear-minJobYear));
	END IF;
	RETURN(yearWeight);
END;

-- 薪资权重
DROP FUNCTION IF EXISTS fn_salaryWeight;
CREATE FUNCTION fn_salaryWeight(expectedSalary DOUBLE,minSalary DOUBLE,maxSalary DOUBLE) RETURNS DOUBLE
begin
	DECLARE salaryWeight DOUBLE;  
	set expectedSalary=ifnull(expectedSalary,0);
	set minSalary=ifnull(minSalary,0);
	set maxSalary=ifnull(maxSalary,10);
	SET salaryWeight=(maxSalary-minSalary);
	IF expectedSalary<minSalary THEN
		IF minSalary<6 THEN
			SET salaryWeight=expectedSalary/(minSalary*minSalary);
		ELSE
			IF expectedSalary<(minSalary-6) THEN
				SET salaryWeight=0;
			ELSE 
				SET salaryWeight=expectedSalary*expectedSalary/(6*minSalary)+((6-minSalary)*expectedSalary)/(6*minSalary);
			END IF;
		END IF;
	ELSEIF expectedSalary>maxSalary THEN
		SET salaryWeight=EXP(-(expectedSalary+LN(0.8)));
	ELSE
		SET salaryWeight=expectedSalary*0.2/(minSalary-maxSalary)+((4*minSalary-5*maxSalary)*0.2/(minSalary-maxSalary));
	END IF;
	RETURN(salaryWeight);
END;

-- 职位权重
DROP FUNCTION IF EXISTS fn_matchWeight;
CREATE FUNCTION fn_matchWeight(skills DOUBLE,jobTitle DOUBLE,detail DOUBLE) RETURNS DOUBLE
begin
	DECLARE matchWeight DOUBLE default 0;  
	IF skills>=1 THEN 
		SET skills=1;
	ELSE
		SET skills=0;
	END IF;
	IF jobTitle>=1 THEN 
		SET jobTitle=1;
	ELSE
		SET jobTitle=0;
	END IF;
	IF detail>=1 THEN 
		SET detail=1;
	ELSE
		SET detail=0;
	END IF;
	SET matchWeight=skills*0.5+jobTitle*0.4+detail*0.1;
	RETURN(matchWeight);
END

-- 人才匹配，用户专场开启
drop procedure if exists batch_match_talent_pro;
create PROCEDURE batch_match_talent_pro()
BEGIN
	--定义变量  
	DECLARE demandId_pro int;  
	DECLARE companyId_pro int;  
	DECLARE minJobYear_pro int; 
	DECLARE maxJobYear_pro int; 
	DECLARE minSalary_pro int; 
	DECLARE maxSalary_pro int; 
	DECLARE email_pro VARCHAR(30); 
	DECLARE matchField_pro VARCHAR(500); 
	--这个用于处理游标到达最后一行的情况  
  DECLARE s int DEFAULT 0; 
	--声明游标cursor_name（cursor_name是个多行结果集）  
  DECLARE match_talent_name CURSOR FOR SELECT id,companyId,minJobYear,maxJobYear,minSalary,maxSalary,email,matchField FROM talent_demand;
	--设置一个终止标记   
  declare continue handler FOR SQLSTATE '02000' SET s=1; 
	--打开游标  
  OPEN match_talent_name;  
	--获取游标当前指针的记录，读取一行数据并传给变量
	fetch  match_talent_name into demandId_pro,companyId_pro,minJobYear_pro,maxJobYear_pro,minSalary_pro,maxSalary_pro,email_pro,matchField_pro;  
  --开始循环，判断是否游标已经到达了最后作为循环条件 
	while s <> 1 do  
		--读取下一行的数据  
		fetch  match_talent_name into demandId_pro,companyId_pro,minJobYear_pro,maxJobYear_pro,minSalary_pro,maxSalary_pro,email_pro,matchField_pro; 
		CALL one_match_talent_pro(demandId_pro,companyId_pro,minJobYear_pro,maxJobYear_pro,minSalary_pro,maxSalary_pro,email_pro,matchField_pro);
  end while;  
	--关闭游标  
  CLOSE match_talent_name ;  
  --语句执行结束  
END;




