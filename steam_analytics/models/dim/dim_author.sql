with base as (
    select
        author_steamid,
        author_num_games_owned,
        author_num_reviews,
        author_last_played_at,
        steam_china_location,
        row_number() over(partition by author_steamid) as rn
    from {{ref('stg_steam')}}
)
select
    author_steamid,
    author_num_games_owned,
    author_num_reviews,
    author_last_played_at,
    steam_china_location
FROM base
WHERE rn = 1