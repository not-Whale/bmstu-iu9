-- 1. ������� ���� ������, ���������������� � ������
--    ������������ ������ �4, ��������� ��������� �
--    ������������ ������� 5-10 �������� SQL Server 2012:
--       � ����������� �������� � ���������� ����������� ���� ������;						    
--		 � ��������� ��������� �����������;														
--       � ������������� � �������;																
--       � �������� ���������, ������� � ��������;											    
-- 2. �������� �������� ���� ������ ������ ��������������
--    ���������� DDL (CREATE/ALTER/DROP), � ������������
--    ������� �������������� ��������� �������:
--       � ���������� � ��������� �����;													    
--       � ���������� ����� ������;															    
--       � ���������� ����������� ����������� (PRIMARY KEY, NULL/NOT							
--         NULL/UNIQUE, CHECK � �.�.);														    
--       � ����������� �������� �� ���������;													
-- 3. � ��������������� ���� ������ ������ ���� ��� ��� ���� ������� (�
--    ������ �������� ���� ������ ��� �������������) ������� ������� DML
--    ���:
--       � ������� ������� (������� SELECT);													
--       � ���������� ����� ������� (������� INSERT), ��� � ������� �����������������			
--         �������� ��������, ��� � � ������� ������� SELECT;									
--       � ����������� ������� (������� UPDATE);											    
--       � �������� ������� (������� DELETE);												    
-- 4. �������, ��������� � ������ ��.2,3 ������ �������������� ���������
--    ����������� �����:
--       � �������� ������������� ������� (DISTINCT);											
--       � �����, �������������� � ���������� ����� (�������� ����������� ��� ����� �			
--         ������ / �������������);																
--       � ���������� ������ (INNER JOIN / LEFT JOIN / RIGHT JOIN / FULL OUTER JOIN);		    
--       � ������� ������ ������� (� ��� �����, ������� NULL / LIKE / BETWEEN / IN /			
--         EXISTS);																				
--       � ���������� ������� (ORDER BY - ASC, DESC);											
--       � ����������� ������� (GROUP BY + HAVING, ������������� ������� �������������			
--         � COUNT / AVG / SUM / MIN / MAX);													
--       � ����������� ����������� ���������� �������� (UNION / UNION ALL / EXCEPT /			
--         INTERSECT);																			
--       � ��������� �������.	


-- �������� ���� ������

USE master
GO

IF DB_ID (N'lab11') IS NOT NULL
	DROP DATABASE lab11
GO

CREATE DATABASE lab11
ON (
	NAME = lab11dat,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab11\lab11dat.mdf',
	SIZE = 10,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab11log,
	FILENAME = 'C:\Users\Admin\Desktop\������\5 �������\��\lab11\lab11log.ldf',
	SIZE = 5,
	MAXSIZE = 25,
	FILEGROWTH = 5
)
GO


-- �������� ������

USE lab11
GO

IF OBJECT_ID (N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

IF OBJECT_ID (N'Doctors') IS NOT NULL
	DROP TABLE Doctors
GO

IF OBJECT_ID(N'ServiceType') IS NOT NULL
	DROP TABLE ServiceType
GO

IF OBJECT_ID(N'Logbook') IS NOT NULL
	DROP TABLE Logbook
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY IDENTITY(1, 1) NOT NULL,
	Document_ID nvarchar(20) UNIQUE NOT NULL,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Secondname nvarchar(50) DEFAULT NULL, 
	Date_of_birth date NOT NULL CHECK (Date_of_birth > '1850-01-01' AND Date_of_birth < CONVERT(date, GETDATE())), 
	Telephone nvarchar(20) DEFAULT NULL
)
GO

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY IDENTITY(1, 1) NOT NULL,
	ITN nvarchar(20) UNIQUE NOT NULL,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Secondname nvarchar(50) DEFAULT NULL,
)
GO

CREATE TABLE Doctors (
	Doctor_ID int NOT NULL,
		CONSTRAINT FK_Workers FOREIGN KEY (Doctor_ID) REFERENCES Workers(Worker_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Qualification nvarchar(50) DEFAULT (N'�� �������'),
)
GO

CREATE TABLE ServiceType (
	Service_ID int PRIMARY KEY NOT NULL,
	Service_Description nvarchar(100) UNIQUE,
	Price money NOT NULL
)
GO

CREATE TABLE Logbook (
	Procedure_ID int PRIMARY KEY IDENTITY(1, 1) NOT NULL,
	Client_ID int NOT NULL, 
		CONSTRAINT FK_Clients FOREIGN KEY (Client_ID) REFERENCES Clients(Client_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Doctor_ID int NOT NULL,
		CONSTRAINT FK_Doctors FOREIGN KEY (Doctor_ID) REFERENCES Workers(Worker_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Service_ID int NOT NULL,
		CONSTRAINT FK_ServiceType FOREIGN KEY (Service_ID) REFERENCES ServiceType(Service_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Reserve_date date NOT NULL CHECK (Reserve_date >= '2001-01-01'),
	Reserve_time time NOT NULL CHECK (Reserve_time >= '07:00:00' AND Reserve_time <= '18:00:00')
)
GO

INSERT INTO Clients (Document_ID, Surname, Name, Date_of_birth, Telephone)
VALUES 
	('4285 122897', N'������', N'����', '1989-07-05', '+79943581172'),
	('4040 910626', N'����������', N'���������', '1997-03-04', '+79204692461')
GO

INSERT INTO Clients (Document_ID, Surname, Name, Secondname, Date_of_birth)
VALUES
	('4237 836539', N'�������', N'�����', N'���������', '1993-10-21'),
	('4047 266308', N'�����������', N'����', N'����������', '1984-06-01')
GO


INSERT INTO Clients (Document_ID, Surname, Name, Secondname, Date_of_birth, Telephone)
VALUES
	('4349 869772', N'��������', N'�������', N'����������', '1995-01-06', '+79080834358'),
	('4920 145641', N'������', N'��������', N'�������������', '1992-12-07', '+79299575273'),
	('4743 278264', N'����������', N'�����', N'����������', '1996-02-21', '+79426540840'),
	('4712 484969', N'��������', N'���������', N'���������', '1996-12-15', '+79825465968')
GO


INSERT INTO Workers (ITN, Surname, Name)
VALUES 
	('837502448086', N'������������', N'���������'),
	('158359719469', N'������������', N'������')
GO

INSERT INTO Workers (ITN, Surname, Name, Secondname)
VALUES
	('526820420779', N'�����', N'�����', N'��������������'),
	('365378862566', N'�����', N'���������', N'�������������'),
	('778301206476', N'�������', N'�����', N'���������')
GO

INSERT INTO Doctors (Doctor_ID, Qualification)
VALUES
	(1, N'������ ���������'),
	(2, N'������ ���������'),
	(3, N'������ ���������'),
	(4, N'������ ���������'), 
	(5, N'������ ���������')
GO

INSERT INTO ServiceType
VALUES
	(1, N'����� �����-�����������', $30),
	(2, N'������� �������', $50),
	(3, N'������� ������������', $60),
	(4, N'������ ������ ���������', $60),
	(5, N'�������� �����', $75),
	(6, N'�������� ������� ��������', $50)
GO

INSERT INTO Logbook (Client_ID, Doctor_ID, Service_ID, Reserve_date, Reserve_time)
VALUES 
	(1, 2, 1, '2022-02-01', '09:00:00'),
	(8, 2, 6, '2022-02-01', '10:00:00'),
	(2, 1, 2, '2022-02-01', '11:00:00'),
	(7, 1, 5, '2022-02-01', '12:00:00'),
	(3, 5, 3, '2022-02-01', '13:00:00'),
	(6, 5, 4, '2022-02-01', '14:00:00'),
	(4, 1, 1, '2019-04-29', '17:30:00'),
	(5, 2, 5, '2019-10-04', '09:00:00'),
	(2, 1, 1, '2020-05-14', '16:00:00'),
	(3, 3, 2, '2018-05-16', '14:30:00'),
	(4, 1, 1, '2022-01-27', '13:00:00'),
	(5, 4, 3, '2016-08-31', '10:15:00'),
	(6, 1, 4, '2017-12-14', '18:00:00'),
	(4, 2, 6, '2015-12-22', '11:30:00'),
	(6, 1, 1, '2017-06-02', '12:00:00'),
	(2, 2, 5, '2020-11-12', '15:15:00')
GO


-- ������������� ������� ����� �������

IF OBJECT_ID (N'Logbook_View') IS NOT NULL
	DROP VIEW Logbook_View
GO

CREATE VIEW Logbook_View AS
SELECT l.Procedure_ID, 
	   c.Client_ID, c.Document_ID as Client_Document,
	   d.Doctor_ID, d.Qualification as Doctor_Qualification,
	   l.Service_ID, st.Service_Description, st.Price, 
	   l.Reserve_date, l.Reserve_time
FROM Logbook as l
INNER JOIN  Clients as c
ON l.Client_ID = c.Client_ID
INNER JOIN ServiceType as st
ON l.Service_ID = st.Service_ID
INNER JOIN Doctors as d
ON d.Doctor_ID = l.Doctor_ID
GO


-- �������� ��� ������������� Logbook

IF OBJECT_ID(N'Logbook_view_delete') IS NOT NULL
	DROP TRIGGER Logbook_view_delete
GO

IF OBJECT_ID(N'Logbook_view_insert') IS NOT NULL
	DROP TRIGGER Logbook_view_insert
GO

IF OBJECT_ID(N'Logbook_view_update') IS NOT NULL
	DROP TRIGGER Logbook_view_update
GO

CREATE TRIGGER Logbook_view_delete
	ON Logbook_View
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM Logbook WHERE Procedure_ID IN (SELECT Procedure_ID FROM deleted)
	END
GO

CREATE TRIGGER Logbook_view_insert
	ON Logbook_view
	INSTEAD OF INSERT
AS
	BEGIN
		IF (SELECT COUNT(*) FROM inserted as i WHERE i.Client_ID NOT IN (SELECT Client_ID FROM Clients)) != 0
			BEGIN
				EXEC sp_addmessage 50001, 15, N'�������������� ����� �������!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50001, 15, -1)
			END
		ELSE
		IF (SELECT COUNT(*) FROM inserted as i WHERE i.Doctor_ID NOT IN (SELECT Doctor_ID FROM Doctors)) != 0
			BEGIN
				EXEC sp_addmessage 50002, 15, N'�������������� ����� �����!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50002, 15, -1)
			END
		ELSE
		IF (SELECT COUNT(*) FROM inserted as i WHERE i.Service_ID NOT IN (SELECT Service_ID FROM ServiceType)) != 0
			BEGIN
				EXEC sp_addmessage 50003, 15, N'�������������� ����� ������!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50003, 15, -1)
			END
		ELSE
			BEGIN
				INSERT INTO Logbook
				SELECT Client_ID, Doctor_ID, Service_ID, Reserve_date, Reserve_time
				FROM inserted
			END
	END
GO

CREATE TRIGGER Logbook_view_update
	ON Logbook_view
	INSTEAD OF UPDATE
AS
	BEGIN
		IF UPDATE (Procedure_ID)
			BEGIN
				EXEC sp_addmessage 50004, 15, N'��������� ������ ����� ������!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50004, 15, -1)
			END
		ELSE
			BEGIN
				UPDATE Logbook SET Client_ID = i.Client_ID, 
								   Doctor_ID = i.Doctor_ID, 
								   Service_ID = i.Service_ID,
								   Reserve_date = i.Reserve_date,
								   Reserve_time = i.Reserve_time
				FROM inserted as i, Logbook as l 
				WHERE i.Procedure_ID = l.Procedure_ID
			END
	END
GO


-- ������� � �������������

SELECT * FROM Logbook_View
INSERT INTO Logbook_View (Client_ID, Doctor_ID, Service_ID, Reserve_date, Reserve_time)
VALUES (1, 1, 1, '2022-02-14', '15:00:00')
SELECT * FROM Logbook_View
GO

-- �������������� ������ �������

INSERT INTO Logbook_View (Client_ID, Doctor_ID, Service_ID, Reserve_date, Reserve_time)
VALUES (20, 1, 1, '2022-02-14', '10:00:00')
GO

INSERT INTO Logbook_View (Client_ID, Doctor_ID, Service_ID, Reserve_date, Reserve_time)
VALUES (2, 20, 1, '2022-02-14', '10:00:00')
GO

INSERT INTO Logbook_View (Client_ID, Doctor_ID, Service_ID, Reserve_date, Reserve_time)
VALUES (2, 2, 20, '2022-02-14', '10:00:00')
GO

-- ���������� ������������� 

SELECT * FROM Logbook_View
UPDATE Logbook_View SET Client_ID = Client_ID + 1 WHERE Client_ID < 8
SELECT * FROM Logbook_View
GO

-- �������������� ������ ����������

UPDATE Logbook_View SET Procedure_ID = 100 + Procedure_ID WHERE Client_ID IN (1, 3, 5)
GO

-- ��������� �������

SELECT DISTINCT Service_Description FROM Logbook_View
GO

SELECT * FROM Clients WHERE Secondname IS NULL
GO

SELECT * FROM Workers WHERE Secondname IS NOT NULL
GO

SELECT * FROM Clients WHERE Date_of_birth BETWEEN '1985-01-01' AND '1990-01-01'
GO

SELECT * FROM Clients WHERE Date_of_birth LIKE '1996%'
GO

SELECT * FROM Logbook_View WHERE Price BETWEEN 20 AND 50
GO

SELECT Procedure_ID, Service_Description, Price, Reserve_date, Reserve_time
FROM Logbook_View ORDER BY Price ASC

SELECT Procedure_ID, Service_Description, Price, Reserve_date, Reserve_time
FROM Logbook_View ORDER BY Reserve_date DESC

SELECT Service_Description, AVG(Price) as AVG_Price
FROM Logbook_View
GROUP BY Service_Description
HAVING AVG(Price) > 30
GO

SELECT Surname, Name, Secondname, MAX(Reserve_time) as Max_Reserve_time
FROM Logbook_View as l
INNER JOIN Clients as c
ON l.Client_ID = c.Client_ID
GROUP BY Surname, Name, Secondname
HAVING MIN(Reserve_time) > '10:00:00'
GO

SELECT Surname, Name, Secondname, SUM(Price) as Total_price, COUNT(Price) as Visit
FROM Logbook_View as l
INNER JOIN Clients as c
ON l.Client_ID = c.Client_ID
GROUP BY Surname, Name, Secondname
GO

SELECT * FROM Clients WHERE Client_ID BETWEEN 4 AND 6
UNION 
SELECT * FROM Clients WHERE Client_ID IN (3, 5, 6)
ORDER BY Client_ID
GO

SELECT * FROM Clients WHERE Client_ID BETWEEN 4 AND 6
UNION ALL
SELECT * FROM Clients WHERE Client_ID IN (3, 5)
ORDER BY Client_ID
GO

SELECT * FROM Clients WHERE Client_ID BETWEEN 2 AND 6
EXCEPT
SELECT * FROM Clients WHERE Client_ID IN (3, 5)
ORDER BY Client_ID
GO

SELECT * FROM Clients WHERE Client_ID BETWEEN 2 AND 6
INTERSECT
SELECT * FROM Clients WHERE Client_ID BETWEEN 4 AND 8
ORDER BY Client_ID
GO 

SELECT Procedure_ID, Price, Reserve_date, Reserve_time FROM Logbook_View
	WHERE EXISTS (SELECT Price WHERE Price > $40)
GO

-- �������� �� �������������

SELECT * FROM Logbook_View
DELETE FROM Logbook_View WHERE Procedure_ID < 12
SELECT * FROM Logbook_View
GO

-- JOIN

SELECT * FROM Clients as c
LEFT JOIN Logbook as l
ON c.Client_ID = l.Client_ID
GO

SELECT * FROM Logbook as l
RIGHT JOIN Workers as c
ON c.Worker_ID = l.Doctor_ID
GO

SELECT * FROM Clients as c
FULL OUTER JOIN Workers as w
ON c.Surname = w.Surname
GO