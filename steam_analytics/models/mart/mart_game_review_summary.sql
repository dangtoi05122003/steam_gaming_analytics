select
    g.appid,
    g.game,
    count(*) as total_reviews,
    sum(case when f.voted_up then 1 else 0 end) as positive_reviews,
    round(avg(cast(f.voted_up as integer)) * 100, 2) as positive_rate,
    sum(f.votes_up) as total_votes_up,
    sum(f.votes_funny) as total_votes_funny,
    avg(f.author_playtime_at_review) as avg_playtime_at_reviews_minutes,
    sum(case when f.steam_purchase then 1 else 0 end) as purchased_count,
    sum(case when f.received_for_free then 1 else 0 end) as free_count,
    sum(case when f.written_during_early_access then 1 else 0 end) as early_access_review_count
from {{ ref('fact_reviews') }} f
join {{ ref('dim_game') }} g on f.appid = g.appid
group by g.appid, g.game
order by total_reviews desc