using Interview.Domain.Entities.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Application.Repositories.Abstract
{

    public interface IWriteRepository<T> : IRepository<T> where T : BaseEntity
    {
        Task<bool> AddAsync(T entity);

        Task<bool> AddRangeAsync(List<T> entities);

        bool Remove(T entity);

        bool RemoveRange(List<T> entities);

        Task<bool> RemoveByIdAsync(string id);

        bool Update(T entity);

        Task<int> SaveAsync();
    }
}
