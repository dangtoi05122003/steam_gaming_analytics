import attr

@attr.s(kw_only=True, auto_attribs=True)
class BaseDagConfig:
    owner: str
    retries: int
    retry_delay: int
    dag_id: str
    start_date: str
    catchup: bool
    schedule: str
    max_active_runs: int
    is_paused_upon_creation: bool