from generators.dag_generator import DagGenerator

class GoldGenerator(DagGenerator):
    def __init__(self, path):
        super().__init__(path)
    def get_bash_command(self, task):
        return f"{task['bash_command']} --profiles-dir /opt/airflow"