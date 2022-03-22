-- 1. Создать базу данных

USE master
GO

IF DB_ID (N'lab5') IS NOT NULL
	DROP DATABASE lab5
GO

CREATE DATABASE lab5
ON (
	NAME = lab5dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab5\lab5dat.mdf',
	SIZE = 10,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5%
)

LOG ON (
	NAME = lab5log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab5\lab5log.ldf',
	SIZE = 5MB, 
	MAXSIZE = 25MB,
	FILEGROWTH = 5MB
)
GO


-- 2. Создать произвольную таблицу

USE lab5
GO

IF OBJECT_ID (N'Clients') is NOT NULL
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY,
	Name nvarchar(50) NOT NULL,
	Surname nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL, 
	Date_of_birth date NOT NULL, 
	Gender bit,
	Document_type int NOT NULL,
	Document_ID nvarchar(20) NOT NULL,
	Telephone nvarchar(20)
)
GO

INSERT INTO Clients (Client_ID, Name, Surname, Lastname, Date_of_birth, Gender, Document_type, Document_ID, Telephone)
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

SELECT * FROM Clients
GO


-- 3. Добавить файловую группу и файл

USE master
GO

ALTER DATABASE lab5
	ADD FILEGROUP lab5_fg
GO

ALTER DATABASE lab5
ADD FILE (
	NAME = lab5dat1,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab5\lab5dat1.ndf',
	SIZE = 10MB,
	MAXSIZE = 100MB,
	FILEGROWTH = 5MB)
TO FILEGROUP lab5_fg
GO


-- 4. Сделать созданную файловую группу файловой группой по умолчанию

ALTER DATABASE lab5
	MODIFY FILEGROUP lab5_fg DEFAULT
GO


-- 5. Создать еще одну произвольную таблицу

USE lab5
GO

IF OBJECT_ID (N'Workers') is NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY NOT NULL,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL,
	Qualification nvarchar(20),
	Qualification_K numeric(10, 3) NOT NULL
)
GO

INSERT INTO Workers(Surname, Name, Lastname, Qualification, Qualification_K)
VALUES 
	(N'Алтуфьевский', N'Владислав', N'Михайлович', N'A', 1.1),
	(N'Ивлев', N'Павел', N'Константинович', N'B', 1.2),
	(N'Раскольников', N'Никита', N'Максимович', N'C', 1.0),
	(N'Чехов', N'Владислав', N'Александрович', N'B', 1.15)
GO

SELECT * FROM Workers
GO


-- 6. Удалить созданную вручную файловую группу

USE master
GO

ALTER DATABASE lab5
	MODIFY FILEGROUP [primary] DEFAULT
GO

USE lab5
GO

DROP TABLE Workers
GO

USE master
GO

ALTER DATABASE lab5
	REMOVE FILE lab5dat1
GO

ALTER DATABASE lab5
	REMOVE FILEGROUP lab5_fg
GO


-- 7. Создать схему, переместить в нее одну из таблиц, удалить схему

USE lab5
GO

CREATE SCHEMA lab5_schema
GO

ALTER SCHEMA lab5_schema TRANSFER Clients
GO

DROP TABLE lab5_schema.Clients
DROP SCHEMA lab5_schema
GO