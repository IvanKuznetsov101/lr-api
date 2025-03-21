CREATE TABLE profile
(
    id_profile  bigserial PRIMARY KEY,
    full_name VARCHAR (50),
    username    VARCHAR (50),
    email VARCHAR (50),
    date_of_birth DATE,
    profile_coordinates GEOGRAPHY(POINT,4326),
    balance INTEGER,
    password VARCHAR (50)
);