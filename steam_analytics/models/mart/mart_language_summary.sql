select
    language,
    count(*) as total_reviews,
    sum(case when voted_up then 1 else 0 end) as positive_reviews,
    round(avg(cast(voted_up as integer)) * 100, 2) as positive_rate
from {{ ref('fact_reviews')}}
group by language
order by total_reviews desc