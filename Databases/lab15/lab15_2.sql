USE master
GO

IF DB_ID(N'lab15_1') IS NOT NULL
	DROP DATABASE lab15_1
GO

IF DB_ID(N'lab15_2') IS NOT NULL
	DROP DATABASE lab15_2
GO

CREATE DATABASE lab15_1
ON (
	NAME = lab15_1dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab15\lab15_1dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab15_1log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab15\lab15_1log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO

CREATE DATABASE lab15_2
ON (
	NAME = lab15_2dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab15\lab15_2dat.mdf',
	SIZE = 20,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab15_2log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab15\lab15_2log.ldf',
	SIZE = 10,
	MAXSIZE = 20,
	FILEGROWTH = 5
)
GO


-- Первая база данных 

USE lab15_1
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
	Document_type int NOT NULL,
	Document_ID nvarchar(20) UNIQUE NOT NULL,
)
GO

INSERT INTO Clients
VALUES 
	(1, N'Особов', N'Иван', N'Юрьевич', '1989-07-05', 1, '4285 122897'),
	(2, N'Мармеладов', N'Владислав', N'Максимович', '1997-03-04', 1, '4040 910626'),
	(3, N'Романов', N'Данил', N'Семенович', '1993-10-21', 1, '4237 836539'),
	(4, N'Родзаевский', N'Иван', N'Михайлович', '1984-06-01', 1, '4047 266308'),
	(5, N'Яковлева', N'Валерия', N'Михайловна', '1995-01-06', 1, '4349 869772'),
	(6, N'Попова', N'Вероника', N'Станиславовна', '1992-12-07', 1, '4920 145641'),
	(7, N'Дементьева', N'Дарья', N'Данииловна', '1996-02-21', 1, '4743 278264'),
	(8, N'Миронова', N'Екатерина', N'Антоновна', '1994-12-15', 1, '4712 484969')
GO


-- Вторая база данных

USE lab15_2
GO

IF OBJECT_ID(N'Logbook') IS NOT NULL
	DROP TABLE Logbook
GO

CREATE TABLE Logbook (
	Procedure_ID int PRIMARY KEY,
	Client_ID int NOT NULL, 
	Total_price money NOT NULL,
	Reserve_date date NOT NULL CHECK (Reserve_date >= '2001-01-01'),
	Reserve_time time NOT NULL CHECK (Reserve_time >= '07:00:00' AND Reserve_time <= '18:00:00')
)
GO

INSERT INTO Logbook
VALUES
	(1, 1, $20, '2019-04-29', '17:30:00'),
	(2, 1, $35, '2019-10-04', '09:00:00'),
	(3, 2, $20, '2020-05-14', '16:00:00'),
	(4, 3, $25, '2018-05-16', '14:30:00'),
	(5, 4, $20, '2022-01-27', '13:00:00'),
	(6, 5, $30, '2016-08-31', '10:15:00'),
	(7, 6, $25, '2017-12-14', '18:00:00'),
	(8, 4, $35, '2015-12-22', '11:30:00'),
	(9, 6, $20, '2017-06-02', '12:00:00'),
	(10, 2, $35, '2020-11-12', '15:15:00')
GO


-- Представление Logbook в БД1

USE lab15_1
GO

IF OBJECT_ID(N'Logbook_view') IS NOT NULL
	DROP VIEW Logbook_view
GO

CREATE VIEW Logbook_view
AS
	SELECT * FROM lab15_2.dbo.Logbook
GO

-- Вставка в Logbook_view

IF OBJECT_ID(N'Logbook_view_insert') IS NOT NULL
	DROP TRIGGER Logbook_view_insert
GO

CREATE TRIGGER Logbook_view_insert
	ON Logbook_view
	INSTEAD OF INSERT
AS
	BEGIN
		INSERT INTO lab15_2.dbo.Logbook 
		SELECT * FROM inserted
	END
GO

-- Удаление из Logbook_view

IF OBJECT_ID(N'Lobook_view_delete') IS NOT NULL
	DROP TRIGGER Logbook_view_delete
GO

CREATE TRIGGER Logbook_view_delete
	ON Logbook_view
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM lab15_2.dbo.Logbook WHERE Procedure_ID IN (SELECT Procedure_ID FROM deleted)
	END
GO

-- Обновление Logbook_view

IF OBJECT_ID(N'Logbook_view_update') IS NOT NULL
	DROP TRIGGER Logbook_view_update
GO 

CREATE TRIGGER Logbook_view_update
	ON Logbook_view
	INSTEAD OF UPDATE
AS
	BEGIN
		IF UPDATE(Procedure_ID)
			BEGIN
				EXEC sp_addmessage 50003, 15, N'Нельзя изменить номер записи!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50003, 15, -1)
			END
		ELSE
			BEGIN
				IF UPDATE(Client_ID)
					UPDATE lab15_2.dbo.Logbook SET Client_ID = i.Client_ID FROM inserted as i, lab15_2.dbo.Logbook AS l WHERE i.Procedure_ID = l.Procedure_ID

				IF UPDATE(Total_price)
					UPDATE lab15_2.dbo.Logbook SET Total_price = i.Total_price FROM inserted as i, lab15_2.dbo.Logbook AS l WHERE i.Procedure_ID = l.Procedure_ID

				IF UPDATE(Reserve_date)
					UPDATE lab15_2.dbo.Logbook SET Reserve_date = i.Reserve_date FROM inserted as i, lab15_2.dbo.Logbook AS l WHERE i.Procedure_ID = l.Procedure_ID

				IF UPDATE(Reserve_time)
					UPDATE lab15_2.dbo.Logbook SET Reserve_time = i.Reserve_time FROM inserted as i, lab15_2.dbo.Logbook AS l WHERE i.Procedure_ID = l.Procedure_ID
			END

			
	END
GO

-- Изменение в Clients

IF OBJECT_ID(N'Clients_update') IS NOT NULL
	DROP TRIGGER Clients_update
GO


-- ! --------------------------------------------------------------------------------------------------------------------------------
CREATE TRIGGER Clients_update
	ON Clients
	AFTER UPDATE
AS
	BEGIN
		IF UPDATE(...)

		DECLARE @compare table (New_Client_ID int, Old_Client_ID int)
		INSERT INTO @compare
		SELECT i.Client_ID, d.Client_ID FROM inserted AS i
		INNER JOIN deleted AS d
		ON i.Document_ID = d.Document_ID

		IF UPDATE(Client_ID)
			UPDATE lab15_2.dbo.Logbook SET Client_ID = c.New_Client_ID FROM @compare AS c WHERE Client_ID = c.Old_Client_ID
	END
GO

SELECT * FROM Clients 
UPDATE Clients SET Client_ID = Client_ID + 1
SELECT * FROM Clients 
GO

-- ! --------------------------------------------------------------------------------------------------------------------------------

-- Удаление из Clients

IF OBJECT_ID(N'Clients_delete') IS NOT NULL
	DROP TRIGGER Clients_delete
GO

CREATE TRIGGER Clients_delete
	ON Clients
	AFTER DELETE
AS
	BEGIN
		DELETE FROM lab15_2.dbo.Logbook WHERE Client_ID IN (SELECT Client_ID FROM deleted)
	END
GO


-- Вторая база данных

USE lab15_2
GO

IF OBJECT_ID(N'Clients_view') IS NOT NULL
	DROP VIEW Clients_view
GO

CREATE VIEW Clients_view
AS
	SELECT * FROM lab15_1.dbo.Clients
GO

-- Вставка в Clients_view

IF OBJECT_ID(N'Clients_view_insert') IS NOT NULL
	DROP TRIGGER Clients_view_insert
GO

CREATE TRIGGER Clients_view_insert
	ON Clients_view
	INSTEAD OF INSERT
AS
	BEGIN
		INSERT INTO lab15_1.dbo.Clients SELECT * FROM inserted
	END
GO

-- Обновление Clients_view

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
				EXEC sp_addmessage 50004, 15, N'Запрещено менять номер клиента!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50004, 15, -1)
			END
		ELSE
			BEGIN
				IF UPDATE(Surname)
					UPDATE lab15_1.dbo.Clients SET Surname = i.Surname FROM inserted AS i, lab15_1.dbo.Clients AS c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Name)
					UPDATE lab15_1.dbo.Clients SET Name = i.Name FROM inserted AS i, lab15_1.dbo.Clients AS c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Lastname)
					UPDATE lab15_1.dbo.Clients SET Lastname = i.Lastname FROM inserted AS i, lab15_1.dbo.Clients AS c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Date_of_birth)
					UPDATE lab15_1.dbo.Clients SET Date_of_birth = i.Date_of_birth FROM inserted AS i, lab15_1.dbo.Clients AS c WHERE i.Client_ID = c.Client_ID

				IF UPDATE(Document_type)
					UPDATE lab15_1.dbo.Clients SET Document_type = i.Document_type FROM inserted AS i, lab15_1.dbo.Clients AS c WHERE i.Client_ID = c.Client_ID
				
				IF UPDATE(Document_ID)
					UPDATE lab15_1.dbo.Clients SET Document_ID = i.Document_ID FROM inserted AS i, lab15_1.dbo.Clients AS c WHERE i.Client_ID = c.Client_ID
			END
	END
GO

-- Удаление из Clients_view

IF OBJECT_ID(N'Clients_view_delete') IS NOT NULL
	DROP TRIGGER Clients_view_delete
GO

CREATE TRIGGER Clients_view_delete
	ON Clients_view
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM lab15_1.dbo.Clients WHERE Client_ID IN (SELECT Client_ID FROM deleted)
	END
GO

-- Вставка в Logbook

IF OBJECT_ID(N'Logbook_insert') IS NOT NULL
	DROP TRIGGER Logbook_insert
GO

CREATE TRIGGER Logbook_insert
	ON Logbook
	INSTEAD OF INSERT
AS
	BEGIN
		IF ((SELECT COUNT(*) FROM inserted as i WHERE i.Client_ID NOT IN (SELECT Client_ID FROM lab15_1.dbo.Clients)) != 0)
			BEGIN
				EXEC sp_addmessage 50001, 15, N'Номер клиента не существует!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50001, 15, -1)
			END
		ELSE
			BEGIN
				INSERT INTO Logbook 
				SELECT * FROM inserted
			END
	END
GO

-- Изменение Logbook

IF OBJECT_ID(N'Logbook_update') IS NOT NULL
	DROP TRIGGER Logbook_update
GO

CREATE TRIGGER Logbook_update
	ON Logbook
	INSTEAD OF UPDATE
AS
	BEGIN
		IF UPDATE(Procedure_ID)
			BEGIN
				EXEC sp_addmessage 50003, 15, N'Нельзя изменить номер записи!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50003, 15, -1)
			END
		ELSE
		IF UPDATE(Client_ID) AND ((SELECT COUNT(*) FROM inserted as i WHERE i.Client_ID NOT IN (SELECT Client_ID FROM lab15_1.dbo.Clients)) != 0) 
			BEGIN
				EXEC sp_addmessage 50002, 15, N'Номер клиента не существует!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50002, 15, -1)
			END
		ELSE
			BEGIN
				IF UPDATE(Client_ID)
					UPDATE lab15_2.dbo.Logbook SET Client_ID = i.Client_ID FROM inserted AS i, lab15_2.dbo.Logbook as l WHERE i.Procedure_ID = l.Procedure_ID

				IF UPDATE(Total_price)
					UPDATE lab15_2.dbo.Logbook SET Total_price = i.Total_price FROM inserted AS i, lab15_2.dbo.Logbook as l WHERE i.Procedure_ID = l.Procedure_ID

				IF UPDATE(Reserve_date)
					UPDATE lab15_2.dbo.Logbook SET Reserve_date = i.Reserve_date FROM inserted AS i, lab15_2.dbo.Logbook as l WHERE i.Procedure_ID = l.Procedure_ID

				IF UPDATE(Reserve_time)
					UPDATE lab15_2.dbo.Logbook SET Reserve_time = i.Reserve_time FROM inserted AS i, lab15_2.dbo.Logbook as l WHERE i.Procedure_ID = l.Procedure_ID
			END
	END
GO












/*
-- Примеры

USE lab15_1
GO

-- Вставка

SELECT * FROM Logbook_view
GO

INSERT INTO Logbook_view
VALUES
	(11, 1, $20, '2022-02-15', '07:00:00'),
	(12, 2, $20, '2022-02-15', '08:00:00'),
	(13, 3, $20, '2022-02-15', '09:00:00')
GO

-- Обновление Logbook_view

UPDATE Logbook_view SET Procedure_ID = 2 WHERE Procedure_ID = 3
GO

UPDATE Logbook_view SET Client_ID = 20 WHERE Client_ID = 1
GO

UPDATE Logbook_view SET Procedure_ID = 15 WHERE Procedure_ID = 1
GO

UPDATE Logbook_view SET Total_price = Total_price + $52 WHERE Procedure_ID = 15
GO

UPDATE Logbook_view SET Reserve_date = '2022-03-01' WHERE Procedure_ID = 15
GO

UPDATE Logbook_view SET Reserve_time = '12:34:50' WHERE Procedure_ID = 15
GO

SELECT * FROM Logbook_view
GO

-- Удаление из Logbook_view

DELETE FROM Logbook_view WHERE Client_ID BETWEEN 11 AND 13
GO

-- Обновление Clients

UPDATE Clients SET Client_ID = 20 WHERE Client_ID = 3
GO

-- Удаление из Clients

DELETE FROM Clients WHERE Client_ID IN (1, 2, 5, 6)
SELECT * FROM Logbook_view
GO





USE lab15_2
GO

-- Удаление из Clients_view

SELECT * FROM lab15_1.dbo.Clients
DELETE FROM Clients_view WHERE Client_ID BETWEEN 5 AND 8
SELECT * FROM lab15_1.dbo.Clients
GO

-- Обновление Clients_view

UPDATE Clients_view SET Client_ID = 15 WHERE Client_ID = 1
GO

UPDATE Clients_view SET Date_of_birth = '2000-01-01' WHERE Client_ID = 15
GO

-- Вставка в Clients_view

INSERT INTO Clients_view
VALUES (9, 'A', 'B', 'C', '2000-01-01', 1, '1234 567890')
GO

SELECT * FROM Clients_view
GO
*/