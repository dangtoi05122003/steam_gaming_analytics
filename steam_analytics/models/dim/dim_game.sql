with base as (
    select
        appid,
        game,
        row_number() over (partition by appid order by created_at desc) as rn
    from {{ ref('stg_steam') }}
)
select
    appid,
    game
from base
where rn = 1