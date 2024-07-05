using AutoMapper;
using Interview.Application.Mapper.DTO.AuthDTO;
using Interview.Application.Mapper.DTO.CandidateDocumentDTO;
using Interview.Application.Mapper.DTO.CandidateDTO;
using Interview.Application.Mapper.DTO.CategoryDTO;
using Interview.Application.Mapper.DTO.LevelDTO;
using Interview.Application.Mapper.DTO.PositionDTO;
using Interview.Application.Mapper.DTO.QuestionDTO;
using Interview.Application.Mapper.DTO.RoleClaimDTO;
using Interview.Application.Mapper.DTO.RoleDTO;
using Interview.Application.Mapper.DTO.SessionDTO;
using Interview.Application.Mapper.DTO.SessionQuestionDTO;
using Interview.Application.Mapper.DTO.StructureDTO;
using Interview.Application.Mapper.DTO.StructureTypeDTO;
using Interview.Application.Mapper.DTO.UserClaimDTO;
using Interview.Application.Mapper.DTO.UserDTO;
using Interview.Application.Mapper.DTO.UserRoleDTO;
using Interview.Application.Mapper.DTO.VacancyDTO;
using Interview.Domain.Entities.AuthModels;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Domain.Entities.Models;
using NuGet.ContentModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Application.Mapper
{
    public class MapperProfile : Profile
    {
        public MapperProfile()
        {

            CreateMap<CandidateDocument, CandidateDocumentDTOforCreate>();
            CreateMap<CandidateDocumentDTOforCreate, CandidateDocument>();
            CreateMap<CandidateDocument, CandidateDocumentDTOforUpdate>();
            CreateMap<CandidateDocumentDTOforUpdate, CandidateDocument>();
            CreateMap<CandidateDocument, CandidateDocumentDTOforGetandGetAll>();
            CreateMap<CandidateDocumentDTOforGetandGetAll, CandidateDocument>();

            CreateMap<Candidate, CandidateDTOforCreate>();
            CreateMap<CandidateDTOforCreate, Candidate>();
            CreateMap<Candidate, CandidateDTOforUpdate>();
            CreateMap<CandidateDTOforUpdate, Candidate>();
            CreateMap<Candidate, CandidateDTOforGetandGetAll>();
            CreateMap<CandidateDTOforGetandGetAll, Candidate>();

            CreateMap<Level, LevelDTOforCreate>();
            CreateMap<LevelDTOforCreate, Level>();
            CreateMap<Level, LevelDTOforUpdate>();
            CreateMap<LevelDTOforUpdate, Level>();
            CreateMap<Level, LevelDTOforGetandGetAll>();
            CreateMap<LevelDTOforGetandGetAll, Level>();

            CreateMap<Category, CategoryDTOforCreate>();
            CreateMap<CategoryDTOforCreate, Category>();
            CreateMap<Category, CategoryDTOforUpdate>();
            CreateMap<CategoryDTOforUpdate, Category>();
            CreateMap<Category, CategoryDTOforGetandGetAll>();
            CreateMap<CategoryDTOforGetandGetAll, Category>();

            CreateMap<Question, QuestionDTOforCreate>();
            CreateMap<QuestionDTOforCreate, Question>();
            CreateMap<Question, QuestionDTOforUpdate>();
            CreateMap<QuestionDTOforUpdate, Question>();
            CreateMap<Question, QuestionDTOforGetandGetAll>();
            CreateMap<QuestionDTOforGetandGetAll, Question>();

            CreateMap<SessionQuestion, SessionQuestionDTOforCreate>();
            CreateMap<SessionQuestionDTOforCreate, SessionQuestion>();
            CreateMap<SessionQuestion, SessionQuestionDTOforUpdate>();
            CreateMap<SessionQuestionDTOforUpdate, SessionQuestion>();
            CreateMap<SessionQuestion, SessionQuestionDTOforGetandGetAll>();
            CreateMap<SessionQuestionDTOforGetandGetAll, SessionQuestion>();

            CreateMap<Session, SessionDTOforCreate>();
            CreateMap<SessionDTOforCreate, Session>();
            CreateMap<Session, SessionDTOforUpdate>();
            CreateMap<SessionDTOforUpdate, Session>();
            CreateMap<Session, SessionDTOforGetandGetAll>();
            CreateMap<SessionDTOforGetandGetAll, Session>();

            CreateMap<Structure, StructureDTOforCreate>();
            CreateMap<StructureDTOforCreate, Structure>();
            CreateMap<Structure, StructureDTOforUpdate>();
            CreateMap<StructureDTOforUpdate, Structure>();
            CreateMap<Structure, StructureDTOforGetandGetAll>();
            CreateMap<StructureDTOforGetandGetAll, Structure>();

            CreateMap<StructureType, StructureTypeDTOforCreate>();
            CreateMap<StructureTypeDTOforCreate, StructureType>();
            CreateMap<StructureType, StructureTypeDTOforUpdate>();
            CreateMap<StructureTypeDTOforUpdate, StructureType>();
            CreateMap<StructureType, StructureTypeDTOforGetandGetAll>();
            CreateMap<StructureTypeDTOforGetandGetAll, StructureType>();

            CreateMap<Vacancy, VacancyDTOforCreate>();
            CreateMap<VacancyDTOforCreate, Vacancy>();
            CreateMap<Vacancy, VacancyDTOforUpdate>();
            CreateMap<VacancyDTOforUpdate, Vacancy>();
            CreateMap<Vacancy, VacancyDTOforGetandGetAll>();
            CreateMap<VacancyDTOforGetandGetAll, Vacancy>();

            CreateMap<Position, PositionDTOforCreate>();
            CreateMap<PositionDTOforCreate, Position>();
            CreateMap<Position, PositionDTOforUpdate>();
            CreateMap<PositionDTOforUpdate, Position>();
            CreateMap<Position, PositionDTOforGetandGetAll>();
            CreateMap<PositionDTOforGetandGetAll, Position>();

            CreateMap<User, UserDTOforCreate>();
            CreateMap<UserDTOforCreate, User>();
            CreateMap<User, UserDTOforUpdate>();
            CreateMap<UserDTOforUpdate, User>();
            CreateMap<User, UserDTOforGetandGetAll>();
            CreateMap<UserDTOforGetandGetAll, User>();

            CreateMap<Role, RoleDTOforCreate>();
            CreateMap<RoleDTOforCreate, Role>();
            CreateMap<Role, RoleDTOforUpdate>();
            CreateMap<RoleDTOforUpdate, Role>();
            CreateMap<Role, RoleDTOforGetandGetAll>();
            CreateMap<RoleDTOforGetandGetAll, Role>();

            CreateMap<UserRole, UserRoleDTOforCreate>();
            CreateMap<UserRoleDTOforCreate, UserRole>();
            CreateMap<UserRole, UserRoleDTOforUpdate>();
            CreateMap<UserRoleDTOforUpdate, UserRole>();
            CreateMap<UserRole, UserRoleDTOforGetandGetAll>();
            CreateMap<UserRoleDTOforGetandGetAll, UserRole>();

            CreateMap<UserClaim, UserClaimDTOforCreate>();
            CreateMap<UserClaimDTOforCreate, UserClaim>();
            CreateMap<UserClaim, UserClaimDTOforUpdate>();
            CreateMap<UserClaimDTOforUpdate, UserClaim>();
            CreateMap<UserClaim, UserClaimDTOforGetandGetAll>();
            CreateMap<UserClaimDTOforGetandGetAll, UserClaim>();

            CreateMap<RoleClaim, RoleClaimDTOforCreate>();
            CreateMap<RoleClaimDTOforCreate, RoleClaim>();
            CreateMap<RoleClaim, RoleClaimDTOforUpdate>();
            CreateMap<RoleClaimDTOforUpdate, RoleClaim>();
            CreateMap<RoleClaim, RoleClaimDTOforGetandGetAll>();
            CreateMap<RoleClaimDTOforGetandGetAll, RoleClaim>();


            CreateMap<Login, LoginDTO>();
            CreateMap<LoginDTO, Login>();

            CreateMap<LoginResponse, LoginResponseDTO>();
            CreateMap<LoginResponseDTO, LoginResponse>();

            CreateMap<Register, RegisterDTO>();
            CreateMap<RegisterDTO, Register>();

            CreateMap<Register, RegisterAdminDTO>();
            CreateMap<RegisterAdminDTO, Register>();

            CreateMap<UpdatePassword, UpdatePasswordDTO>();
            CreateMap<UpdatePasswordDTO, UpdatePassword>();

            CreateMap<UpdateProfile, UpdateProfileDTO>();
            CreateMap<UpdateProfileDTO, UpdateProfile>();

       
        }
    }
}
