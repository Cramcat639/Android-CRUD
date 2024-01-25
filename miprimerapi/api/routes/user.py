from fastapi import APIRouter, HTTPException, status, Body
from config.db import conn
from models.user import user as user_model
from schemas.user import User
from typing import List
from passlib.context import CryptContext



router = APIRouter()


# Crear un objeto CryptContext para encriptar contraseñas
password_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


@router.get("/user", tags=["user"], response_model=List[User], description="lista todos los usuarios")
def get_users():
    return conn.execute(user_model.select()).fetchall()


@router.get("/user/{id}", tags=["user"], response_model=User, description="muestra un usuario por id")
def get_user_by_id(id: str):
    return conn.execute(user_model.select().where(user_model.c.id == id)).first()


@router.post("/user/login", tags=["user"], description="Obtener el ID del usuario por correo electrónico y contraseña")
def get_user_id_by_credentials(user_data: dict = Body(...)):
    try:
        mail = user_data.get("mail")
        password = user_data.get("password")
        if not (mail and password):
            raise HTTPException(status_code=400, detail="Se requiere 'mail' y 'password' en el cuerpo JSON.")
        
        user_db_data = conn.execute(user_model.select().where(user_model.c.mail == mail)).first()

        if user_db_data and password_context.verify(password, user_db_data[3]):  # 3 es el índice de la columna 'password'
            return {"id": user_db_data[0]}  # 0 es el índice de la columna 'id'
        else:
            raise HTTPException(status_code=401, detail="Credenciales inválidas")
    except HTTPException:
        raise  # Propaga las excepciones de HTTPException directamente
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.post("/user/new", tags=["user"], response_model=User, description="crear un usuario")
def create_user(user: User):
    try:
        new_user = {"username": user.username, "mail": user.mail}
        new_user["password"] = password_context.hash(user.password.encode("utf-8"))
        result = conn.execute(user_model.insert().values(new_user))
        # Confirmar la transacción para guardar los cambios permanentemente
        conn.commit()
        return conn.execute(user_model.select().where(user_model.c.id == result.lastrowid)).first()
    except Exception as e:
        # Deshacer la transacción en caso de error
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))


@router.put("/{user_id}", tags=["user"], response_model=User, description="actualizar un usuario por ID")
def update_user(user_id: int, updated_user: User):
    try:
        existing_user = conn.execute(user_model.select().where(user_model.c.id == user_id)).first()
        if not existing_user:
            raise HTTPException(status_code=404, detail="Usuario no encontrado")
        updated_fields = {k: v for k, v in updated_user.model_dump().items() if v is not None}
        if "password" in updated_fields:
            updated_fields["password"] = password_context.hash(updated_fields["password"])
        conn.execute(user_model.update().where(user_model.c.id == user_id).values(updated_fields))
        # Confirmar la transacción para guardar los cambios permanentemente
        conn.commit()
        return conn.execute(user_model.select().where(user_model.c.id == user_id)).first()
    except Exception as e:
        # Deshacer la transacción en caso de error
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    

@router.delete("/delete/{user_id}", tags=["user"], response_model=User, description="eliminar un usuario por ID")
def delete_user(user_id: int):
    try:
        existing_user = conn.execute(user_model.select().where(user_model.c.id == user_id)).first()
        if not existing_user:
            raise HTTPException(status_code=404, detail="Usuario no encontrado")
        conn.execute(user_model.delete().where(user_model.c.id == user_id))
        # Confirmar la transacción para guardar los cambios permanentemente
        conn.commit()
        return existing_user
    except Exception as e:
        # Deshacer la transacción en caso de error
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    