select
    r.recommendationid,
    r.appid,
    r.author_steamid,
    l.language_id,
    max(cast(r.voted_up as integer)) as voted_up,
    max(cast(r.votes_up as integer)) as votes_up,
    max(cast(r.votes_funny as integer)) as votes_funny,
    max(cast(r.weighted_vote_score as double)) as weighted_vote_score,
    max(cast(r.comment_count as integer)) as comment_count,
    max(cast(r.author_playtime_at_review as integer)) as playtime_at_review,
    max(cast(r.author_playtime_forever as integer)) as playtime_forever,
    max(cast(r.author_playtime_last_two_weeks as integer)) as playtime_last_two_weeks,
    max(cast(r.steam_purchase as integer)) as steam_purchase,
    max(cast(r.received_for_free as integer)) as received_for_free,
    max(cast(r.written_during_early_access as integer)) as written_during_early_access,
    max(cast(r.hidden_in_steam_china as integer)) as hidden_in_steam_china,
    max(cast(r.created_at as date)) as created_date,
    max(cast(r.updated_at as date)) as updated_date,
    max(cast(r.author_last_played_at as date)) as last_played_date
from {{ ref('stg_steam') }} r
left join {{ ref('dim_language')}} l on r.language = l.language
group by r.recommendationid, r.appid, r.author_steamid, l.language_id