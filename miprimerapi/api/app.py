from fastapi import FastAPI
from routes.user import router
from config.openapi import tags_metadata

app = FastAPI(
    title="Users API CRUD",
    description="API para el manejo de usuarios CRUD",
    version="0.0.1",
    openapi_tags=tags_metadata,
)

app.include_router(router)
