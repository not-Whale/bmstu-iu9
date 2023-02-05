USE master
GO

IF DB_ID (N'lab0') IS NOT NULL
	DROP DATABASE lab0
GO

CREATE DATABASE lab0
ON (
	NAME = lab0dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab0\lab0dat.mdf',
	SIZE = 10,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5%
)
LOG ON (
	NAME = lab0log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab0\lab0log.ldf',
	SIZE = 5MB,
	MAXSIZE = 25MB,
	FILEGROWTH = 5MB
)
GO

USE lab0
GO

-- Таблица клиентов

IF OBJECT_ID (N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int IDENTITY(1,1) PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL, 
	Date_of_birth date NOT NULL CHECK (Date_of_birth > '1850-01-01' AND Date_of_birth < CONVERT(date, GETDATE())), 
	Gender bit DEFAULT NULL,
	Document_type int NOT NULL,
	Document_ID nvarchar(20) NOT NULL,
	Telephone nvarchar(20) DEFAULT N'Не указан'
)
GO

INSERT INTO Clients(Surname, Name, Lastname, Date_of_birth, Gender, Document_type, Document_ID, Telephone)
VALUES 
	(N'Особов', N'Иван', N'Юрьевич', '1989-07-05', 1, 1, '4285 122897', '+79943581172'),
	(N'Мармеладов', N'Владислав', N'Максимович', '1997-03-04', 1, 1, '4040 910626', '+79204692461'),
	(N'Романов', N'Данил', N'Семенович', '1993-10-21', 1, 1, '4237 836539', '+79751928912'),
	(N'Родзаевский', N'Иван', N'Михайлович', '1984-06-01', 1, 1, '4047 266308', '+79077920200'),
	(N'Яковлева', N'Валерия', N'Михайловна', '1995-01-06', 0, 1, '4349 869772', '+79080834358'),
	(N'Попова', N'Вероника', N'Станиславовна', '1992-12-07', 0, 1, '4920 145641', '+79299575273'),
	(N'Дементьева', N'Дарья', N'Данииловна', '1996-02-21', 0, 1, '4743 278264', '+79426540840'),
	(N'Миронова', N'Екатерина', N'Антоновна', '1994-12-15', 0, 1, '4712 484969', '+79825465968')
GO

-- Таблица персонала

IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Workers (
	Worker_ID int IDENTITY(1,1) PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL,
	Qualification nvarchar(20) DEFAULT ('Не указана'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
		CONSTRAINT check_QK_validity CHECK (Qualification_K >= 1.0)
)
GO

INSERT INTO Workers(Surname, Name, Lastname, Qualification, Qualification_K)
VALUES 
	(N'Алтуфьевский', N'Владислав', N'Михайлович', N'A', 1.1),
	(N'Ивлев', N'Павел', N'Константинович', N'B', 1.2),
	(N'Раскольников', N'Никита', N'Максимович', N'C', 1.0),
	(N'Чехов', N'Владислав', N'Александрович', N'B', 1.15)
GO

-- Таблица типов услуг

IF OBJECT_ID(N'ServiceType') IS NOT NULL
	DROP TABLE ServiceType
GO

CREATE TABLE ServiceType (
	Service_ID int IDENTITY(1,1) PRIMARY KEY,
	Service_Description nvarchar(100) NOT NULL,
	Price money NOT NULL
)
GO

INSERT INTO ServiceType(Service_Description, Price)
VALUES
	(N'Прием врача-стоматолога', $20),
	(N'Лечение кариеса', $25),
	(N'Лечение периодонтита', $30),
	(N'Снятие зубных отложений', $25),
	(N'Удаление зубов', $35),
	(N'Удаление костных выступов', $35)
GO

-- Таблица записей

IF OBJECT_ID(N'Logbook') IS NOT NULL
	DROP TABLE Logbook
GO

CREATE TABLE Logbook (
	Procedure_ID int IDENTITY(1,1) PRIMARY KEY,
	Client_ID int NOT NULL, 
		CONSTRAINT FK_Clients FOREIGN KEY (Client_ID) REFERENCES Clients(Client_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Worker_ID int NOT NULL,
		CONSTRAINT FK_Workers FOREIGN KEY (Worker_ID) REFERENCES Workers(Worker_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Service_ID int NOT NULL,
		CONSTRAINT FK_ServiceType FOREIGN KEY (Service_ID) REFERENCES ServiceType(Service_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Total_price money NOT NULL,
	Reserve_date date NOT NULL CHECK (Reserve_date >= '2001-01-01'),
	Reserve_time time NOT NULL CHECK (Reserve_time >= '07:00:00' AND Reserve_time <= '18:00:00')
)
GO

INSERT INTO Logbook(Client_ID, Worker_ID, Service_ID, Total_price, Reserve_date, Reserve_time)
VALUES
	(1, 1, 1, $20, '2019-04-29', '17:30:00'),
	(1, 2, 5, $35, '2019-10-04', '09:00:00'),
	(2, 1, 1, $20, '2020-05-14', '16:00:00'),
	(3, 3, 2, $25, '2018-05-16', '14:30:00'),
	(4, 1, 1, $20, '2015-01-27', '13:00:00'),
	(5, 4, 3, $30, '2016-08-31', '10:15:00'),
	(6, 1, 4, $25, '2017-12-14', '18:00:00'),
	(4, 2, 6, $35, '2015-12-22', '11:30:00'),
	(6, 1, 1, $20, '2017-06-02', '12:00:00'),
	(2, 2, 5, $35, '2020-11-12', '15:15:00')
GO