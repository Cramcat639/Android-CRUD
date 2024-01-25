from typing import Optional
from pydantic import BaseModel

class User(BaseModel):
    id: Optional[int]
    username: str
    mail: str
    password: str
    isadmin: Optional[int]
    isblock: Optional[int]

class UserCount(BaseModel):
    total: int