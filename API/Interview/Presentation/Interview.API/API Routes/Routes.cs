using Interview.Domain.Entities.Models;

namespace Interview.API.API_Routes
{
    public struct Routes
    {
        public  const string CandidateDocument = "candidatedocument";

        public  const string  CandidateDocumentById = CandidateDocument + "/{id}";


        public const string Candidate = "candidate";

        public const string  CandidateById = Candidate + "/{id}";


        public const string Level = "level";

        public const string  LevelById = Level + "/{id}";


        public const string Category = "category";

        public const string  CategoryById = Category + "/{id}";


        public const string StructureType = "structureType";

        public const string  StructureTypeById = StructureType + "/{id}";


        public const string Structure = "structure";

        public const string  StructureById = Structure + "/{id}";


        public const string Position = "position";

        public const string  PositionById = Position + "/{id}";


        public const string Vacancy = "vacancy";

        public const string  VacancyById = Vacancy + "/{id}";


        public const string Session = "session";
        public const string OwnSession = "ownSession";

        public const string  SessionById = Session + "/{id}";


        public const string Question = "question";

        public const string  QuestionById = Question + "/{id}";


        public const string SessionQuestion = "sessionQuestion";

        public const string SessionQuestionById = "Id" + "/{id:int}";


        public const string GetSessionQuestionBySessionId = "sessionId" + "/{sessionId:int}";

        public const string GetAllQuestionByPage = "bypage";


        public const string RandomQuestion = "randomQuestion";

        public const string RandomQuestionById = RandomQuestion;

        public const string RandomQuestion2 = "randomQuestion2";

        public const string Profile = "profile";

        public const string addUser = "addUser";
        public const string addRole = "addRole";
        public const string addRoleClaim = "addRoleClaim";
        public const string addUserClaim = "addUserClaim";
        public const string addUserRole = "addUserRole";
        public const string RegisterAdmin = "registerAdmin";
        public const string RegisterHR = "registerHR";
        public const string RegisterUser = "RegisterUser";
        public const string Login = "login";
        public const string Logout = "logout";
        public const string RefreshToken = "refreshtoken";
        public const string RevokeUsername = "revoke/{username}";
        public const string RevokeAll = "revoke-all";
        public const string UpdateProfile = "updateProfile";
        public const string UpdatePassword = "updatePassword";
        public const string GetMehtods = "getMehtods";
        public const string GetUserAccess = "getUserAccess";
        public const string GetAdmins = "getAdmins";
        public const string GetHR = "getHR";
        public const string GetRoles = "GetRoles";
        public const string DeleteRole = "DeleteRole";
        public const string GetRoleAccessType = "getRoleAccessType";
        public const string AddUserRole = "AddUserRole";
  
    }
}
