CREATE DATABASE Interview;

USE Interview;


-- İstifadəçi
CREATE TABLE [User]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [UserName] NVARCHAR(max) DEFAULT NULL,
   [Email] NVARCHAR(max) DEFAULT NULL ,
   [Password] NVARCHAR(max) DEFAULT NULL ,
   [Phonenumber] NVARCHAR(max)  DEFAULT NULL,
   [ConcurrencyStamp] NVARCHAR(max) DEFAULT NULL,
   [ImagePath] NVARCHAR(max) DEFAULT NULL,
   [RefreshToken] NVARCHAR(max) DEFAULT NULL,
   [RefreshTokenExpiryTime] datetime2 DEFAULT NULL,
);


-- Rol
CREATE TABLE [Role]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Name] NVARCHAR(max) DEFAULT NULL,
   [ConcurrencyStamp] NVARCHAR(max) DEFAULT NULL,
);


-- İstifadçi və Rol
CREATE TABLE [UserRole]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   
   [UserId] INT NOT NULL,
   [RoleId] INT NOT NULL,

   CONSTRAINT [FK_UserId_forUserRole] FOREIGN KEY ([UserId]) REFERENCES [User] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_RoleId_forUserRole] FOREIGN KEY ([RoleId]) REFERENCES [Role] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,

);


-- İstifadçi səlahiyyətləri
CREATE TABLE [UserClaim]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [ClaimType] NVARCHAR(max) DEFAULT NULL,
   [ClaimValue] NVARCHAR(max) DEFAULT NULL,
   
   [UserId] INT NOT NULL,

   
   CONSTRAINT [FK_UserId_forUserClaim] FOREIGN KEY ([UserId]) REFERENCES [User] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
);


-- Rol səlahiyyətləri
CREATE TABLE [RoleClaim]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [ClaimType] NVARCHAR(max) DEFAULT NULL,
   [ClaimValue] NVARCHAR(max) DEFAULT NULL,
   
   [RoleId] INT NOT NULL,

   
   CONSTRAINT [FK_RoleId_forRoleClaim] FOREIGN KEY ([RoleId]) REFERENCES [Role] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
);


-- Namizədlə bağlı fayl
CREATE TABLE [CandidateDocument]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Surname] NVARCHAR(max) DEFAULT NULL,
   [Name] NVARCHAR(max) NOT NULL ,
   [Patronymic] NVARCHAR(max) DEFAULT NULL ,
   [Phonenumber] NVARCHAR(max)  DEFAULT NULL,
   [Email] NVARCHAR(max) NOT NULL,
   [CV] NVARCHAR(max) NOT NULL,
   [Address] NVARCHAR(max) DEFAULT NULL,
);


-- Namizəd
CREATE TABLE [Candidate]
(
  [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,

  [CandidateDocumentId] INT NOT NULL,
      
  CONSTRAINT [FK_CandidateDocument_forCandidates] FOREIGN KEY ([CandidateDocumentId]) REFERENCES [CandidateDocument] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
);


-- StructureType
CREATE TABLE [StructureType]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Name] NVARCHAR(max) NOT NULL,
   
 );
   
   
   -- Structure
CREATE TABLE [Structure]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Name] NVARCHAR(max) NOT NULL,
   
   [ParentId] NVARCHAR(max) NOT NULL,
   [StructureTypeId] INT NOT NULL,
   
   CONSTRAINT [FK_StructureType_forStructure] FOREIGN KEY ([StructureTypeId]) REFERENCES [StructureType] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
 );
  
 


-- İş səviyyəsi
CREATE TABLE [Position]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Name] NVARCHAR(max) NOT NULL
);


-- Vakansiya
CREATE TABLE [Vacancy]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Title] NVARCHAR(max) NOT NULL,
   [Description] NVARCHAR(max) NOT NULL,
   [StartDate] datetime2 NOT NULL,
   [EndDate] datetime2 NOT NULL,
   
   [PositionId] INT NOT NULL,
   [StructureId] INT NOT NULL,

   CONSTRAINT [FK_PositionId_forVacancy] FOREIGN KEY ([PositionId]) REFERENCES [Position] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_StructureId_forVacancy] FOREIGN KEY ([StructureId]) REFERENCES [Structure] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
);


-- Sesiya
CREATE TABLE [Session]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [EndValue] decimal DEFAULT 0.0,
   [StartDate] datetime2 DEFAULT NULL,
   [EndDate] datetime2 DEFAULT NULL,
   
   [UserId] INT NOT NULL,
   [VacancyId] INT NOT NULL,
   [CandidateId] INT NOT NULL,

   CONSTRAINT [FK_UserId_forSession] FOREIGN KEY ([UserId]) REFERENCES [User] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_VacancyId_forSession] FOREIGN KEY ([VacancyId]) REFERENCES [Vacancy] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_CandidateId_forSession] FOREIGN KEY ([CandidateId]) REFERENCES [Candidate] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   
);


-- Kateqoriya
CREATE TABLE [Category]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Name]  NVARCHAR(max) NOT NULL,
  
);


-- Sual səviyyəsi
CREATE TABLE [Level]
(

   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Name] NVARCHAR(max) NOT NULL,
   [Coefficient] decimal NOT NULL  DEFAULT 0.0,
  
);


-- Sual
CREATE TABLE [Question]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Text] NVARCHAR(max) NOT NULL,

   [LevelId] INT NOT NULL,
   [CategoryId] INT NOT NULL,
   [StructureId] INT NOT NULL,

   CONSTRAINT [FK_LevelId_forQuestion] FOREIGN KEY ([LevelId]) REFERENCES [Level] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_CategoryId_forQuestion] FOREIGN KEY ([CategoryId]) REFERENCES [Category] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_StructureId_forQuestion] FOREIGN KEY ([StructureId]) REFERENCES [Structure] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
);


-- Session Sual (Cavablandırılan suallar)
CREATE TABLE [SessionQuestion]
(
   [Id] INT PRIMARY KEY IDENTITY (1,1) NOT NULL,
   [Value] INT DEFAULT 0,

   [SessionId] INT NOT NULL,
   [QuestionId] INT NOT NULL,

   CONSTRAINT [FK_SessionId_forSessionQuestion] FOREIGN KEY ([SessionId]) REFERENCES [Session] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_QuestionId_forSessionQuestion] FOREIGN KEY ([QuestionId]) REFERENCES [Question] ([Id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
);











