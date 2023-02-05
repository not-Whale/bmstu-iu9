USE master
GO

IF DB_ID(N'lab8') IS NOT NULL
	DROP DATABASE lab8
GO

CREATE DATABASE lab8
ON (
	NAME = lab8dat,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab8\lab8dat.mdf',
	SIZE = 10,
	MAXSIZE = UNLIMITED,
	FILEGROWTH = 5%
)
LOG ON (
	NAME = lab8log,
	FILENAME = 'C:\Users\Admin\Desktop\Никита\5 семестр\БД\lab8\lab8log.ldf',
	SIZE = 5,
	MAXSIZE = 25,
	FILEGROWTH = 5
)
GO


USE lab8
GO

IF OBJECT_ID (N'Clients') IS NOT NULL
	DROP TABLE Clients
GO

CREATE TABLE Clients (
	Client_ID int IDENTITY(1,1) PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) DEFAULT '-', 
	Date_of_birth date NOT NULL CHECK (Date_of_birth > '1850-01-01' AND Date_of_birth < CONVERT(date, GETDATE())), 
	Gender bit,
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


IF OBJECT_ID (N'Workers') IS NOT NULL
	DROP TABLE Workers
GO

CREATE TABLE Workers (
	Worker_ID int IDENTITY(1,1) PRIMARY KEY,
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL,
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
			-- ? ON DELETE поиск новой родительской записи
			ON UPDATE CASCADE,
	Service_ID int NOT NULL,
		CONSTRAINT FK_ServiceType FOREIGN KEY (Service_ID) REFERENCES ServiceType(Service_ID)
			ON DELETE CASCADE
			ON UPDATE CASCADE,
	Total_price money NOT NULL,
		-- ? функция вычисления
	Reserve_date date NOT NULL CHECK (Reserve_date >= '2001-01-01'),
		-- ? CHECK (Reserve_date >= CONVERT(date, GETDATE()) - '0010-01-01'),
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


-- 1. Создать хранимую процедуру, производящую выборку
--    из некоторой таблицы и возвращающую результат
--    выборки в виде курсора.

USE lab8
GO

IF OBJECT_ID(N'dbo.select_procedure') IS NOT NULL
	DROP PROCEDURE dbo.select_procedure
GO

CREATE PROCEDURE dbo.select_procedure
	@cursor CURSOR VARYING OUTPUT
	AS
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT Surname, Name, Lastname
	FROM Clients
	OPEN @cursor
GO

DECLARE @clients_cursor CURSOR
EXECUTE dbo.select_procedure @cursor = @clients_cursor OUTPUT

FETCH NEXT FROM @clients_cursor
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @clients_cursor
	END

CLOSE @clients_cursor
DEALLOCATE @clients_cursor
GO


-- 2. Модифицировать хранимую процедуру п.1. таким
--    образом, чтобы выборка осуществлялась с
--    формированием столбца, значение которого
--    формируется пользовательской функцией. 

IF OBJECT_ID(N'generate_random') IS NOT NULL
	DROP FUNCTION generate_random
GO

IF OBJECT_ID(N'view_number') IS NOT NULL
	DROP VIEW view_number
GO

CREATE VIEW view_number AS
	SELECT CAST(CAST(NEWID() AS binary(3)) AS INT) AS NextID
GO

CREATE FUNCTION generate_random(@a int,@b int)
	RETURNS int
	AS
		BEGIN
			DECLARE @number int
			SELECT TOP 1 @number = NextID from view_number
			SET @number = @number % @b + @a
			RETURN (@number)
		END
go

IF OBJECT_ID(N'dbo.select_proc_with_new_column', N'P') IS NOT NULL
	DROP PROCEDURE dbo.select_proc_with_new_column
GO

CREATE PROCEDURE dbo.select_proc_with_new_column
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT Surname, Name, Lastname, dbo.generate_random(1, 100) as Client_Random_Number 
	FROM Clients
	OPEN @cursor;
GO

DECLARE @client_number_cursor CURSOR
EXECUTE dbo.select_proc_with_new_column @cursor = @client_number_cursor OUTPUT;

FETCH NEXT FROM @client_number_cursor
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @client_number_cursor
	END

CLOSE @client_number_cursor
DEALLOCATE @client_number_cursor
GO


-- 3. Создать хранимую процедуру, вызывающую процедуру
--    п.1., осуществляющую прокрутку возвращаемого
--    курсора и выводящую сообщения, сформированные из
--    записей при выполнении условия, заданного еще одной
--    пользовательской функцией.

IF OBJECT_ID(N'check_number',N'FN') IS NOT NULL
	DROP FUNCTION check_number
go

CREATE FUNCTION check_number(@a int)
	RETURNS bit
	AS
		BEGIN
			DECLARE @result bit
			IF (@a <= 50)
				SET @result = 1
			ELSE
				SET @result = 0
			RETURN (@result)
		END
go

IF OBJECT_ID(N'dbo.checking_procedure') IS NOT NULL
	DROP PROCEDURE dbo.checking_procedure
GO

CREATE PROCEDURE dbo.checking_procedure
AS
	DECLARE @checking_cursor CURSOR
	DECLARE @client_surname nvarchar(50)
	DECLARE @client_name nvarchar(50)
	DECLARE @client_lastname nvarchar(50)
	DECLARE @client_number int

	EXECUTE dbo.select_proc_with_new_column @cursor = @checking_cursor OUTPUT

	FETCH NEXT FROM @checking_cursor INTO @client_surname, @client_name, @client_lastname, @client_number

	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		IF (dbo.check_number (@client_number) = 0)
			PRINT @client_surname + ' ' + @client_name + ' ' + @client_lastname + ' :' + N' is not in list'
		ELSE
			PRINT @client_surname + ' ' + @client_name + ' ' + @client_lastname + ' :' + N' is one of first 50'
		FETCH NEXT FROM @checking_cursor INTO @client_surname, @client_name, @client_lastname, @client_number
	END

	CLOSE @checking_cursor
	DEALLOCATE @checking_cursor

GO

EXECUTE dbo.checking_procedure
GO


-- 4. Модифицировать хранимую процедуру п.2. таким
--    образом, чтобы выборка формировалась с помощью
--    табличной функции.

-- INLINE

IF OBJECT_ID(N'dbo.my_function_inline') IS NOT NULL
	DROP PROCEDURE dbo.my_function_inline;
GO

CREATE FUNCTION dbo.my_function_inline (@min_id int)
	RETURNS TABLE AS
	RETURN (
	SELECT Surname, Name, Lastname, Date_of_birth
	FROM Clients
	WHERE Date_of_birth BETWEEN '1980-01-01' AND '1995-01-01'
)
GO

-- INLINE END

-- LINE

IF OBJECT_ID(N'dbo.my_function_line') IS NOT NULL
	DROP PROCEDURE dbo.my_function_line;
GO

CREATE FUNCTION dbo.my_function_line (@min_id int)
	RETURNS @return_table TABLE
	(
	Surname nvarchar(50) NOT NULL,
	Name nvarchar(50) NOT NULL,
	Lastname nvarchar(50) NOT NULL,
	Date_of_birth date NOT NULL
	)
	AS
		BEGIN
			INSERT @return_table SELECT Surname, Name, Lastname, Date_of_birth
			FROM Clients
			WHERE Date_of_birth BETWEEN '1980-01-01' AND '1995-01-01'
			RETURN
		END
GO 

-- LINE END

ALTER PROCEDURE dbo.select_proc_with_new_column
	@cursor CURSOR VARYING OUTPUT
AS
	DECLARE @min_id int = 5
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT * FROM dbo.my_function_inline (@min_id)
	OPEN @cursor
GO


DECLARE @client_table_cursor CURSOR
EXECUTE dbo.select_proc_with_new_column @cursor = @client_table_cursor OUTPUT

FETCH NEXT FROM @client_table_cursor;
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @client_table_cursor
	END

CLOSE @client_table_cursor;
DEALLOCATE @client_table_cursor;
GO 
