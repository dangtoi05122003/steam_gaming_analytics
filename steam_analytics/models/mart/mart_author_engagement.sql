select
    author_steamid,
    count(*) as total_reviews_written,
    sum(case when voted_up then 1 else 0 end) as positive_reviews_written,
    avg(author_playtime_at_review) as avg_playtime_at_review_minutes,
    max(author_num_games_owned) as latest_num_games_owned,
    max(author_num_reviews) as latest_num_reviews
from {{ref('fact_reviews')}}
group by author_steamid