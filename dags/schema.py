from airflow import DAG
from airflow.operators.bash import BashOperator
from datetime import datetime, timedelta
default_args = {
    "owner": "steam",
    "retries": 5,
    "retry_delay": timedelta(minutes=5),
}
with DAG (
    dag_id= "Schema",
    default_args=default_args,
    start_date=datetime(2025, 5, 5),
    catchup=False,
    schedule=None,
    max_active_runs=1
) as dag:
    BashOperator(
        task_id="Schema",
        bash_command=(
            'java '
            '--add-opens=java.base/java.nio=ALL-UNNAMED '
            '--add-opens=java.base/sun.nio.ch=ALL-UNNAMED '
            '-cp /opt/airflow/app/spark-app.jar com.steam.trino.Schema'
        ),
        cwd='/opt/airflow'
    )
