namespace Interview.Application.Mapper.DTO.SessionDTO
{
    public class SessionDTOforGetandGetAll
    {
        public int Id { get; set; }

        public decimal EndValue { get; set; }

        public DateTime? StartDate { get; set; }

        public DateTime? EndDate { get; set; }

        public int VacancyId { get; set; }

        public int CandidateId { get; set; }

        public int UserAccountId { get; set; }

    }
}
