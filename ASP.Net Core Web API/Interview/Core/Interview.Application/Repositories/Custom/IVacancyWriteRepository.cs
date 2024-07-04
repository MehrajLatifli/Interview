﻿using Interview.Application.Repositories.Abstract;
using Interview.Domain.Entities.Models;

namespace Interview.Application.Repositories.Custom
{
    public interface IVacancyWriteRepository : IWriteRepository<Vacancy>
    {
    }
}
