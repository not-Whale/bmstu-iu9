-- 1. Создать две базы данных на одном экземпляре
--    СУБД SQL Server 2012.

USE master
GO

IF DB_ID(N'lab13_1') IS NOT NULL
	DROP DATABASE lab13_1
GO

IF DB_ID(N'lab13_2') IS NOT NULL
	DROP DATABASE lab13_2
GO

CREATE DATABASE lab13_1
ON (
	NAME = lab13_1dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab13\lab13_1dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab13_1log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab13\lab13_1log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO

CREATE DATABASE lab13_2
ON (
	NAME = lab13_2dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab13\lab13_2dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab13_2log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab13\lab13_2log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO


-- 2. Создать в базах данных п.1. горизонтально
--    фрагментированные таблицы.

-- Первая база данных

USE lab13_1
GO

IF OBJECT_ID(N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY NOT NULL,
		CONSTRAINT check_client_id CHECK (Client_ID <= 4),
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

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY NOT NULL,
		CONSTRAINT check_worker_id CHECK (Worker_ID <= 2),
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL,
	Qualification nvarchar(20) DEFAULT ('Не указана'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
		CONSTRAINT check_QK_validity CHECK (Qualification_K >= 1.0)
)
GO

-- Вторая база данных

USE lab13_2
GO

IF OBJECT_ID(N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY NOT NULL,
		CONSTRAINT check_client_id CHECK (Client_ID > 4),
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

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY NOT NULL,
		CONSTRAINT check_worker_id CHECK (Worker_ID > 2),
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL,
	Qualification nvarchar(20) DEFAULT ('Не указана'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
		CONSTRAINT check_QK_validity CHECK (Qualification_K >= 1.0)
)
GO


-- 3. Создать секционированные представления,
--    обеспечивающие работу с данными таблиц
--    (выборку, вставку, изменение, удаление).

-- Представления

USE lab13_1
GO

IF OBJECT_ID(N'Clients_view') IS NOT NULL
	DROP VIEW Clients_view
GO

IF OBJECT_ID(N'Workers_view') IS NOT NULL
	DROP VIEW Workers_view
GO

CREATE VIEW Clients_view
AS
	SELECT * FROM lab13_1.dbo.Clients
	UNION ALL
	SELECT * FROM lab13_2.dbo.Clients
GO

CREATE VIEW Workers_view
AS
	SELECT * FROM lab13_1.dbo.Workers
	UNION ALL
	SELECT * FROM lab13_2.dbo.Workers
GO

-- Вставка

INSERT INTO Clients_view
VALUES 
	(1, N'Особов', N'Иван', N'Юрьевич', '1989-07-05', 1, 1, '4285 122897', '+79943581172'),
	(2, N'Мармеладов', N'Владислав', N'Максимович', '1997-03-04', 1, 1, '4040 910626', '+79204692461'),
	(3, N'Романов', N'Данил', N'Семенович', '1993-10-21', 1, 1, '4237 836539', '+79751928912'),
	(4, N'Родзаевский', N'Иван', N'Михайлович', '1984-06-01', 1, 1, '4047 266308', '+79077920200'),
	(5, N'Яковлева', N'Валерия', N'Михайловна', '1995-01-06', 0, 1, '4349 869772', '+79080834358'),
	(6, N'Попова', N'Вероника', N'Станиславовна', '1992-12-07', 0, 1, '4920 145641', '+79299575273'),
	(7, N'Дементьева', N'Дарья', N'Данииловна', '1996-02-21', 0, 1, '4743 278264', '+79426540840'),
	(8, N'Миронова', N'Екатерина', N'Антоновна', '1994-12-15', 0, 1, '4712 484969', '+79825465968')
GO

INSERT INTO Workers_view
VALUES 
	(1, N'Алтуфьевский', N'Владислав', N'Михайлович', N'A', 1.1),
	(2, N'Ивлев', N'Павел', N'Константинович', N'B', 1.2),
	(3, N'Раскольников', N'Никита', N'Максимович', N'C', 1.0),
	(4, N'Чехов', N'Владислав', N'Александрович', N'B', 1.15)
GO

-- Выборка (начальное состояние - после вставки)

SELECT * FROM Clients_view
SELECT * FROM Workers_view
GO

-- Удаление

DELETE FROM Clients_view 
	WHERE (Client_ID <= 2) or (Client_ID >= 7)
GO

DELETE FROM Workers_view
	WHERE Worker_ID IN (1, 4)
GO

-- Изменение

UPDATE Clients_view SET Document_ID = '3026 611846' WHERE Client_ID = 4
GO

UPDATE Workers_view SET Qualification_K = 1.5 WHERE Worker_ID = 2
GO

UPDATE Clients_view SET Client_ID = 12 WHERE Client_ID = 3
GO

-- Конечное состояние 

SELECT * FROM Clients_view
SELECT * FROM Workers_view
GO