from airflow import DAG
from airflow.operators.bash import BashOperator
from datetime import datetime, timedelta
from models.base import BaseDagConfig
from generators.base import BaseGenerator
from abc import abstractmethod, ABC

class DagGenerator(BaseGenerator, ABC):
    def __init__(self, path):
        super().__init__(path)
    def load_dags(self, global_session):
        for path in self.load_config_path():
            data = self.load_yaml(path)
            default = data.get("default_args")
            for config in data.get("dag_configs"):
                tasks = config.get("tasks")
                conf = BaseDagConfig(**{**default, **{k:v for k, v in config.items() if k != "tasks"}})
                global_session[conf.dag_id] = self.create_dag(conf, tasks)
    def create_dag(self, conf, tasks):
        dag = DAG(
            dag_id = conf.dag_id,
            default_args= {
                'owner': conf.owner,
                'retries': conf.retries,
                'retry_delay': timedelta(minutes=conf.retry_delay),
            },
            start_date=datetime.strptime(conf.start_date, '%Y-%m-%d'),
            schedule = conf.schedule,
            max_active_runs = conf.max_active_runs,
            catchup = conf.catchup,
            is_paused_upon_creation=conf.is_paused_upon_creation
        )
        with dag:
            operators = [
                BashOperator(
                    task_id=task['task_id'],
                    bash_command=self.get_bash_command(task),
                    cwd=task.get("cwd")
                )
                for task in tasks
            ]
            for current_task, next_task in zip(operators, operators[1:]):
                current_task >> next_task
        return dag
    @abstractmethod
    def get_bash_command(self, task):
        pass
