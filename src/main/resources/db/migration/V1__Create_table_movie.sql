CREATE TABLE IF NOT EXISTS movie (
            id int primary key GENERATED by default AS IDENTITY,
            title varchar (255) not null,
            status varchar (255) not null
);