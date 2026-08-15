select
    hidden_in_steam_china,
    count(*) as total_reviews
from {{ref('fact_reviews')}}
group by hidden_in_steam_china