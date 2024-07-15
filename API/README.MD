<img src="https://github.com/MehrajLatifli/Interview-with-Onion-Architecture-/blob/master/v2/Assets/image.png" alt="Your Image Description" width="500">


<h1> SQL </h1>
<a href="https://github.com/MehrajLatifli/Interview-with-Onion-Architecture-/blob/master/v2/Assets/Interview.sql">Interview SQL file</a>
</br>
<p>If Id's jumping up 1000 ->  ALTER DATABASE SCOPED CONFIGURATION SET IDENTITY_CACHE = OFF   GO</p>
</br>
<img src="https://github.com/MehrajLatifli/Interview-with-Onion-Architecture-/blob/master/v2/Assets/İnterview%20diagram.png" alt="Diagram" width="500">

</br>

<h1> MSSQL Server </h1>
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=admin1234@" -p 1430:1433 --name sql2 --hostname sql2 -d ` mcr.microsoft.com/mssql/server:2022-latest 

</br>

<h1> PostgreSQL </h1>
docker run --name some-Logpostgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=Interview_LogDB -d postgres:15.4
</br>
</br>
<p> After Create new database -> Interview_LogDB </p>
</br>

<h1> Seq log server </h1>
docker run --name some-seq -d --restart unless-stopped -e ACCEPT_EULA=Y -p 5341:80 datalust/seq:latest

</br>
</br>


<p> SELECT
    @Id AS Id,
    @MessageTemplate AS Message,
    @Timestamp AS Timestamp,
    user_name AS UserName
FROM
    stream
WHERE
    user_name IS NOT NULL
ORDER BY Timestamp DESC </p>

</br>

<h1> Dockerize -> Use Powershell or Developer Powershell in Visual studio</h1>
<br/>
docker build    -f "Your_RootPath\Interview\API\Interview\Presentation\Interview.API\Dockerfile"    -t interview:dev    "Your_RootPath\Interview\API\Interview" 
<br/>
<br/>
1) dotnet dev-certs https --clean
<br/>
2) dotnet dev-certs https --trust
<br/>
3) if do not work anything -> Restart VS
<br/>
4) cd Your_RootPath\Interview\API\Interview
<br/>
5) docker-compose down
<br/>    
6) docker-compose up --build

</br>
<br/>

<h1> Migration </h1>
add-migration InterviewContextMigration -Context Interview.Persistence.Contexts.InterviewDbContext.InterviewContext
</br>
</br>
update-database -Context Interview.Persistence.Contexts.InterviewDbContext.InterviewContext
</br>
<h1> Azure Blob Storage </h1>

docker run -p 10000:10000 -p 10001:10001 -p 10002:10002 --name some-azurite mcr.microsoft.com/azure-storage/azurite

Microsoft Azure Storage Explorer -> Local Storage Emulator (Display name: Interview)

Connection string:   AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;DefaultEndpointsProtocol=http;BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;QueueEndpoint=http://127.0.0.1:10001/devstoreaccount1;TableEndpoint=http://127.0.0.1:10002/devstoreaccount1; 