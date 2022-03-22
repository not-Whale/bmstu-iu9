USE lab0;
GO

-- 1. Создать представление на основе одной из таблиц задания 6

if OBJECT_ID(N'ClientsView') IS NOT NULL
	DROP VIEW ClientsView
GO

CREATE VIEW ClientsView AS
	SELECT * FROM Clients
	WHERE Date_of_birth BETWEEN '1980-01-01' AND '1995-01-01'
GO

SELECT * FROM ClientsView
GO

-- 2. Создать представление на основе полей обеих связанных таблиц задания 6

IF OBJECT_ID(N'WorkersView') IS NOT NULL
	DROP VIEW WorkersView
GO

CREATE VIEW WorkersView AS
	SELECT w.Worker_ID, l.Client_ID, l.Procedure_ID
	FROM Workers as w INNER JOIN Logbook as l 
	ON w.Worker_ID = l.Worker_ID
GO

SELECT * FROM WorkersView
GO


-- 3. Создать индекс для одной из таблиц задания 6, включив в него дополнительные неключевые поля

IF EXISTS (SELECT * FROM sys.indexes  WHERE name = N'IX_ServiceType_Description')  
    DROP INDEX IX_ServiceType_Description ON ServiceType
GO

CREATE INDEX IX_ServiceType_Description
	ON ServiceType(Service_Description)
	INCLUDE (price)
GO


-- 4. Создать индексированное представление

IF OBJECT_ID(N'LogbookView') IS NOT NULL
	DROP VIEW LogbookView
GO

CREATE VIEW LogbookView WITH SCHEMABINDING AS
	SELECT Procedure_ID, Total_price, Reserve_date
	FROM dbo.Logbook
	WHERE Total_price > $25
GO

IF EXISTS (SELECT * FROM sys.indexes  WHERE name = N'IX_Logbook_Reserve_date')  
    DROP INDEX IX_Logbook_Reserve_date ON Logbook  
GO

CREATE UNIQUE CLUSTERED INDEX IX_Logbook_Reserve_date
    ON LogbookView (Procedure_ID, Reserve_date);
go

SELECT * FROM LogbookView
go