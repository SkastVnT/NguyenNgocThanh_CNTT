raiserror('Creating Drink Store database....',0,1)
SET NOCOUNT ON
GO
USE [master]
GO
CREATE DATABASE [DrinkStore]
GO
USE [DrinkStore]
GO
CREATE TABLE [dbo].[Category](
	[id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[name] [varchar](50) NOT NULL
)
GO
CREATE TABLE [dbo].[Product](
	[id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[description] [varchar](50) NOT NULL,
	[price] [float] NOT NULL,
	[discount] [float] NOT NULL,
	[categoryId] [int] references Category(id) NOT NULL
)
GO
CREATE TABLE [dbo].[Account](
	[id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[name] [varchar](50) NOT NULL,
	[address] [varchar](50) NOT NULL,
	[phone] [varchar](12) NOT NULL,
	[email] [varchar](30) NOT NULL,
	--password: default = 1
	[password] [char](64) NOT NULL default('6B86B273FF34FCE19D6B804EFF5A3F5747ADA4EAA22F1D49C01E52DDB7875B4B'),
	[enabled] [bit] NOT NULL default(1),
	[role] [varchar](255) NOT NULL
)
GO
CREATE TABLE [dbo].[Customer](
	[id] [int] PRIMARY KEY references [Account](id) NOT NULL,
	[category] varchar(50) NOT NULL CHECK([category] IN ('Gold','Silver','Copper')),
	[shipToAddress] [varchar](50) NOT NULL
)
GO
CREATE TABLE [dbo].[Employee](
	[id] [int] PRIMARY KEY references [Account](id) NOT NULL,
	[salary] [money] NOT NULL,
	[departmentId] [int] NOT NULL
)
GO
CREATE TABLE [dbo].[OrderHeader](
	[id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[date] [datetime] NOT NULL,
	[status] [varchar](30) NULL,
	[customerId] [int] references Customer(id) NOT NULL,
	[employeeId] [int] references Employee(id) NOT NULL
)
GO
CREATE TABLE [dbo].[OrderDetail](
	[id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[orderHeaderId] [int] references OrderHeader(id) NOT NULL,
	[productId] [int] references Product(id) NOT NULL,
	[quantity] [int] NOT NULL,
	[price] [float] NOT NULL,
	[discount] [float] NOT NULL
)
GO
SET IDENTITY_INSERT [dbo].[Category] ON 

INSERT [dbo].[Category] ([id], [name]) VALUES (1, N'Drink')
SET IDENTITY_INSERT [dbo].[Category] OFF
SET IDENTITY_INSERT [dbo].[Product] ON 

INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (1, N'Coca cola nguyên bản', 7.53, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (2, N'Sting vàng', 8.72, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (3, N'Pepsi nguyên bản', 10.49, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (4, N'Pepsi light', 5.83, 0, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (5, N'Coca cola Zero', 4.97, 0.15, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (6, N'Fanta cam', 6.74, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (7, N'Monster', 9.26, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (8, N'C2 Chanh', 6.59, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (9, N'Redbull', 4.71, 0, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (10, N'Sting đỏ', 10.63, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (11, N'Bí đao', 7.91, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (12, N'Warrior dâu', 8.25, 0.15, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (13, N'Warrior nho', 6.44, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (14, N'Milo', 9.73, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (15, N'Revive thường', 6.88, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (16, N'Revive chanh muối', 8.46, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (17, N'Wakeup 247', 5.67, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (18, N'7Up', 4.96, 0, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (19, N'Fanta nho', 9.12, 0.15, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (20, N'Mirinda soda kem', 10.02, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (21, N'Mirinda cam', 8.19, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (22, N'Mirinda xá xị', 9.66, 0.05, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (23, N'Mirinda soda kem việt quất', 7.54, 0.1, 1)
INSERT [dbo].[Product] ([id], [description], [price], [discount], [categoryId]) VALUES (24, N'Sprite', 6.89, 0.05, 1)
SET IDENTITY_INSERT [dbo].[Product] OFF

SET IDENTITY_INSERT [dbo].[Account] ON 
INSERT [dbo].[Account] ([id], [name], [address], [phone], [email], [role]) VALUES
(1, N'Admin', N'9652 Los Angeles', N'0123456789', N'a@petstore.com', 'ROLE_ADMIN'),
(2, N'Employee1', N'5747 Shirley Drive', N'1234567890', N'e1@petstore.com', 'ROLE_EMPLOYEE'),
(3, N'Employee2', N'3841 Silver Oaks Place', N'2345678901', N'e2@petstore.com', 'ROLE_EMPLOYEE'),
(4, N'Customer1', N'1873 Lion Circle', N'5678901234', N'c1@gmail.com','ROLE_CUSTOMER'),
(5, N'Customer2', N'5747 Shirley Drive', N'6789872314', N'c2@gmail.com', 'ROLE_CUSTOMER')
SET IDENTITY_INSERT [dbo].[Account] OFF

INSERT [dbo].[Customer] ([id], [category], [shipToAddress]) VALUES (4, 'Copper', N'1873 Lion Circle')
INSERT [dbo].[Customer] ([id], [category], [shipToAddress]) VALUES (5, 'Copper', N'5747 Shirley Drive')

INSERT [dbo].[Employee] ([id], [salary], [departmentId]) VALUES 
(1, 1200, 1),(2, 1000, 2),(3, 800, 2)
GO
SET NOCOUNT OFF
raiserror('The Drink Store database in now ready for use.',0,1)
GO

