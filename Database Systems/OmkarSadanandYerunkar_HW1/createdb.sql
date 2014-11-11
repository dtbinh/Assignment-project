--CREATE TABLE
CREATE TABLE Users
(
  u_id int NOT NULL,
  user_type varchar(20),
  registration_date timestamp,
  email varchar(20),
  first_name varchar(10),
  last_name varchar(10),
  country varchar(10),
  zip number(5), 
  Primary key (u_id)
  );
  

Create table Group1
(
group_name varchar(255),
g_id int not null,
abbrev varchar(20),
primary key(g_id)
);

Create table GroupJoin
(
group_name varchar(255),
g_id int not null,
datetime timestamp,
u_id int,
primary key(g_id,u_id),
foreign key (u_id) references Users(u_id)
);

Create table Company
(
comp_id int not null,
company_name varchar(20),
primary key(comp_id)
);

Create table CompanyFollow
(
comp_id int not null,
datetime timestamp,
u_id int,
primary key(comp_id,u_id),
foreign key (u_id) references Users(u_id)
);



Create table PostShare
(
p_id int not null,
post_type varchar(20),
p_content varchar(255),
share_type varchar(20),
like_count number(3),
comment_count number(3),
datetime TIMESTAMP,
sender_id int,
reciever_id int,
primary key (p_id)
);



Create table CommentHas
(
c_id int not null,
c_content varchar(255),
is_like int,
is_shared int,
p_id int,
sender_id int,
foreign key (p_id) references PostShare(p_id),
primary key(c_id,p_id)
);


Create table ResourceAttach
(
r_id int not null,
r_link varchar(20),
r_type varchar(20),
p_id int,
foreign key (p_id) references PostShare(p_id),
primary key(r_id)
);


Create table ResourceOwn
(
r_id int not null,
u_id int,
foreign key(u_id) references Users(u_id),
primary key(r_id,u_id)
);

Create table Connections
(
fromuser int not null,
touser int not null,
conn_type varchar(20),
foreign key (fromuser) references Users(u_id),
primary key (fromuser,touser)
);


--INSERT QUERIES
--Users
insert into users
Values (1,'Employed',to_date('04/09/2013 8:15:20','mm/dd/yyyy HH24:MI:SS'),'ladygaga@xx.com','Lady','Gaga','USA',90007);
insert into users
Values (2,'Student',to_date('02/19/2013 7:12:59','mm/dd/yyyy hh24:mi:ss'),'mickey@xx.com','Michael','Smith','USA',58493);
insert into users
Values (3,'Job Seeker',to_date('01/24/2013 14:00:00','mm/dd/yyyy hh24:mi:ss'),'imwilliams@yy.com','Patricia','Williams','USA',42231);
insert into users
Values (4,'Employed',to_date('12/31/2013 9:30:12','mm/dd/yyyy hh24:mi:ss'),'millerl@xx.com','Linda','Miller','USA',61123);
insert into users
Values (5,'Job Seeker',to_date('10/25/2013 17:33:12','mm/dd/yyyy hh24:mi:ss'),'robert@xx.com','Robert','Moore','USA',36393);
insert into users
Values (6,'Student',to_date('8/8/2013 18:18:18','mm/dd/yyyy hh24:mi:ss'),'taylor@yy.com','David','Taylor','Ukraine',84489);
insert into users
Values (7,'Employed',to_date('4/7/2013 21:30:00','mm/dd/yyyy hh24:mi:ss'),'mariahall@xx.com','Maria','Hall','Germany',45898);
insert into users
Values (8,'Employed',to_date('3/2/2013 22:00:00','mm/dd/yyyy hh24:mi:ss'),'jyoung@xx.com','Jennifer','Young','Portugal',11276);
insert into users
Values (9,'Job Seeker',to_date('5/5/2013 7:25:54','mm/dd/yyyy hh24:mi:ss'),'lauram@yy.com','Laura','Martin','Croatia',76688);
insert into users
Values (10,'Student',to_date('5/20/2013 18:20:30','mm/dd/yyyy hh24:mi:ss'),'susanhill@yy.com','Susan','Hill','Hungary',223344);
insert into users
Values (11,'Job Seeker',to_date('01/24/2013 12:30:20','mm/dd/yyyy hh24:mi:ss'),'srt@xy.com','Sachin','Tendulkar','India',651234);
insert into users
Values (12,'Employed',to_date('9/12/2013 12:30:20','mm/dd/yyyy hh24:mi:ss'),'rnadal@yy.com','Rafael','Nadal','Spain',775490);
insert into users
Values (13,'Student',to_date('11/24/2013 8:30:12','mm/dd/yyyy hh24:mi:ss'),'woods@xx.com','Tiger','Woods','USA',101010);
insert into users
Values (14,'Job Seeker',to_date('01/31/2013 19:45:54','mm/dd/yyyy hh24:mi:ss'),'jackiec@xx.com','Jackie','Chan','China',238345);
insert into users
Values (15,'Student',to_date('7/15/2013 10:10:10','mm/dd/yyyy hh24:mi:ss'),'chilinglin@xx.com','Chiling','Lin','China',565656);
insert into users
Values (16,'Employed',to_date('3/3/2013 12:29:30','mm/dd/yyyy hh24:mi:ss'),'alberto@xyz.com','Alberto','Dias','Brazil',232323);
insert into users
Values (17,'Student',to_date('3/3/2013 16:21:28','mm/dd/yyyy hh24:mi:ss'),'georgew@yyy.com','George','Williams','UK',876543);
insert into users
Values (18,'Job Seeker',to_date('01/24/2013 17:17:17','mm/dd/yyyy hh24:mi:ss'),'jose@zz.com','Nelson','Jose','South Africa',123456);
insert into users
Values (19,'Employed',to_date('10/14/2013 14:11:11','mm/dd/yyyy hh24:mi:ss'),'roger@yy.com','Roger','Federer','Switzerland',546791);
insert into users
Values (20,'Student',to_date('9/26/2013 20:20:30','mm/dd/yyyy hh24:mi:ss'),'serena@xyz.com','Serena','Williams','USA',192837);

--Group1
insert into Group1
values ('University of Southern California',56701,'USC');
insert into Group1
values ('Bay Area',56702,'BA');
insert into Group1
values ('Database System',56703,'DB');




--GroupJoin
insert into GroupJoin
values ('University of Southern California',56701,to_date('5/20/2013 18:20:30','mm/dd/yyyy hh24:mi:ss'),1);
insert into GroupJoin
values ('University of Southern California',56701,to_date('4/10/2013 18:10:30','mm/dd/yyyy hh24:mi:ss'),9);
insert into GroupJoin
values ('University of Southern California',56701,to_date('5/28/2013 4:20:30','mm/dd/yyyy hh24:mi:ss'),5);
insert into GroupJoin
values ('University of Southern California',56701,to_date('3/28/2013 16:30:30','mm/dd/yyyy hh24:mi:ss'),19);
insert into GroupJoin
values ('University of Southern California',56701,to_date('3/12/2013 15:30:40','mm/dd/yyyy hh24:mi:ss'),11);
insert into GroupJoin
values ('University of Southern California',56701,to_date('3/22/2013 10:30:40','mm/dd/yyyy hh24:mi:ss'),16);
insert into GroupJoin
values ('University of Southern California',56701,to_date('8/16/2013 9:45:40','mm/dd/yyyy hh24:mi:ss'),7);
insert into GroupJoin
values ('University of Southern California',56701,to_date('8/18/2013 9:16:40','mm/dd/yyyy hh24:mi:ss'),12);
insert into GroupJoin
values ('University of Southern California',56701,to_date('7/19/2013 12:16:40','mm/dd/yyyy hh24:mi:ss'),15);
insert into GroupJoin
values ('University of Southern California',56701,to_date('7/22/2013 12:22:40','mm/dd/yyyy hh24:mi:ss'),8);
insert into GroupJoin
values ('University of Southern California',56701,to_date('9/27/2013 12:45:40','mm/dd/yyyy hh24:mi:ss'),2);
insert into GroupJoin
values ('University of Southern California',56701,to_date('9/30/2013 16:45:40','mm/dd/yyyy hh24:mi:ss'),13);
insert into GroupJoin
values ('Bay Area',56702,to_date('11/12/2013 8:45:30','mm/dd/yyyy hh24:mi:ss'),1);
insert into GroupJoin
values ('Bay Area',56702,to_date('11/12/2013 9:45:30','mm/dd/yyyy hh24:mi:ss'),16);
insert into GroupJoin
values ('Bay Area',56702,to_date('11/24/2013 17:45:30','mm/dd/yyyy hh24:mi:ss'),17);
insert into GroupJoin
values ('Database System',56703,to_date('12/17/2013 11:11:30','mm/dd/yyyy hh24:mi:ss'),16);
insert into GroupJoin
values ('Database System',56703,to_date('12/1/2013 1:11:30','mm/dd/yyyy hh24:mi:ss'),1);

--Company
insert into company
values (4001,'Google Inc');
insert into company
values (4002,'Facebook');
insert into company
values (4003,'CISCO');
insert into company
values (4004,'Yahoo.com');

--CompanyFollow
insert into companyfollow
values(4002,to_date('1/4/2013 7:55:30','mm/dd/yyyy hh24:mi:ss'),12);
insert into companyfollow
values(4004,to_date('1/8/2013 7:35:30','mm/dd/yyyy hh24:mi:ss'),17);
insert into companyfollow
values(4002,to_date('12/6/2013 6:43:55','mm/dd/yyyy hh24:mi:ss'),2);
insert into companyfollow
values(4001,to_date('11/5/2013 4:53:15','mm/dd/yyyy hh24:mi:ss'),10);
insert into companyfollow
values(4002,to_date('11/15/2013 1:23:17','mm/dd/yyyy hh24:mi:ss'),4);



--PostShare
insert into postshare
values (101,'connection','FB CEO Resigns','public',null,null,to_date('12/16/2013 15:32:00','mm/dd/yyyy hh24:mi:ss'),19,20);
insert into postshare
values (102,'connection','Yahoo hiring interns','connection',null,null,to_date('12/18/2013 8:15:00','mm/dd/yyyy hh24:mi:ss'),9,3);
insert into postshare
values (103,'group','Tech jobs rush','public',null,null,to_date('12/18/2013 18:22:00','mm/dd/yyyy hh24:mi:ss'),11,56701);
insert into postshare
values (104,'group','See you at career fair','public',null,null,to_date('12/24/2013 20:20:00','mm/dd/yyyy hh24:mi:ss'),16,56702);
insert into postshare
values (105,'connection','are you ready','connection',null,null,to_date('12/30/2013 12:30:00','mm/dd/yyyy hh24:mi:ss'),1,3);
insert into postshare
values (107,'group','Trojans hiring','public',null,null,to_date('1/5/2014 08:30:00','mm/dd/yyyy hh24:mi:ss'),7,56701);
insert into postshare
values (106,'connection','Google glasses','public',null,null,to_date('1/4/2014 16:30:00','mm/dd/yyyy hh24:mi:ss'),9,3);
insert into postshare
values (108,'company','is FB using open graphs?','public',null,null,to_date('1/5/2014 12:30:00','mm/dd/yyyy hh24:mi:ss'),10,4002);
insert into postshare
values (109,'connection','Google hiring interns','connection',null,null,to_date('12/19/2013 8:15:00','mm/dd/yyyy hh24:mi:ss'),14,3);
insert into postshare
values (110,'connection','Amazon hiring interns','connection',null,null,to_date('1/4/2014 8:15:00','mm/dd/yyyy hh24:mi:ss'),14,10);
insert into postshare
values (111,'connection','Wow!Amazing!','connection',null,null,to_date('1/5/2014 8:15:00','mm/dd/yyyy hh24:mi:ss'),11,14);
insert into postshare
values (112,'connection','Wow!Superb!','connection',null,null,to_date('1/5/2014 8:15:00','mm/dd/yyyy hh24:mi:ss'),11,14);
insert into postshare
values (113,'group','USC welcomes you!','connection',null,null,to_date('1/5/2014 8:15:00','mm/dd/yyyy hh24:mi:ss'),56701,56701);
insert into postshare
values (114,'group','Fight on!','connection',null,null,to_date('1/5/2014 8:15:00','mm/dd/yyyy hh24:mi:ss'),56701,56701);
insert into postshare
values (115,'group','Fight on!','public',null,null,to_date('1/5/2014 8:15:00','mm/dd/yyyy hh24:mi:ss'),56701,56701);

--CommentHas
insert into commenthas
values (701,'Which companies coming?',1,1,103,12);
insert into commenthas
values (702,'Really?When?',1,0,101,20);
insert into commenthas
values (703,'JP Morgan',1,0,103,15);
insert into commenthas
values (704,'From USC!',0,0,102,6);
insert into commenthas
values (705,'Today',1,1,101,12);
insert into commenthas
values (706,'When is Career fair?',1,0,104,17);
insert into commenthas
values (707,'Bloomburg',0,1,103,12);
insert into commenthas
values (707,'Bloomburg',0,1,103,12);
insert into commenthas
values (709,'Trojans hiring trojans',1,0,107,8);
insert into commenthas
values (710,'Startups!',0,1,107,9);
insert into commenthas
values (711,'I love google glasses',0,1,106,3);
insert into commenthas
values (712,'Big companies',1,0,107,8);
insert into commenthas
values (713,'FB using RDF',1,0,108,14);
insert into commenthas
values (714,'RDF to link data',1,1,108,15);
insert into commenthas
values (715,'Linked Data!',1,1,111,15);
insert into commenthas
values (716,'Congrats!',1,1,111,15);
insert into commenthas
values (717,'Congrats!Well done!',1,1,111,8);
insert into commenthas
values (718,'Great!Congrats!',1,1,111,15);
insert into commenthas
values (719,'Great!Thanks for info.',1,1,113,15);
insert into commenthas
values (720,'Great!Thanks you!',1,1,113,14);
insert into commenthas
values (721,'Great!Thanks man!',1,1,114,14);
insert into commenthas
values (722,'Great!Thanks dude!',1,1,114,14);
insert into commenthas
values (723,'Thanks!P',1,1,115,14);
insert into commenthas
values (724,'Great!Thanks a ton!',1,1,115,14);


--ResourceAttach
insert into resourceattach
values (8001,'image8001','image',103);
insert into resourceattach
values (8002,'image8002','image',null);
insert into resourceattach
values (8003,'presentation8003','presentation',null);
insert into resourceattach
values (8004,'survey8004','survey',null);
insert into resourceattach
values (8005,'presentation8005','presentation',null);
insert into resourceattach
values (8006,'presentation8006','presentation',null);
insert into resourceattach
values (8007,'image8007','image',null);
insert into resourceattach
values (8008,'survey8008','survey',106);
insert into resourceattach
values (8009,'image8008','image',101);
insert into resourceattach
values (8010,'presentation8010','presentation',null);
insert into resourceattach
values (8011,'presentation8011','presentation',104);

--ResourceOwn
insert into resourceown
values (8001,1);
insert into resourceown
values (8006,1);
insert into resourceown
values (8003,2);
insert into resourceown
values (8003,3);
insert into resourceown
values (8004,4);
insert into resourceown
values (8006,4);
insert into resourceown
values (8002,5);
insert into resourceown
values (8006,6);
insert into resourceown
values (8008,6);
insert into resourceown
values (8007,7);
insert into resourceown
values (8006,7);
insert into resourceown
values (8008,8);
insert into resourceown
values (8005,8);
insert into resourceown
values (8003,9);
insert into resourceown
values (8010,10);
insert into resourceown
values (8006,10);
insert into resourceown
values (8008,11);
insert into resourceown
values (8003,12);
insert into resourceown
values (8011,13);
insert into resourceown
values (8006,13);
insert into resourceown
values (8003,14);
insert into resourceown
values (8010,15);
insert into resourceown
values (8006,15);
insert into resourceown
values (8001,16);
insert into resourceown
values (8007,17);
insert into resourceown
values (8004,17);
insert into resourceown
values (8003,18);
insert into resourceown
values (8004,19);
insert into resourceown
values (8006,19);
insert into resourceown
values (8011,20);
insert into resourceown
values (8001,20);
insert into resourceown
values (8002,8);
insert into resourceown
values (8009,19);
insert into resourceown
values (8001,11);
insert into resourceown
values (8011,16);
insert into resourceown
values (8008,9);

--Connections
insert into Connections
values (1,56701,'group');
insert into Connections
values (5,56701,'group');
insert into Connections
values (9,56701,'group');
insert into Connections
values (19,56701,'group');
insert into Connections
values (11,56701,'group');
insert into Connections
values (16,56701,'group');
insert into Connections
values (7,56701,'group');
insert into Connections
values (12,56701,'group');
insert into Connections
values (15,56701,'group');
insert into Connections
values (8,56701,'group');
insert into Connections
values (2,56701,'group');
insert into Connections
values (13,56701,'group');
insert into Connections
values (16,56702,'group');
insert into Connections
values (1,56702,'group');
insert into Connections
values (17,56702,'group');
insert into Connections
values (16,56703,'group');
insert into Connections
values (1,56703,'group');
insert into Connections
values (19,12,'connection');
insert into Connections
values (20,12,'connection');
insert into Connections
values (19,20,'connection');
insert into Connections
values (9,3,'connection');
insert into Connections
values (3,6,'connection');
insert into Connections
values (1,3,'connection');
insert into Connections
values (9,7,'connection');
insert into Connections
values (14,5,'connection');
insert into Connections
values (16,18,'connection');
insert into Connections
values (5,20,'connection');
insert into Connections
values (7,14,'connection');
insert into Connections
values (12,4002,'company');
insert into Connections
values (17,4004,'company');
insert into Connections
values (2,4002,'company');
insert into Connections
values (10,4001,'company');
insert into Connections
values (4,4002,'company');
insert into Connections
values (7,1,'connection');



