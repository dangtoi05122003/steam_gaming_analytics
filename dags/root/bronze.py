import os
from generators.bronze import BronzeGenerator

CONFIG_PATH = os.getenv("AIRFLOW_BRONZE_PATH")

generator = BronzeGenerator(path=CONFIG_PATH)
generator.load_dags(globals())