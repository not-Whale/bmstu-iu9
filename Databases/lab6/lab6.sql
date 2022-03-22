USE master
GO

IF DB_ID (N'lab6') IS NOT NULL
	DROP DATABASE lab6
GO

CREATE DATABASE lab6
ON (
	NAME = lab6dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab6\lab6dat.mdf',
	SIZE = 10,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5%
)
LOG ON (
	NAME = lab6log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab6\lab6log.ldf',
	SIZE = 5MB,
	MAXSIZE = 25MB,
	FILEGROWTH = 5MB
)
GO

-- 1. Создать таблицу с автоинкрементным первичным ключом

USE lab6
GO

IF OBJECT_ID (N'Clients1') IS NOT NULL
	DROP TABLE Clients1
GO

-- 2. Добавить поля, для которых используются ограничения (CHECK), 
--    значения по умолчанию (DEFAULT), также использовать встроенные функции 
--    для вычисления значений.

CREATE TABLE Clients1 (
	Client_ID int IDENTITY(1,1) PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL, 
	Date_of_birth date NOT NULL CHECK (Date_of_birth > '1850-01-01' AND Date_of_birth < '2022-01-01'), 
	Gender bit,
	Document_type int NOT NULL,
	Document_ID nvarchar(20) NOT NULL,
	Telephone nvarchar(20)
)
GO

INSERT INTO Clients1(Surname, Name, Lastname, Date_of_birth, Document_type, Document_ID)
VALUES 
	(N'Иванов', N'Иван', N'Иванович', '1996-01-02', 1, '12 34 654321'),
	(N'Петров', N'Петр', N'Петрович', '1997-03-04', 1, '65 77 123098')
GO

SELECT * FROM Clients1
GO

SELECT IDENT_CURRENT('Clients1') AS last_id
GO

-- 3. Создать таблицу с первичным ключом на основе глобального уникального идентификатора.

IF OBJECT_ID (N'Workers1') IS NOT NULL
	DROP TABLE Workers1
GO

CREATE TABLE Workers1 (
	Worker_ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL,
	Qualification nvarchar(20) DEFAULT ('Unknown'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
	CONSTRAINT check_K CHECK (Qualification_K >= 1.0)
)
GO

INSERT INTO Workers1(Surname, Name, Lastname)
VALUES 
	(N'Григорьев', N'Николай', N'Антонович')
GO

SELECT * FROM Workers1
GO

-- 4. Создать таблицу с первичным ключом на основе последовательности.

IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'TestSequence')
	DROP SEQUENCE TestSequence
go

CREATE SEQUENCE TestSequence
	START WITH 0
	INCREMENT BY 1
	MAXVALUE 10;
go

IF OBJECT_ID(N'ServiceType') IS NOT NULL
	DROP TABLE ServiceType
GO

CREATE TABLE ServiceType (
	Service_ID int PRIMARY KEY,
	Service_Description nvarchar(100) NOT NULL,
	Price money NOT NULL
)
GO

INSERT INTO ServiceType
VALUES
	(NEXT VALUE FOR DBO.TestSequence, N'Услуга за 50 тугриков', $50),
	(NEXT VALUE FOR DBO.TestSequence, N'Услуга подороже за 100 тугриков', $100),
	(NEXT VALUE FOR DBO.TestSequence, N'Вообще дорогая услуга за 1000 тугриков', $1000)
GO

SELECT * FROM ServiceType
GO


-- 5. Создать две связанные таблицы, и протестировать на них различные варианты действий для ограничений ссылочной целостности.

IF OBJECT_ID (N'Clients') IS NOT NULL
	DROP TABLE Logbook
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL, 
	Date_of_birth date NOT NULL CHECK (Date_of_birth > '1850-01-01' AND Date_of_birth < '2022-01-01'), 
	Gender bit,
	Document_type int NOT NULL,
	Document_ID nvarchar(20) NOT NULL,
	Telephone nvarchar(20)
)
GO

INSERT INTO Clients(Client_ID, Surname, Name, Lastname, Date_of_birth, Document_type, Document_ID)
VALUES 
	(1, N'Иванов', N'Иван', N'Иванович', '1996-01-02', 1, '12 34 654321'),
	(2, N'Петров', N'Петр', N'Петрович', '1997-03-04', 1, '65 77 123098')
GO

INSERT INTO Clients(Client_ID, Surname, Name, Lastname, Date_of_birth, Gender, Document_type, Document_ID, Telephone)
VALUES 
	(3, N'Особов', N'Иван', N'Юрьевич', '1989-07-05', 1, 1, '4285 122897', '+79943581172'),
	(4, N'Мармеладов', N'Владислав', N'Максимович', '1997-03-04', 1, 1, '4040 910626', '+79204692461'),
	(5, N'Романов', N'Данил', N'Семенович', '1993-10-21', 1, 1, '4237 836539', '+79751928912'),
	(6, N'Родзаевский', N'Иван', N'Михайлович', '1984-06-01', 1, 1, '4047 266308', '+79077920200'),
	(7, N'Яковлева', N'Валерия', N'Михайловна', '1995-01-06', 0, 1, '4349 869772', '+79080834358'),
	(8, N'Попова', N'Вероника', N'Станиславовна', '1992-12-07', 0, 1, '4920 145641', '+79299575273'),
	(9, N'Дементьева', N'Дарья', N'Данииловна', '1996-02-21', 0, 1, '4743 278264', '+79426540840'),
	(10, N'Миронова', N'Екатерина', N'Антоновна', '1994-12-15', 0, 1, '4712 484969', '+79825465968')
GO

IF OBJECT_ID(N'Logbook') IS NOT NULL
	DROP TABLE Logbook
GO

CREATE TABLE Logbook (
	Procedure_ID int PRIMARY KEY NOT NULL,
	Client_ID int NOT NULL
		-- NOT NULL
		-- DEFAULT 1
		-- DEFAULT 1
	, CONSTRAINT FK_Clients FOREIGN KEY (Client_ID) REFERENCES Clients(Client_ID)
		-- ON DELETE SET DEFAULT
		ON DELETE CASCADE
		-- ON DELETE SET NULL
		-- ON UPDATE SET DEFAULT
		ON UPDATE CASCADE
		-- ON UPDATE SET NULL
		,
	Total_price money NOT NULL,
	Reserve_date date NOT NULL,
	Reserve_time time NOT NULL
)
GO

INSERT INTO Logbook(Client_ID, Procedure_ID, Total_price, Reserve_date, Reserve_time)
VALUES
	(1, 1, $100, '2021-12-22', '15:05:00'),
	(1, 2, $200, '2021-12-21', '15:00:00'),
	(2, 3, $300, '2021-12-22', '12:15:00'),
	(2, 4, $400, '2021-12-20', '16:25:00')
GO

SELECT * FROM Clients
GO

SELECT * FROM Logbook
GO

DELETE FROM Clients
	WHERE Client_ID = 1
GO

UPDATE Clients
	SET Client_ID = 15
	WHERE Client_ID = 3
GO

SELECT * FROM Clients
GO

SELECT * FROM Logbook
GO
