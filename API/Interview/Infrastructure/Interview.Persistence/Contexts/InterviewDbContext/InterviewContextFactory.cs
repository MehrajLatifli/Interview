using Interview.Persistence.ServiceExtensions;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;

namespace Interview.Persistence.Contexts.InterviewDbContext;

public class InterviewContextFactory : IDesignTimeDbContextFactory<InterviewContext>
{
    public InterviewContext CreateDbContext(string[] args)
    {
        var optionsBuilder = new DbContextOptionsBuilder<InterviewContext>();
        optionsBuilder.UseSqlServer(ServiceExtension.CustomDbConnectionString);

        return new InterviewContext(optionsBuilder.Options);
    }
}
