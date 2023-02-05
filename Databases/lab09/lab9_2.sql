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
	TIN nvarchar(20) UNIQUE NOT NULL,
	Worker_ID int PRIMARY KEY NOT NULL, 
	CONSTRAINT FK_Workers FOREIGN KEY (Worker_ID) REFERENCES Workers(Worker_ID)
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

-- 1. Для одной из таблиц создать
--    триггеры на вставку, удаление и добавление, при
--    выполнении заданных условий один из триггеров
--    должен инициировать возникновение ошибки
--    (RAISERROR / THROW).

IF OBJECT_ID(N'Delete_workers') IS NOT NULL
	DROP TRIGGER Delete_workers
GO

CREATE TRIGGER Delete_workers
	ON Workers
	AFTER DELETE
AS
	BEGIN
		DECLARE @count int
		SET @count = (SELECT COUNT(*) FROM deleted)
		IF @count > 1
			PRINT 'Записи сотрудников (' + CAST(@count AS nvarchar(1)) + ') удалены!'
		IF @count = 1
			PRINT 'Запись сотрудника удалена!'
	END
GO

IF OBJECT_ID(N'Update_workers') IS NOT NULL
	DROP TRIGGER Update_workers
GO

CREATE TRIGGER Update_workers
	ON Workers
	AFTER UPDATE
AS
	BEGIN
		DECLARE @count int
		SET @count = (SELECT COUNT(*) FROM inserted)
		IF @count > 1
			PRINT 'Записи сотрудников (' + CAST(@count AS nvarchar(1)) + ') обновлены!'
		IF @count = 1
			PRINT 'Запись сотрудника обновлена!'
	END
GO

IF OBJECT_ID(N'Insert_workers') IS NOT NULL
	DROP TRIGGER Insert_workers
GO

CREATE TRIGGER Insert_workers
	ON Workers
	INSTEAD OF INSERT
AS
	BEGIN
		DECLARE @workers_count_before int
		DECLARE @workers_count_after int
		DECLARE @inserted int
		SET @workers_count_before = (SELECT COUNT(*) FROM Workers)
		SET @inserted = (SELECT COUNT(*) FROM inserted)

		INSERT INTO Workers
		SELECT i.Worker_ID, i.Surname, i.Name, i.Lastname
		FROM inserted as i
		WHERE i.Worker_ID NOT IN (SELECT Worker_ID FROM Workers)

		SET @workers_count_after = (SELECT COUNT(*) FROM Workers)

		IF @workers_count_before + @inserted > @workers_count_after
			BEGIN
				EXEC sp_addmessage 50001, 15, N'Попытка добавить существующий ID сотрудника!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50001, 15, -1)
			END
	END
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
		IF ((SELECT COUNT(*) FROM inserted as i WHERE i.Worker_ID IN (SELECT Worker_ID FROM Doctors_view)) != 0)
			BEGIN
				EXEC sp_addmessage 50010, 15, N'Номер сотрудника неоригинальный!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50010, 15, -1)
			END
		ELSE
			BEGIN
				INSERT INTO Workers
				SELECT Worker_ID, Surname, Name, Lastname
				FROM inserted

				INSERT INTO Doctors
				SELECT TIN, Worker_ID, Qualification
				FROM inserted
			END
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
		IF UPDATE(Worker_ID)
			BEGIN
				EXEC sp_addmessage 50010, 15, N'Запрещено менять номер работника!', @lang='us_english', @replace='REPLACE'
				RAISERROR(50010, 15, -1)
			END
		ELSE
			BEGIN
				IF UPDATE(TIN)
					UPDATE Doctors SET TIN = i.TIN FROM inserted AS i, Doctors AS d WHERE i.Worker_ID = d.Worker_ID

				IF UPDATE(Qualification)
					UPDATE Doctors SET Qualification = i.Qualification FROM inserted AS i, Doctors AS d WHERE i.Worker_ID = d.Worker_ID

				IF UPDATE(Surname)
					UPDATE Workers SET Surname = i.Surname FROM inserted AS i, Workers AS w WHERE i.Worker_ID = w.Worker_ID

				IF UPDATE(Name)
					UPDATE Workers SET Name = i.Name FROM inserted AS i, Workers AS w WHERE i.Worker_ID = w.Worker_ID

				IF UPDATE(Lastname)
					UPDATE Workers SET Lastname = i.Lastname FROM inserted AS i, Workers AS w WHERE i.Worker_ID = w.Worker_ID

			END
	END
GO

SELECT * FROM Workers
SELECT * FROM Doctors
GO

UPDATE Doctors_view SET Qualification = Qualification + N' AAA'
GO

INSERT INTO Doctors_view
VALUES 
	(1, 'TIN', 'A', 'B', 'C', 'D'),
	(21, 'TIN', 'A', 'B', 'C', 'D')
GO

UPDATE Doctors_view SET Worker_ID = 22 WHERE Worker_ID = 1
GO

UPDATE Doctors_view SET TIN = '065464491589' WHERE Worker_ID = 2
GO

UPDATE Doctors_view SET Qualification = 'Qualification' WHERE Worker_ID = 3
GO

SELECT * FROM Workers
SELECT * FROM Doctors
GO

DELETE FROM Doctors_view WHERE Worker_ID = 4
GO

DELETE FROM Doctors_view WHERE Qualification = 'A'
GO

SELECT * FROM Workers
SELECT * FROM Doctors
GO