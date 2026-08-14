import os
from generators.silver import SilverGenerator

CONFIG_PATH = os.getenv("AIRFLOW_SILVER_PATH")

generator = SilverGenerator(path=CONFIG_PATH)
generator.load_dags(globals())