CREATE TABLE Account
(
id INTEGER PRIMARY KEY,
_id TEXT,
jwt TEXT,
email TEXT,
createdAt TEXT,
apikey TEXT,
iat TEXT,
password TEXT
);

CREATE TABLE Device
(
id INTEGER PRIMARY KEY,
name TEXT,
type NUMERIC,
deviceid TEXT,
apikey TEXT,
online TEXT,
devicegroup TEXT
);

CREATE TABLE Application
(
autoLoginRemote NUMERIC,
showType            NUMERIC
);


