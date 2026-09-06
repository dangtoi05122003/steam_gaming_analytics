select
    recommendationid,
    appid,
    max(review) as review
from {{ ref('stg_steam') }}
group by recommendationid, appid