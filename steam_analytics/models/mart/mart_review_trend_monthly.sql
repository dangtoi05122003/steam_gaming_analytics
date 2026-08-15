select
    d.year,
    d.month,
    count(*) as total_reviews,
    sum(case when f.voted_up then 1 else 0 end) as positive_reviews,
    round(avg(cast(f.voted_up as integer)) * 100, 2) as positive_rate
from {{ ref('fact_reviews') }} f
join {{ ref('dim_date')}} d on f.date = d.date
group by d.year, d.month
order by d.year, d.month