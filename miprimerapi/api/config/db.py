from sqlalchemy import create_engine, MetaData
import os


# Obtiene la ruta absoluta del directorio del script
script_directory = os.path.abspath(os.path.dirname(__file__))

# Combina la ruta del script con la ruta relativa a la base de datos
database_path = os.path.join(script_directory,"..", "db", "Crud2.db")

# Crea el motor de la base de datos
engine = create_engine(f"sqlite:///{database_path}")
meta = MetaData()

conn = engine.connect()