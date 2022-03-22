USE master
GO

IF DB_ID (N'lab9') IS NOT NULL
	DROP DATABASE lab9
GO

CREATE DATABASE lab9
ON (
	NAME = lab9dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab9\lab9dat.mdf',
	SIZE = 10,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5
)
LOG ON (
	NAME = lab9log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab9\lab9log.ldf',
	SIZE = 5,
	MAXSIZE = 25,
	FILEGROWTH = 5
)
GO

-- Пересоздадим таблицы из ЛР7 для удобства работы с ними

-- Таблица клиентов

/*

IF OBJECT_ID (N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int PRIMARY KEY,
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

INSERT INTO Clients(Client_ID, Surname, Name, Lastname, Date_of_birth, Gender, Document_type, Document_ID, Telephone)
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

-- Таблица персонала

IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL,
	Qualification nvarchar(20) DEFAULT ('Не указана'),
	Qualification_K numeric(10, 3) DEFAULT (1.0),
		CONSTRAINT check_QK_validity CHECK (Qualification_K >= 1.0)
)
GO

INSERT INTO Workers(Worker_ID, Surname, Name, Lastname, Qualification, Qualification_K)
VALUES 
	(1, N'Алтуфьевский', N'Владислав', N'Михайлович', N'A', 1.1),
	(2, N'Ивлев', N'Павел', N'Константинович', N'B', 1.2),
	(3, N'Раскольников', N'Никита', N'Максимович', N'C', 1.0),
	(4, N'Чехов', N'Владислав', N'Александрович', N'B', 1.15)
GO

-- Таблица типов услуг

IF OBJECT_ID(N'ServiceType') IS NOT NULL
	DROP TABLE ServiceType
GO

CREATE TABLE ServiceType (
	Service_ID int PRIMARY KEY,
	Service_Description nvarchar(100) NOT NULL,
	Price money NOT NULL
)
GO

INSERT INTO ServiceType(Service_ID, Service_Description, Price)
VALUES
	(1, N'Прием врача-стоматолога', $20),
	(2, N'Лечение кариеса', $25),
	(3, N'Лечение периодонтита', $30),
	(4, N'Снятие зубных отложений', $25),
	(5, N'Удаление зубов', $35),
	(6, N'Удаление костных выступов', $35)
GO

-- Таблица записей

IF OBJECT_ID(N'Logbook') IS NOT NULL
	DROP TABLE Logbook
GO

CREATE TABLE Logbook (
	Procedure_ID int PRIMARY KEY,
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

INSERT INTO Logbook(Procedure_ID, Client_ID, Worker_ID, Service_ID, Total_price, Reserve_date, Reserve_time)
VALUES
	(1, 1, 1, 1, $20, '2019-04-29', '17:30:00'),
	(2, 1, 2, 5, $35, '2019-10-04', '09:00:00'),
	(3, 2, 1, 1, $20, '2020-05-14', '16:00:00'),
	(4, 3, 3, 2, $25, '2018-05-16', '14:30:00'),
	(5, 4, 1, 1, $20, '2022-01-27', '13:00:00'),
	(6, 5, 4, 3, $30, '2016-08-31', '10:15:00'),
	(7, 6, 1, 4, $25, '2017-12-14', '18:00:00'),
	(8, 4, 2, 6, $35, '2015-12-22', '11:30:00'),
	(9, 6, 1, 1, $20, '2017-06-02', '12:00:00'),
	(10, 2, 2, 5, $35, '2020-11-12', '15:15:00')
GO


-- 1. Для одной из таблиц создать
--    триггеры на вставку, удаление и добавление, при
--    выполнении заданных условий один из триггеров
--    должен инициировать возникновение ошибки
--    (RAISERROR / THROW).


IF OBJECT_ID(N'Delete_service') IS NOT NULL
	DROP TRIGGER Delete_service
GO

CREATE TRIGGER Delete_service
	ON ServiceType
	INSTEAD OF DELETE
AS
	BEGIN
		IF 'Прием врача-стоматолога' IN (SELECT Service_Description FROM deleted)
			BEGIN
				EXEC sp_addmessage 50001, 15, N'Удаление данной услуги невозможно!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50001, 15, -1)
			END
		ELSE
			BEGIN
				DELETE FROM ServiceType WHERE Service_ID IN (SELECT Service_ID FROM deleted)
				IF (SELECT COUNT(*) FROM deleted) > 1
					PRINT 'Услуги удалены!'
				ELSE
					PRINT 'Услуга удалена!'
			END
	END
GO


--------------------------------------- Примеры --------------------------------------

/*
DELETE FROM ServiceType
	WHERE Service_ID = 1
GO

DELETE FROM ServiceType
	WHERE Service_ID = 3
GO

DELETE FROM ServiceType
	WHERE Service_ID in (2, 4)
GO
*/

--------------------------------------- Примеры --------------------------------------


-- Триггер на обновление

IF OBJECT_ID(N'Update_client') IS NOT NULL
	DROP TRIGGER Update_client
GO

CREATE TRIGGER Update_client
	ON Clients
	AFTER UPDATE
AS
	BEGIN
		IF (UPDATE(Client_ID) AND EXISTS (SELECT TOP 1 Client_ID FROM deleted))
			BEGIN
				EXEC sp_addmessage 50005, 15, N'Невозможно изменить номер клиента. Необходимо создать нового клиента!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50005, 15, -1)
			END

		IF (UPDATE(Surname) AND EXISTS (SELECT TOP 1 Surname FROM deleted))
			OR (UPDATE(Name) AND EXISTS (SELECT TOP 1 Name FROM deleted))
			OR (UPDATE(Lastname) AND EXISTS (SELECT TOP 1 Lastname FROM deleted))
			OR (UPDATE(Gender) AND EXISTS (SELECT TOP 1 Gender FROM deleted))
			BEGIN
				EXEC sp_addmessage 50002, 15, N'Если человек меняет имя, фамилию, отчество или пол необходимо удалить старую запись и создать новую!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50002, 15, -1)
			END
		ELSE
			BEGIN
				DECLARE @compare_table TABLE (
					Client_ID int PRIMARY KEY,
					Add_Surname nvarchar(50), Add_Name nvarchar(50), Add_Lastname nvarchar(50), Add_Date_of_birth date, 
					Add_Gender bit, Add_Document_type int, Add_Document_ID nvarchar(20), Add_Telephone nvarchar(20),
					Delete_Surname nvarchar(50), Delete_Name nvarchar(50), Delete_Lastname nvarchar(50), Delete_Date_of_birth date, 
					Delete_Gender bit, Delete_Document_type int, Delete_Document_ID nvarchar(20), Delete_Telephone nvarchar(20))

					INSERT INTO @compare_table
					SELECT i.Client_ID, 
						   i.Surname, i.Name, i.Lastname, i.Date_of_birth, i.Gender, i.Document_type, i.Document_ID, i.Telephone,
						   d.Surname, d.Name, d.Lastname, d.Date_of_birth, d.Gender, d.Document_type, d.Document_ID, d.Telephone
					FROM inserted i INNER JOIN deleted d ON i.Client_ID = d.Client_ID

					IF UPDATE(Document_type)
						PRINT N'Изменен тип документа!'
					IF UPDATE(Document_ID)
						PRINT N'Изменен номер документа!'
					IF UPDATE(Telephone)
						PRINT N'Изменен номер телефона!'

					SELECT * FROM @compare_table

					DECLARE @count int
					SET @count = (SELECT COUNT(*) FROM @compare_table)
					IF @count > 1
						PRINT N'(у ' + CAST(@count AS nvarchar(1)) + ' клиентов)'
					IF @count = 1
						PRINT N'(у одного клиента)'
			END
	END
GO


--------------------------------------- Примеры --------------------------------------

/*
UPDATE Clients SET Client_ID = 2 
	WHERE Client_ID = 1
GO

UPDATE Clients SET Surname = 'new_surname'
	WHERE Client_ID = 2
GO

UPDATE Clients SET Document_ID = '3026 600848'
	WHERE Client_ID = 3
GO
*/

--------------------------------------- Примеры --------------------------------------


-- Триггер на вставку

IF OBJECT_ID(N'Add_worker') IS NOT NULL
	DROP TRIGGER Add_worker
GO

CREATE TRIGGER Add_worker
	ON Workers
	AFTER INSERT
AS
	BEGIN
		DECLARE @count int
		SET @count = (SELECT COUNT(*) FROM inserted)
		IF @count > 1
			PRINT 'Добавлены новые сотрудники!'
		IF @count = 1
			PRINT 'Добавлен новый сотрудник!'
	END
GO

--------------------------------------- Примеры --------------------------------------

/*
INSERT INTO Workers(Worker_ID, Surname, Name, Lastname, Qualification, Qualification_K)
VALUES (5, N'Добавил', N'одного', N'сотрудника', N'1', 1.2)
GO

INSERT INTO Workers(Worker_ID, Surname, Name, Lastname, Qualification, Qualification_K)
VALUES 
	(6, N'Добавил', N'первого', N'сотрудника', N'1', 1.1),
	(7, N'Добавил', N'второго', N'сотрудника', N'1', 1.1)
GO
*/

--------------------------------------- Примеры --------------------------------------


-- 2. Для представления создать триггеры на вставку, 
--    удаление и добавление, обеспечивающие возможность 
--    выполнения операций с данными непосредственно через
--    представление.

-- Представление

IF OBJECT_ID(N'LogbookView') IS NOT NULL
	DROP VIEW LogbookView
GO

-- ***

CREATE VIEW LogbookView AS
	SELECT 
		l.Procedure_ID, l.Client_ID, l.Worker_ID, 
		l.Service_ID, l.Total_price, l.Reserve_date, 
		l.Reserve_time
	FROM Logbook as l 
GO

-- Триггер на удаление

IF OBJECT_ID(N'Delete_view_logbook') IS NOT NULL
	DROP TRIGGER Delete_view_logbook
GO

CREATE TRIGGER Delete_view_logbook
	ON LogbookView
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM Logbook	WHERE Procedure_ID IN (SELECT Procedure_ID FROM deleted)
		DECLARE @count int
		SET @count = (SELECT DISTINCT COUNT(*) FROM deleted)
		IF @count > 1
			PRINT 'Записи удалены!'
		IF @count = 1
			PRINT 'Запись удалена!'
	END
GO


--------------------------------------- Примеры --------------------------------------

/*
DELETE FROM LogbookView
	WHERE Procedure_ID = 2
GO

DELETE FROM LogbookView 
	WHERE Worker_ID = 1
GO
*/

--------------------------------------- Примеры --------------------------------------


-- Триггер на обновление

IF OBJECT_ID(N'Update_view_logbook') IS NOT NULL
	DROP TRIGGER Update_view_logbook
GO

CREATE TRIGGER Update_view_logbook
	ON LogbookView
	INSTEAD OF UPDATE
AS
	BEGIN
		DECLARE @new_table TABLE (
			Add_Procedure_ID int, Add_Client_ID int, Add_Worker_ID int, 
			Add_Service_ID int, Add_Total_price money, Add_Reserve_date date, 
			Add_Reserve_time time,
			Delete_Procedure_ID int, Delete_Client_ID int, Delete_Worker_ID int, 
			Delete_Service_ID int, Delete_Total_price money, Delete_Reserve_date date, 
			Delete_Reserve_time time)

		IF UPDATE(Client_ID)
			BEGIN
				EXEC sp_addmessage 50004, 15, N'Запрещено менять клиента на назначенный прием!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50004, 15, -1)
			END

		IF UPDATE(Service_ID)
			BEGIN
				EXEC sp_addmessage 50005, 15, N'Запрещено менять услугу на назначенный прием!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50005, 15, -1)
			END

		INSERT INTO @new_table
		SELECT i.Procedure_ID, i.Client_ID, i.Worker_ID, 
		       i.Service_ID, i.Total_price, i.Reserve_date, i.Reserve_time,
			   d.Procedure_ID, d.Client_ID, d.Worker_ID, 
			   d.Service_ID, d.Total_price, d.Reserve_date, d.Reserve_time
		FROM inserted i
		INNER JOIN deleted d ON (i.Client_ID = d.Client_ID AND i.Service_ID = d.Service_ID)
		WHERE (UPDATE(Worker_ID) AND i.Worker_ID IN (SELECT Worker_ID FROM Workers)) OR
		      (UPDATE(Procedure_ID) AND i.Procedure_ID NOT IN (SELECT Procedure_ID FROM Logbook)) OR
			  (UPDATE(Reserve_date) AND i.Reserve_date >= CONVERT(date, GETDATE())) OR
			  (UPDATE(Reserve_time) AND i.Reserve_date = CONVERT(date, GETDATE()) AND i.Reserve_time BETWEEN CONVERT(time, GETDATE()) AND '18:00:00') OR
			  (UPDATE(Reserve_time) AND i.Reserve_date > CONVERT(date, GETDATE()) AND i.Reserve_time BETWEEN '07:00:00' AND '18:00:00') OR
			  (UPDATE(Total_price)) AND
			  (NOT UPDATE(Client_ID)) AND
			  (NOT UPDATE(Service_ID)) 

		SELECT * FROM @new_table

		IF UPDATE(Worker_ID)
			BEGIN
				UPDATE Logbook SET Worker_ID = (SELECT TOP 1 Worker_ID FROM inserted) WHERE Procedure_ID IN (SELECT Delete_Procedure_ID FROM @new_table)
				PRINT N'Был выбран другой врач!'
			END

		IF UPDATE(Procedure_ID)
			BEGIN
				UPDATE Logbook SET Procedure_ID = (SELECT TOP 1 Procedure_ID FROM inserted) WHERE Procedure_ID IN (SELECT Delete_Procedure_ID FROM @new_table)
				PRINT N'Изменен номер записи в таблице!'
			END
		
		IF UPDATE(Total_price)
			BEGIN
				UPDATE Logbook SET Total_price = (SELECT TOP 1 Total_price FROM inserted) WHERE Procedure_ID IN (SELECT Delete_Procedure_ID FROM @new_table)
				PRINT N'Изменена конечная цена услуги!'
			END

		IF UPDATE(Reserve_date)
			BEGIN
				UPDATE Logbook SET Reserve_date = (SELECT TOP 1 Reserve_date FROM inserted) WHERE Procedure_ID IN (SELECT Delete_Procedure_ID FROM @new_table)
				PRINT N'Изменена дата записи!'
			END

		IF UPDATE(Reserve_time)
			BEGIN
				UPDATE Logbook SET Reserve_time = (SELECT TOP 1 Reserve_time FROM inserted) WHERE Procedure_ID IN (SELECT Delete_Procedure_ID FROM @new_table)
				PRINT N'Изменено время записи!'
			END
	END
GO


--------------------------------------- Примеры --------------------------------------

/*
-- Можно

UPDATE LogbookView SET Worker_ID = 2
	WHERE Worker_ID = 1
GO

UPDATE LogbookView SET Procedure_ID = 15
	WHERE Procedure_ID = 5
GO

UPDATE LogbookView SET Total_price = $100
	WHERE Procedure_ID = 15
GO

UPDATE LogbookView SET Reserve_date = '2022-02-01'
	WHERE Procedure_ID = 15
GO

UPDATE LogbookView SET Reserve_time = '15:00:00'
	WHERE Procedure_ID = 15
GO


-- Нельзя

UPDATE LogbookView SET Client_ID = 2 
	WHERE Client_ID = 1
GO

UPDATE LogbookView SET Service_ID = 3
	WHERE Service_ID = 2
GO

UPDATE LogbookView SET Reserve_time = '22:00:00'
	WHERE Procedure_ID = 15
GO
*/

--------------------------------------- Примеры --------------------------------------

-- Триггер на вставку


IF OBJECT_ID(N'Add_view_logbook') IS NOT NULL
	DROP TRIGGER Add_view_logbook
GO

CREATE TRIGGER Add_view_logbook
	ON LogbookView
	INSTEAD OF INSERT
AS
	BEGIN
		DECLARE @new_table TABLE (
			Add_Procedure_ID int, Add_Client_ID int, Add_Worker_ID int, Add_Service_ID int, Add_Total_price money, Add_Reserve_date date, Add_Reserve_time time)

		INSERT INTO @new_table
		SELECT i.Procedure_ID, i.Client_ID, i.Worker_ID, i.Service_ID, i.Service_ID, i.Reserve_date, i.Reserve_time
		FROM inserted as i
		WHERE i.Worker_ID IN (SELECT Worker_ID FROM Workers) AND
		      i.Client_ID IN (SELECT Client_ID FROM Clients) AND
		      i.Service_ID IN (SELECT Service_ID FROM ServiceType) AND
		      i.Procedure_ID NOT IN (SELECT Procedure_ID FROM Logbook) AND 
			  i.Reserve_date >= CONVERT(date, GETDATE()) AND
			  (
				i.Reserve_date > CONVERT(date, GETDATE()) AND i.Reserve_time BETWEEN '07:00:00' AND '18:00:00' OR
				i.Reserve_date = CONVERT(date, GETDATE()) AND i.Reserve_time BETWEEN CONVERT(time, GETDATE()) AND '18:00:00'
			  )

		SELECT * FROM @new_table

		INSERT INTO Logbook
			SELECT * FROM @new_table

		DECLARE @count int
		SET @count = (SELECT DISTINCT COUNT(*) FROM @new_table)
		IF @count > 1
			PRINT 'Добавлены новые записи!'
		IF @count = 1
			PRINT 'Добавлена новая запись!'
	END
GO


--------------------------------------- Примеры --------------------------------------

/*
-- Можно

INSERT INTO LogbookView
VALUES (20, 1, 2, 1, $50, '2022-03-01', '10:00:00')
GO

INSERT INTO LogbookView
VALUES 
	(21, 1, 2, 1, $55, '2022-03-01', '11:00:00'),
	(22, 1, 2, 1, $55, '2022-03-01', '12:00:00'),
	(23, 1, 2, 1, $55, '2022-03-01', '13:00:00')
GO


-- Нельзя

INSERT INTO LogbookView
VALUES (1, 1, 2, 1, $1, '2050-01-01', '11:00:00')
GO

INSERT INTO LogbookView
VALUES (24, 15, 2, 1, $1, '2050-01-01', '11:00:00')
GO

INSERT INTO LogbookView
VALUES (24, 1, 20, 1, $1, '2050-01-01', '11:00:00')
GO

INSERT INTO LogbookView
VALUES (24, 1, 2, 15, $1, '2050-01-01', '11:00:00')
GO

INSERT INTO LogbookView
VALUES (24, 1, 2, 1, $1, '1750-01-01', '11:00:00')
GO

INSERT INTO LogbookView
VALUES (24, 1, 2, 1, $1, '2050-01-01', '21:00:00')
GO
*/

--------------------------------------- Примеры --------------------------------------

*/

USE lab9
GO

IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Workers (
	Worker_ID int PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT NULL
)
GO

IF OBJECT_ID(N'Doctors') IS NOT NULL
	DROP TABLE Doctors
GO

CREATE TABLE Doctors (
	TIN nvarchar(20) PRIMARY KEY NOT NULL,
	Worker_ID int NOT NULL, 
	CONSTRAINT FK_Clients FOREIGN KEY (Worker_ID) REFERENCES Workers(Worker_ID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	Qualification nvarchar(20) DEFAULT ('Не указана'),
)


INSERT INTO Workers
VALUES 
	(1, N'Алтуфьевский', N'Владислав', N'Михайлович'),
	(2, N'Ивлев', N'Павел', N'Константинович'),
	(3, N'Раскольников', N'Никита', N'Максимович'),
	(4, N'Чехов', N'Владислав', N'Александрович')
GO

INSERT INTO Doctors
VALUES 
	('508844388003', 1, N'A'),
	('799839630380', 2, N'B'),
	('457092805784', 3, N'C'),
	('587435777897', 4, N'B')
GO

-- 2. Для представления создать триггеры на вставку, 
--    удаление и добавление, обеспечивающие возможность 
--    выполнения операций с данными непосредственно через
--    представление.

IF OBJECT_ID(N'Doctors_view') IS NOT NULL
	DROP VIEW Doctors_view
GO

CREATE VIEW Doctors_view AS
	SELECT w.Worker_ID, d.TIN, w.Surname, w.Name, w.Lastname, d.Qualification
	FROM Doctors as d
	INNER JOIN Workers as w
	ON d.Worker_ID = w.Worker_ID
GO

-- Триггер на удаление

IF OBJECT_ID(N'Delete_doctors_view') IS NOT NULL
	DROP TRIGGER Delete_doctors_view
GO

CREATE TRIGGER Delete_doctors_view
	ON Doctors_view
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM Workers	WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)
		DELETE FROM Doctors WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)

		DECLARE @count int
		SET @count = (SELECT COUNT(*) FROM deleted)
		IF @count > 1
			PRINT 'Записи сотрудников удалены!'
		IF @count = 1
			PRINT 'Запись сотрудника удалена!'
	END
GO


-- Триггер на вставку

IF OBJECT_ID(N'Add_doctors_view') IS NOT NULL
	DROP TRIGGER Add_doctors_view
GO

CREATE TRIGGER Add_doctors_view
	ON Doctors_view
	INSTEAD OF INSERT
AS
	BEGIN
		DECLARE @new_table TABLE (
			Add_Worker_ID int, 
			Add_TIN nvarchar(20), 
			Add_Surname nvarchar(50), 
			Add_Name nvarchar(50), 
			Add_Lastname nvarchar(50), 
			Add_Qualification nvarchar(20))

		INSERT INTO @new_table
		SELECT i.Worker_ID, i.TIN, i.Surname, i.Name, i.Lastname, i.Qualification
		FROM inserted as i
		WHERE i.Worker_ID NOT IN (SELECT Worker_ID FROM Workers)

		INSERT INTO Workers
		SELECT Add_Worker_ID, Add_Surname, Add_Name, Add_Lastname
		FROM @new_table

		INSERT INTO Doctors
		SELECT Add_Worker_ID, Add_TIN, Add_Qualification
		FROM @new_table

		DECLARE @count int
		SET @count = (SELECT COUNT(*) FROM @new_table)
		IF @count > 1
			PRINT 'Добавлены новые записи!'
		IF @count = 1
			PRINT 'Добавлена новая запись!'
	END
GO


-- Триггер на обновление

IF OBJECT_ID(N'Update_doctors_view') IS NOT NULL
	DROP TRIGGER Update_doctors_view
GO

CREATE TRIGGER Update_doctors_view
	ON Doctors_view
	INSTEAD OF UPDATE
AS
	BEGIN
		DECLARE @new_table TABLE (
			Add_Worker_ID int, Add_TIN nvarchar(20), Add_Surname nvarchar(50), Add_Name nvarchar(50), Add_Lastname nvarchar(50), Add_Qualification nvarchar(20),
			Delete_Worker_ID int, Delete_TIN nvarchar(20), Delete_Surname nvarchar(50), Delete_Name nvarchar(50), Delete_Lastname nvarchar(50), Delete_Qualification nvarchar(20))

		IF UPDATE(Worker_ID)
			BEGIN
				EXEC sp_addmessage 50010, 15, N'Запрещено менять номер работника!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50010, 15, -1)
			END
		ELSE
			BEGIN
				IF UPDATE(TIN)
					BEGIN
						UPDATE Doctors SET TIN = (SELECT TOP 1 TIN FROM inserted) WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)
						PRINT N'ИНН врача изменен!'
					END

				IF UPDATE(Surname)
					BEGIN
						UPDATE Workers SET Surname = (SELECT TOP 1 Surname FROM inserted) WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)
						PRINT N'Фамилия врача изменена!'
					END

				IF UPDATE(Name)
					BEGIN
						UPDATE Workers SET Name = (SELECT TOP 1 Name FROM inserted) WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)
						PRINT N'Имя врача изменено!'
					END

				IF UPDATE(Lastname)
					BEGIN 
						UPDATE Workers SET Lastname = (SELECT TOP 1 Lastname FROM inserted) WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)
						PRINT N'Отчество врача изменено!'
					END

				IF UPDATE(Qualification)
					BEGIN
						UPDATE Doctors SET Qualification = (SELECT TOP 1 Qualification FROM inserted) WHERE Worker_ID IN (SELECT Worker_ID FROM deleted)
						PRINT N'Квалификация врача изменена!'
					END
			END
	END
GO

INSERT INTO Doctors_view
VALUES 
	(1, 'a', 'b', 'c', 'd'),
	(2, 'a', 'b', 'c', 'd')
GO