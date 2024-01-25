from sqlalchemy import Column, Table
from sqlalchemy.sql.sqltypes import Integer, String
from config.db import meta, engine

user = Table(
    "user", 
    meta,
    Column("id", Integer, primary_key=True),
    Column("username", String,unique=True, nullable=False),
    Column("mail", String, unique=True, nullable=False),
    Column("password", String, nullable=False),
    Column("isadmin", Integer, default=0),
    Column("isblock", Integer, default=0),
)

meta.create_all(engine)