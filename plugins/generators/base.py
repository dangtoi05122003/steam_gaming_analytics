import os
from glob import glob
from pathlib import Path
import yaml

class BaseGenerator:
    def __init__(self, path: Path):
        self.path = path
    def load_config_path(self):
        return glob(os.path.join(self.path, "*.yml"))
    def load_yaml(self, path: Path):
        with open(path, "r") as f:
            return yaml.safe_load(f)