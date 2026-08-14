from generators.dag_generator import DagGenerator

class BronzeGenerator(DagGenerator):
    def __init__(self, path):
        super().__init__(path)
    def get_bash_command(self, task):
        return task["bash_command"]