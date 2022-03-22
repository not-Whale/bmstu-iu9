-- 0. ������� ��� ���� ������ �� ����� ����������
--    ���� SQL Server 2012.

USE master
GO

IF DB_ID(N'lab14_1') IS NOT NULL
	DROP DATABASE lab14_1
GO

IF DB_ID(N'lab14_2') IS NOT NULL
	DROP DATABASE lab14_2
GO

CREATE DATABASE lab14_1
ON (
	NAME = lab14_1dat,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab14\lab14_1dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab14_1log,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab14\lab14_1log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO

CREATE DATABASE lab14_2
ON (
	NAME = lab14_2dat,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab14\lab14_2dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab14_2log,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab14\lab14_2log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO


-- 2. ������� � ����� ������ ������ 1 ������� 13
--    �������, ���������� �����������
--    ����������������� ������.

-- ������ ���� ������

USE lab14_1
GO

IF OBJECT_ID(N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY NOT NULL,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL, 
	Date_of_birth date NOT NULL CHECK (Date_of_birth > '1850-01-01' AND Date_of_birth < CONVERT(date, GETDATE())), 
)
GO


-- ������ ���� ������

USE lab14_2
GO

IF OBJECT_ID(N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY NOT NULL,
	Document_type int NOT NULL,
	Document_ID nvarchar(20) NOT NULL,
)
GO


-- 2. ������� ����������� �������� ���� ������
--    (�������������, ��������), �������������� ������
--    � ������� ����������� ����������������� ������
--    (�������, �������, ���������, ��������).

-- �������������

USE lab14_1
GO

IF OBJECT_ID(N'Clients_view') IS NOT NULL
	DROP VIEW Clients_view
GO

CREATE VIEW Clients_view
AS
	SELECT f.*, s.Document_type, s.Document_ID
	FROM lab14_1.dbo.Clients as f, lab14_2.dbo.Clients as s
	WHERE f.Client_ID = s.Client_ID
GO

-- �������

IF OBJECT_ID(N'Clients_view_insert') IS NOT NULL
	DROP TRIGGER Clients_view_insert
GO

CREATE TRIGGER Clients_view_insert
	ON Clients_view
	INSTEAD OF INSERT
AS
	BEGIN		
		IF (SELECT COUNT(*) FROM inserted as i WHERE i.Client_ID IN (SELECT Client_ID FROM lab14_1.dbo.Clients_view)) != 0
			BEGIN
				EXEC sp_addmessage 50001, 15, N'����� ������� ��������������!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50001, 15, -1)
			END
		ELSE
			BEGIN
				INSERT INTO lab14_1.dbo.Clients
				SELECT Client_ID, Surname, Name, Lastname, Date_of_birth FROM inserted

				INSERT INTO lab14_2.dbo.Clients
				SELECT Client_ID, Document_type, Document_ID FROM inserted
			END
	END
GO

INSERT INTO Clients_view
VALUES 
	(1, N'������', N'����', N'�������', '1989-07-05', 1, '4285 122897'),
	(2, N'����������', N'���������', N'����������', '1997-03-04', 1, '4040 910626'),
	(3, N'�������', N'�����', N'���������', '1993-10-21', 1, '4237 836539'),
	(4, N'�����������', N'����', N'����������', '1984-06-01', 1, '4047 266308'),
	(5, N'��������', N'�������', N'����������', '1995-01-06', 1, '4349 869772'),
	(6, N'������', N'��������', N'�������������', '1992-12-07', 1, '4920 145641'),
	(7, N'����������', N'�����', N'����������', '1996-02-21', 1, '4743 278264'),
	(8, N'��������', N'���������', N'���������', '1994-12-15', 1, '4712 484969')
GO

SELECT * FROM Clients_view
GO

-- ���������� 

IF OBJECT_ID(N'Clients_view_update') IS NOT NULL
	DROP TRIGGER Clients_view_update
GO

CREATE TRIGGER Clients_view_update
	ON Clients_view
	INSTEAD OF UPDATE
AS
	BEGIN
		IF UPDATE(Client_ID)
			BEGIN
				EXEC sp_addmessage 50002, 15, N'������ �������� ����� �������!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50002, 15, -1)
			END
		ELSE
			BEGIN

				IF UPDATE(Surname)
					UPDATE lab14_1.dbo.Clients SET Surname = i.Surname FROM inserted AS i, lab14_1.dbo.Clients as c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Name)
					UPDATE lab14_1.dbo.Clients SET Name = i.Name FROM inserted AS i, lab14_1.dbo.Clients as c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Lastname)
					UPDATE lab14_1.dbo.Clients SET Lastname = i.Lastname FROM inserted AS i, lab14_1.dbo.Clients as c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Date_of_birth)
					UPDATE lab14_1.dbo.Clients SET Date_of_birth = i.Date_of_birth FROM inserted AS i, lab14_1.dbo.Clients as c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Document_type)
					UPDATE lab14_2.dbo.Clients SET Document_type = i.Document_type FROM inserted AS i, lab14_2.dbo.Clients as c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Document_ID)
					UPDATE lab14_2.dbo.Clients SET Document_ID = i.Document_ID FROM inserted AS i, lab14_2.dbo.Clients as c WHERE i.Client_ID = c.Client_ID

			END
	END
GO

UPDATE Clients_view SET Client_ID = 200
	WHERE Client_ID = 1
GO

UPDATE Clients_view SET Surname = 'new_surname'
	WHERE Client_ID = 1
GO

UPDATE Clients_view SET Name = 'new_name'
	WHERE Client_ID = 2
GO

UPDATE Clients_view SET Lastname = 'new_lastname'
	WHERE Client_ID = 6
GO

UPDATE Clients_view SET Document_ID = 'new_id'
	WHERE Client_ID = 8
GO

UPDATE Clients_view SET Surname = Surname + N' X'
GO

-- ��������

IF OBJECT_ID(N'Clients_view_delete') IS NOT NULL
	DROP TRIGGER Clients_view_delete
GO

CREATE TRIGGER Clients_view_delete
	ON Clients_view
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM lab14_1.dbo.Clients WHERE Client_ID IN (SELECT Client_ID FROM deleted)
		DELETE FROM lab14_2.dbo.Clients	WHERE Client_ID IN (SELECT Client_ID FROM deleted)
	END
GO

DELETE FROM Clients_view
	WHERE Client_ID BETWEEN 3 AND 5
GO

SELECT * FROM Clients_view
GO