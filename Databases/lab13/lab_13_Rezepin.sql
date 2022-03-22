-- 1. ������� ��� ���� ������ �� ����� ����������
--    ���� SQL Server 2012.

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
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab13\lab13_1dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab13_1log,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab13\lab13_1log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO

CREATE DATABASE lab13_2
ON (
	NAME = lab13_2dat,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab13\lab13_2dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab13_2log,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab13\lab13_2log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO


-- 2. ������� � ����� ������ �.1. �������������
--    ����������������� �������.

-- ������ ���� ������

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
	Telephone nvarchar(20) DEFAULT N'�� ������'
)
GO

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY NOT NULL,
		CONSTRAINT check_worker_id CHECK (Worker_ID <= 2),
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL,
	Qualification nvarchar(20) DEFAULT ('�� �������'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
		CONSTRAINT check_QK_validity CHECK (Qualification_K >= 1.0)
)
GO

-- ������ ���� ������

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
	Telephone nvarchar(20) DEFAULT N'�� ������'
)
GO

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY NOT NULL,
		CONSTRAINT check_worker_id CHECK (Worker_ID > 2),
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL,
	Qualification nvarchar(20) DEFAULT ('�� �������'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
		CONSTRAINT check_QK_validity CHECK (Qualification_K >= 1.0)
)
GO


-- 3. ������� ���������������� �������������,
--    �������������� ������ � ������� ������
--    (�������, �������, ���������, ��������).

-- �������������

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

-- �������

INSERT INTO Clients_view
VALUES 
	(1, N'������', N'����', N'�������', '1989-07-05', 1, 1, '4285 122897', '+79943581172'),
	(2, N'����������', N'���������', N'����������', '1997-03-04', 1, 1, '4040 910626', '+79204692461'),
	(3, N'�������', N'�����', N'���������', '1993-10-21', 1, 1, '4237 836539', '+79751928912'),
	(4, N'�����������', N'����', N'����������', '1984-06-01', 1, 1, '4047 266308', '+79077920200'),
	(5, N'��������', N'�������', N'����������', '1995-01-06', 0, 1, '4349 869772', '+79080834358'),
	(6, N'������', N'��������', N'�������������', '1992-12-07', 0, 1, '4920 145641', '+79299575273'),
	(7, N'����������', N'�����', N'����������', '1996-02-21', 0, 1, '4743 278264', '+79426540840'),
	(8, N'��������', N'���������', N'���������', '1994-12-15', 0, 1, '4712 484969', '+79825465968')
GO

INSERT INTO Workers_view
VALUES 
	(1, N'������������', N'���������', N'����������', N'A', 1.1),
	(2, N'�����', N'�����', N'��������������', N'B', 1.2),
	(3, N'������������', N'������', N'����������', N'C', 1.0),
	(4, N'�����', N'���������', N'�������������', N'B', 1.15)
GO

-- ������� (��������� ��������� - ����� �������)

SELECT * FROM Clients_view
SELECT * FROM Workers_view
GO

-- ��������

DELETE FROM Clients_view 
	WHERE (Client_ID <= 2) or (Client_ID >= 7)
GO

DELETE FROM Workers_view
	WHERE Worker_ID IN (1, 4)
GO

-- ���������

UPDATE Clients_view SET Document_ID = '3026 611846' WHERE Client_ID = 4
GO

UPDATE Workers_view SET Qualification_K = 1.5 WHERE Worker_ID = 2
GO

UPDATE Clients_view SET Client_ID = 12 WHERE Client_ID = 3
GO

-- �������� ��������� 

SELECT * FROM Clients_view
SELECT * FROM Workers_view
GO