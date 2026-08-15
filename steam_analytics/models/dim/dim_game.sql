select distinct
    appid,
    game
from {{ ref('stg_steam') }}