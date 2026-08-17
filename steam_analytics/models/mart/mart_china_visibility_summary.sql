select
    coalesce(hidden_in_steam_china, false) as hidden_in_steam_china,
    count(*) as total_reviews,
    sum(case when voted_up then 1 else 0 end) as positive_reviews
from {{ref('fact_reviews')}}
group by hidden_in_steam_china