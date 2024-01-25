from sqlalchemy import create_engine, MetaData

engine = create_engine("sqlite:///C:/Users/admin/Desktop/Mobiles M8/api/db/Crud2.db")

meta = MetaData()

conn = engine.connect()